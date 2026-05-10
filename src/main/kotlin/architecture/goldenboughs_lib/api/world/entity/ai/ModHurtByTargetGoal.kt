package architecture.goldenboughs_lib.api.world.entity.ai

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.level.GameRules
import net.minecraft.world.phys.AABB
import java.util.*
import java.util.function.Predicate

/**
 * 自定义受伤反击目标选择器
 * 
 * 
 * 当生物受到伤害时，自动将伤害来源设置为目标并进行反击。
 * 支持配置忽略特定类型的伤害来源，以及通知周围同类参与战斗。
 * 
 */
@AllOpe
class ModHurtByTargetGoal(
	mob: Mob,
	/**
	 * 是否启用通知同类功能
	 */
	protected val alertSameType: Boolean,
	/**
	 * 伤害来源忽略判断条件
	 */
	private val ignoreDamagePredicate: Predicate<LivingEntity>,
	/**
	 * 附近生物筛选条件
	 */
	private val nearbyMobFilter: Predicate<Mob>,
	/**
	 * 通知同类时的忽略判断条件
	 */
	private val ignoreAlertPredicate: Predicate<Mob>
) : TargetGoal(mob, true) {
	/**
	 * 上次受伤的时间戳
	 */
	private var lastHurtTimestamp = 0

	/**
	 * 创建目标选择器
	 */
	init {
		this.setFlags(EnumSet.of(Flag.TARGET))
	}

	override fun canUse(): Boolean {
		val attacker = this.mob.lastHurtByMob
		if (attacker == null || this.mob.lastHurtByMobTimestamp == this.lastHurtTimestamp) {
			return false
		}

		if (attacker.type === EntityType.PLAYER &&
			this.mob.level().gameRules.getBoolean(GameRules.RULE_UNIVERSAL_ANGER)
		) {
			return false
		}

		if (this.ignoreDamagePredicate.test(attacker)) {
			return false
		}

		return this.canAttack(attacker, HURT_BY_TARGETING)
	}

	override fun start() {
		val attacker = this.mob.lastHurtByMob
		if (attacker != null) {
			this.mob.setTarget(attacker)
			this.targetMob = this.mob.target
			this.lastHurtTimestamp = this.mob.lastHurtByMobTimestamp
		}

		this.unseenMemoryTicks = UNSEEN_MEMORY_TICKS
		if (this.alertSameType && attacker != null) {
			this.alertOthers()
		}

		super.start()
	}

	/**
	 * 通知范围内的生物参与战斗
	 */
	protected fun alertOthers() {
		val target = this.mob.lastHurtByMob ?: return

		for (nearbyMob in this.nearbyMobs) {
			if (this.shouldAlertMob(nearbyMob, target)) {
				this.alertOther(nearbyMob, target)
			}
		}
	}

	protected fun alertOther(mob: Mob, target: LivingEntity) {
		mob.setTarget(target)
	}

	protected val nearbyMobs: MutableList<Mob>
		/**
		 * 获取范围内符合条件的生物
		 */
		get() {
			val followDistance = this.followDistance
			val searchArea =
				AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(
					followDistance,
					ALERT_RANGE_Y.toDouble(),
					followDistance
				)
			return this.mob.level().getEntitiesOfClass(
				Mob::class.java,
				searchArea,
				this.nearbyMobFilter.and(EntitySelector.NO_SPECTATORS)
			)
		}

	/**
	 * 判断是否应该通知指定生物
	 */
	protected fun shouldAlertMob(nearbyMob: Mob, target: LivingEntity): Boolean {
		if (!this.isBasicConditionMet(nearbyMob)) {
			return false
		}

		if (!this.isOwnerConditionMet(nearbyMob)) {
			return false
		}

		if (nearbyMob.isAlliedTo(target)) {
			return false
		}

		return !this.isIgnoredType(nearbyMob)
	}

	/**
	 * 检查基本条件：不是自己且当前没有目标
	 */
	protected fun isBasicConditionMet(nearbyMob: Mob): Boolean {
		return nearbyMob !== this.mob && nearbyMob.target == null
	}

	/**
	 * 检查主人条件：可驯服生物需属于同一主人
	 */
	protected fun isOwnerConditionMet(nearbyMob: Mob): Boolean {
		if (this.mob is TamableAnimal && nearbyMob is TamableAnimal) {
			return (mob as TamableAnimal).getOwner() === nearbyMob.getOwner()
		}
		return true
	}

	/**
	 * 检查是否为需要忽略的类型
	 */
	protected fun isIgnoredType(nearbyMob: Mob): Boolean {
		return this.ignoreAlertPredicate.test(nearbyMob)
	}

	class Builder {
		private var alertSameType = false
		private var ignoreDamagePredicate = Predicate { entity: LivingEntity -> false }
		private var nearbyMobFilter = Predicate { entity: Mob -> true }
		private var ignoreAlertPredicate = Predicate { mob: Mob -> false }

		fun withAlertOthers(): Builder {
			this.alertSameType = true
			return this
		}

		fun withIgnoreDamage(predicate: Predicate<LivingEntity>): Builder {
			this.ignoreDamagePredicate = predicate
			return this
		}

		fun withIgnoreDamageTypes(vararg classes: Class<*>): Builder {
			this.ignoreDamagePredicate = createClassPredicate<LivingEntity>(*classes)
			return this
		}

		fun withNearbyMobFilter(predicate: Predicate<Mob>): Builder {
			this.alertSameType = true
			this.nearbyMobFilter = predicate
			return this
		}

		fun withIgnoreAlertTypes(vararg classes: Class<*>): Builder {
			this.alertSameType = true
			return withIgnoreAlert(createClassPredicate(*classes))
		}

		fun withIgnoreAlert(predicate: Predicate<Mob>): Builder {
			this.alertSameType = true
			this.ignoreAlertPredicate = predicate
			return this
		}

		fun build(mob: Mob): ModHurtByTargetGoal {
			return ModHurtByTargetGoal(
				mob,
				this.alertSameType,
				this.ignoreDamagePredicate,
				this.nearbyMobFilter,
				this.ignoreAlertPredicate
			)
		}

		companion object {
			fun create(): Builder {
				return Builder()
			}
		}
	}

	companion object {
		/**
		 * 目标选择条件：忽略视线和隐身
		 */
		private val HURT_BY_TARGETING: TargetingConditions =
			TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting()

		/**
		 * 警戒通知的垂直搜索范围（方块）
		 */
		private const val ALERT_RANGE_Y = 10

		/**
		 * 目标丢失后的记忆持续时间（tick）
		 */
		private const val UNSEEN_MEMORY_TICKS = 300

		/**
		 * 根据类型数组创建判断谓词
		 */
		@JvmStatic
		protected fun <T : LivingEntity> createClassPredicate(vararg classes: Class<*>): Predicate<T> {
			if (classes.isEmpty()) {
				return { entity: T -> false }
			}
			return Predicate { entity: T ->
				for (clazz in classes) {
					if (clazz.isAssignableFrom(entity.javaClass)) {
						return@Predicate true
					}
				}
				false
			}
		}
	}
}
