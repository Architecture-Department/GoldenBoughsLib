package architecture.goldenboughs_lib.api.world.entity.ai;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义受伤反击目标选择器
 * <p>
 * 当生物受到伤害时，自动将伤害来源设置为目标并进行反击。
 * 支持配置忽略特定类型的伤害来源，以及通知周围同类参与战斗。
 * </p>
 */
public class ModHurtByTargetGoal extends TargetGoal {
	/**
	 * 目标选择条件：忽略视线和隐身
	 */
	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	/**
	 * 警戒通知的垂直搜索范围（方块）
	 */
	private static final int ALERT_RANGE_Y = 10;
	/**
	 * 目标丢失后的记忆持续时间（tick）
	 */
	private static final int UNSEEN_MEMORY_TICKS = 300;

	/**
	 * 是否启用通知同类功能
	 */
	protected final boolean alertSameType;
	/**
	 * 伤害来源忽略判断条件
	 */
	private final Predicate<LivingEntity> ignoreDamagePredicate;
	/**
	 * 通知同类时的忽略判断条件
	 */
	private final Predicate<Mob> ignoreAlertPredicate;
	/**
	 * 附近生物筛选条件
	 */
	private final Predicate<Mob> nearbyMobFilter;
	/**
	 * 上次受伤的时间戳
	 */
	private int lastHurtTimestamp;

	/**
	 * 创建目标选择器
	 */
	protected ModHurtByTargetGoal(Mob mob, boolean alertSameType, @NotNull Predicate<LivingEntity> ignoreDamagePredicate, @NotNull Predicate<Mob> nearbyMobFilter, @NotNull Predicate<Mob> ignoreAlertPredicate) {
		super(mob, true);
		this.alertSameType = alertSameType;
		this.ignoreDamagePredicate = ignoreDamagePredicate;
		this.nearbyMobFilter = nearbyMobFilter;
		this.ignoreAlertPredicate = ignoreAlertPredicate;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	/**
	 * 根据类型数组创建判断谓词
	 */
	protected static <T extends LivingEntity> Predicate<T> createClassPredicate(@NotNull Class<?>... classes) {
		if (classes == null || classes.length == 0) {
			return entity -> false;
		}
		return entity -> {
			for (Class<?> clazz : classes) {
				if (clazz.isAssignableFrom(entity.getClass())) {
					return true;
				}
			}
			return false;
		};
	}

	@Override
	public boolean canUse() {
		LivingEntity attacker = this.mob.getLastHurtByMob();
		if (attacker == null || this.mob.getLastHurtByMobTimestamp() == this.lastHurtTimestamp) {
			return false;
		}

		if (attacker.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
			return false;
		}

		if (this.ignoreDamagePredicate.test(attacker)) {
			return false;
		}

		return this.canAttack(attacker, HURT_BY_TARGETING);
	}

	@Override
	public void start() {
		LivingEntity attacker = this.mob.getLastHurtByMob();
		if (attacker != null) {
			this.mob.setTarget(attacker);
			this.targetMob = this.mob.getTarget();
			this.lastHurtTimestamp = this.mob.getLastHurtByMobTimestamp();
		}

		this.unseenMemoryTicks = UNSEEN_MEMORY_TICKS;
		if (this.alertSameType && attacker != null) {
			this.alertOthers();
		}

		super.start();
	}

	/**
	 * 通知范围内的生物参与战斗
	 */
	protected void alertOthers() {
		LivingEntity target = this.mob.getLastHurtByMob();
		if (target == null) {
			return;
		}

		for (Mob nearbyMob : this.getNearbyMobs()) {
			if (this.shouldAlertMob(nearbyMob, target)) {
				this.alertOther(nearbyMob, target);
			}
		}
	}

	protected void alertOther(Mob mob, LivingEntity target) {
		mob.setTarget(target);
	}

	/**
	 * 获取范围内符合条件的生物
	 */
	protected List<Mob> getNearbyMobs() {
		double followDistance = this.getFollowDistance();
		AABB searchArea = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(followDistance, ALERT_RANGE_Y, followDistance);
		return this.mob.level().getEntitiesOfClass(Mob.class, searchArea, this.nearbyMobFilter.and(EntitySelector.NO_SPECTATORS));
	}

	/**
	 * 判断是否应该通知指定生物
	 */
	protected boolean shouldAlertMob(Mob nearbyMob, LivingEntity target) {
		if (!this.isBasicConditionMet(nearbyMob)) {
			return false;
		}

		if (!this.isOwnerConditionMet(nearbyMob)) {
			return false;
		}

		if (nearbyMob.isAlliedTo(target)) {
			return false;
		}

		return !this.isIgnoredType(nearbyMob);
	}

	/**
	 * 检查基本条件：不是自己且当前没有目标
	 */
	protected boolean isBasicConditionMet(Mob nearbyMob) {
		return nearbyMob != this.mob && nearbyMob.getTarget() == null;
	}

	/**
	 * 检查主人条件：可驯服生物需属于同一主人
	 */
	protected boolean isOwnerConditionMet(Mob nearbyMob) {
		if (this.mob instanceof TamableAnimal tamableSelf && nearbyMob instanceof TamableAnimal tamableOther) {
			return tamableSelf.getOwner() == tamableOther.getOwner();
		}
		return true;
	}

	/**
	 * 检查是否为需要忽略的类型
	 */
	protected boolean isIgnoredType(Mob nearbyMob) {
		return this.ignoreAlertPredicate.test(nearbyMob);
	}

	public static class Builder {
		private boolean alertSameType = false;
		private Predicate<LivingEntity> ignoreDamagePredicate = entity -> false;
		private Predicate<Mob> nearbyMobFilter = entity -> true;
		private Predicate<Mob> ignoreAlertPredicate = mob -> false;

		protected Builder() {
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder withAlertOthers() {
			this.alertSameType = true;
			return this;
		}

		public Builder withIgnoreDamage(@NotNull Predicate<LivingEntity> predicate) {
			this.ignoreDamagePredicate = predicate;
			return this;
		}

		public Builder withIgnoreDamageTypes(@NotNull Class<?>... classes) {
			this.ignoreDamagePredicate = createClassPredicate(classes);
			return this;
		}

		public Builder withNearbyMobFilter(@NotNull Predicate<Mob> predicate) {
			this.alertSameType = true;
			this.nearbyMobFilter = predicate;
			return this;
		}

		public Builder withIgnoreAlertTypes(@NotNull Class<?>... classes) {
			this.alertSameType = true;
			return withIgnoreAlert(createClassPredicate(classes));
		}

		public Builder withIgnoreAlert(@NotNull Predicate<Mob> predicate) {
			this.alertSameType = true;
			this.ignoreAlertPredicate = predicate;
			return this;
		}

		public ModHurtByTargetGoal build(Mob mob) {
			return new ModHurtByTargetGoal(mob, this.alertSameType, this.ignoreDamagePredicate, this.nearbyMobFilter, this.ignoreAlertPredicate);
		}
	}
}
