package architecture.goldenboughs_lib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

/**
 * 穿透工具类
 * 为弹射物提供穿墙效果和射线检测穿透功能
 *
 * @author ImaginaryCraft
 */
public final class PiercingUtil {

	/**
	 * 穿透标签名称
	 */
	public static final String PIERCING_TAG = "imaginarycraft:piercing";

	//#region 穿墙效果配置
	public static final String PIERCING_CONFIG_KEY = "PiercingConfig";

	private PiercingUtil() {
	}

	/**
	 * 为投射物禁用穿墙效果
	 *
	 * @param projectile 投射物实体
	 */
	public static void disableWallPassThrough(@NotNull Projectile projectile) {
		projectile.noPhysics = false;
	}
	//#endregion
	//#region 射线检测

	/**
	 * 检查投射物是否启用了穿墙效果
	 *
	 * @param projectile 投射物实体
	 * @return 是否启用了穿墙
	 */
	public static boolean isWallPassThroughEnabled(@NotNull Projectile projectile) {
		return projectile.noPhysics;
	}

	/**
	 * 判断实体是否在射线路径上（使用默认容差0.5格）
	 */
	public static boolean isOnRayPath(@NotNull Vec3 start, @NotNull Vec3 end,
	                                  @NotNull Entity entity) {
		return isOnRayPath(start, end, entity, 0.5);
	}

	/**
	 * 判断实体是否在射线路径上
	 *
	 * @param start     射线起点
	 * @param end       射线终点
	 * @param entity    目标实体
	 * @param tolerance 命中容差（格）
	 * @return 实体是否在射线路径上
	 */
	public static boolean isOnRayPath(@NotNull Vec3 start, @NotNull Vec3 end,
	                                  @NotNull Entity entity, double tolerance) {
		Vec3 ray = end.subtract(start);
		Vec3 entityCenter = entity.position().add(0, entity.getBbHeight() / 2, 0);
		Vec3 toEntity = entityCenter.subtract(start);

		double rayLenSq = ray.lengthSqr();
		if (rayLenSq < 1e-6) {
			return start.distanceTo(entityCenter) <= tolerance;
		}

		double t = Mth.clamp(toEntity.dot(ray) / rayLenSq, 0, 1);
		Vec3 closestPoint = start.add(ray.scale(t));

		return entityCenter.distanceTo(closestPoint) <= tolerance + entity.getBbWidth() / 2;
	}

	/**
	 * 获取射线路径上的所有实体（使用默认容差0.5格）
	 */
	public static @NotNull List<Entity> getPiercedEntities(@NotNull Level level,
	                                                       @Nullable Entity shooter,
	                                                       @NotNull Vec3 start,
	                                                       @NotNull Vec3 direction,
	                                                       double maxDistance,
	                                                       int maxPierce) {
		return getPiercedEntities(level, shooter, start, direction, maxDistance, maxPierce, 0.5);
	}

	/**
	 * 获取射线路径上的所有实体（穿透检测）
	 *
	 * @param level       世界
	 * @param shooter     射击者（可null，将被排除）
	 * @param start       起点
	 * @param direction   方向向量
	 * @param maxDistance 最大距离
	 * @param maxPierce   最大穿透数（-1表示无限）
	 * @param tolerance   命中容差（格）
	 * @return 排序后的命中实体列表（按距离排序）
	 */
	public static @NotNull List<Entity> getPiercedEntities(@NotNull Level level,
	                                                       @Nullable Entity shooter,
	                                                       @NotNull Vec3 start,
	                                                       @NotNull Vec3 direction,
	                                                       double maxDistance,
	                                                       int maxPierce,
	                                                       double tolerance) {
		Vec3 end = start.add(direction.normalize().scale(maxDistance));
		AABB searchBox = new AABB(start, end).inflate(tolerance + 1.0);

		Predicate<Entity> filter = entity ->
			entity.isPickable()
				&& !entity.isSpectator()
				&& (shooter == null || !entity.getUUID().equals(shooter.getUUID()));

		List<Entity> candidates = level.getEntities(shooter, searchBox, filter);

		return candidates.stream()
			.filter(e -> isOnRayPath(start, end, e, tolerance))
			.sorted(Comparator.comparingDouble(e -> e.distanceToSqr(start)))
			.limit(maxPierce < 0 ? Long.MAX_VALUE : maxPierce)
			.toList();
	}

	// ==================== 条件性穿透 ====================

	/**
	 * 获取生物视线方向上的穿透实体
	 *
	 * @param shooter     射击者
	 * @param maxDistance 最大距离
	 * @param maxPierce   最大穿透数
	 * @return 命中的实体列表
	 */
	public static @NotNull List<Entity> getPiercedEntitiesFromEye(@NotNull LivingEntity shooter,
	                                                              double maxDistance,
	                                                              int maxPierce,
	                                                              double tolerance) {
		Vec3 eyePos = shooter.getEyePosition(1.0f);
		Vec3 lookVec = shooter.getViewVector(1.0f);
		return getPiercedEntities(shooter.level(), shooter, eyePos, lookVec, maxDistance, maxPierce, tolerance);
	}

	/**
	 * 检查方块是否可穿透
	 *
	 * @param level 世界
	 * @param pos   方块位置
	 * @param state 方块状态
	 * @return 是否可穿透
	 */
	public static boolean canPierceBlock(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
		// 默认：空气、液体、草、花、蜘蛛网等可穿透
		return
			state.isAir()
				|| !state.getFluidState().isEmpty()
				|| state.getCollisionShape(level, pos, CollisionContext.empty()).isEmpty();
	}
	//#endregion
	//#region 穿透伤害计算

	/**
	 * 从起点到终点检测第一个不可穿透的方块
	 *
	 * @param level 世界
	 * @param start 起点
	 * @param end   终点
	 * @return 方块碰撞结果，如果可完全穿透则返回null
	 */
	public static @Nullable BlockHitResult getFirstImpenetrableBlock(@NotNull Level level,
	                                                                 @NotNull Vec3 start,
	                                                                 @NotNull Vec3 end) {
		// 使用 clip 获取所有碰撞
		BlockHitResult result = level.clip(new net.minecraft.world.level.ClipContext(
			start,
			end,
			net.minecraft.world.level.ClipContext.Block.COLLIDER,
			net.minecraft.world.level.ClipContext.Fluid.NONE,
			CollisionContext.empty()
		));

		if (result == null || result.getType() == net.minecraft.world.phys.HitResult.Type.MISS) {
			return null;
		}
		BlockPos hitPos = result.getBlockPos();
		BlockState state = level.getBlockState(result.getBlockPos());
		if (canPierceBlock(level, hitPos, state)) {
			// 可穿透，继续检测下一个方块
			Vec3 newStart = result.getLocation();
			if (newStart.distanceTo(end) < 0.1) {
				return null;  // 已经到达终点
			}
			return getFirstImpenetrableBlock(level, newStart, end);
		}

		return result;
	}

	/**
	 * 计算穿透伤害（递减模式）
	 *
	 * @param baseDamage 基础伤害
	 * @param hitIndex   命中索引（从0开始）
	 * @param decayRate  衰减率（每命中一个目标伤害乘以此值）
	 * @return 计算后的伤害
	 */
	public static float calculatePierceDamage(float baseDamage, int hitIndex, float decayRate) {
		return baseDamage * (float) Math.pow(decayRate, hitIndex);
	}
	//#endregion
	//#region 投射物穿透辅助

	/**
	 * 计算穿透伤害（固定递减模式）
	 *
	 * @param baseDamage      基础伤害
	 * @param hitIndex        命中索引
	 * @param damageReduction 每次穿透减少的伤害值
	 * @return 计算后的伤害
	 */
	public static float calculatePierceDamageFixed(float baseDamage, int hitIndex, float damageReduction) {
		return Math.max(0, baseDamage - hitIndex * damageReduction);
	}

	/**
	 * 为投射物执行穿透检测并返回命中实体
	 * 此方法应在投射物的 tick() 方法中调用
	 *
	 * @param projectile 投射物
	 * @param data       穿透数据
	 * @param tolerance  命中容差
	 * @return 本次 tick 命中的实体列表
	 */
	public static @NotNull List<Entity> performPierceDetection(@NotNull Projectile projectile,
	                                                           @NotNull PierceData data,
	                                                           double tolerance) {
		if (!data.canPierce()) {
			return List.of();
		}

		Level level = projectile.level();
		if (level.isClientSide) {
			return List.of();
		}

		// 使用投射物当前位置和速度方向进行检测
		Vec3 currentPos = projectile.position();
		Vec3 velocity = projectile.getDeltaMovement();
		double speed = velocity.length();

		// 如果速度太小，使用视线方向
		if (speed < 0.01) {
			return List.of();
		}

		// 检测从当前位置到下一帧位置之间的实体
		Vec3 nextPos = currentPos.add(velocity);
		AABB searchBox = projectile.getBoundingBox().expandTowards(velocity).inflate(tolerance);

		Entity owner = projectile.getOwner();
		List<Entity> candidates = level.getEntities(projectile, searchBox, entity ->
			entity.isPickable()
				&& !entity.isSpectator()
				&& !data.hasHitEntity(entity.getId())
				&& (owner == null || !entity.getUUID().equals(owner.getUUID()))
		);

		List<Entity> hitEntities = new ArrayList<>();
		for (Entity entity : candidates) {
			if (isOnRayPath(currentPos, nextPos, entity, tolerance)) {
				hitEntities.add(entity);
				data.recordHit(entity.getId());

				if (!data.canPierce()) {
					break;
				}
			}
		}

		return hitEntities;
	}

	/**
	 * 快速创建穿透数据
	 */
	public static PierceData createPierceData(int maxPierce, float damageDecay, boolean wallPassThrough) {
		return new PierceData(maxPierce, damageDecay, wallPassThrough);
	}

	/**
	 * 创建无限穿透数据
	 */
	public static PierceData createInfinitePierceData(float damageDecay) {
		return new PierceData(-1, damageDecay, true);
	}
	//#endregion
	//#region 标签系统

	/**
	 * 检查弹射物是否有穿透标签
	 *
	 * @param projectile 弹射物实体
	 * @return 是否有穿透标签
	 */
	public static boolean hasPiercingTag(@NotNull Projectile projectile) {
		return projectile.getPersistentData().contains(PIERCING_CONFIG_KEY);
	}

	/**
	 * 更新弹射物的穿透计数（用于持久化）
	 *
	 * @param projectile 弹射物实体
	 * @param data       当前穿透数据
	 */
	public static void updatePiercingProgress(@NotNull Projectile projectile, @NotNull PierceData data) {
		CompoundTag nbt = projectile.getPersistentData();
		if (nbt.contains(PIERCING_CONFIG_KEY)) {
			CompoundTag piercingNbt = nbt.getCompound(PIERCING_CONFIG_KEY);
			piercingNbt.putInt("CurrentPierceCount", data.currentPierceCount);

			// 保存已命中的实体ID列表
			ListTag hitList = new ListTag();
			for (Integer entityId : data.hitEntityIds) {
				hitList.add(StringTag.valueOf(entityId.toString()));
			}
			piercingNbt.put("HitEntities", hitList);
		}
	}

	/**
	 * 从 NBT 恢复穿透进度
	 *
	 * @param projectile 弹射物实体
	 * @return 带有进度的穿透数据
	 */
	@Nullable
	public static PierceData restorePiercingProgress(@NotNull Projectile projectile) {
		PierceData config = getPiercingConfig(projectile);
		if (config == null) {
			return null;
		}

		CompoundTag nbt = projectile.getPersistentData();
		CompoundTag piercingNbt = nbt.getCompound(PIERCING_CONFIG_KEY);

		// 恢复进度
		config.currentPierceCount = piercingNbt.getInt("CurrentPierceCount");

		// 恢复已命中实体列表
		ListTag hitList = piercingNbt.getList("HitEntities", 8); // 8 = StringTag type
		for (int i = 0; i < hitList.size(); i++) {
			try {
				int entityId = Integer.parseInt(hitList.getString(i));
				config.hitEntityIds.add(entityId);
			} catch (NumberFormatException ignored) {
				// 忽略无效ID
			}
		}

		return config;
	}

	/**
	 * 从弹射物获取穿透配置
	 *
	 * @param projectile 弹射物实体
	 * @return 穿透配置，如果没有则返回 null
	 */
	@Nullable
	public static PierceData getPiercingConfig(@NotNull Projectile projectile) {
		CompoundTag nbt = projectile.getPersistentData();
		if (!nbt.contains(PIERCING_CONFIG_KEY)) {
			return null;
		}

		CompoundTag piercingNbt = nbt.getCompound(PIERCING_CONFIG_KEY);
		PierceData config = new PierceData();
		config.maxPierceCount = piercingNbt.getInt("MaxPierce");
		config.damageDecay = piercingNbt.getFloat("DamageDecay");
		config.wallPassThrough = piercingNbt.getBoolean("WallPassThrough");
		config.originalDamage = piercingNbt.getFloat("OriginalDamage");

		return config;
	}

	/**
	 * 快速为弹射物设置穿透效果（简化版）
	 *
	 * @param projectile      弹射物
	 * @param maxPierce       最大穿透数（-1表示无限）
	 * @param damageDecay     伤害衰减率
	 * @param wallPassThrough 是否穿墙
	 */
	public static void setPiercing(@NotNull Projectile projectile, int maxPierce,
	                               float damageDecay, boolean wallPassThrough) {
		PierceData config = new PierceData(maxPierce, damageDecay, wallPassThrough);
		addPiercingTag(projectile, config);
	}

	/**
	 * 为弹射物添加穿透标签
	 *
	 * @param projectile 弹射物实体
	 * @param config     穿透配置
	 */
	public static void addPiercingTag(@NotNull Projectile projectile, @NotNull PierceData config) {
		CompoundTag nbt = projectile.getPersistentData();
		CompoundTag piercingNbt = new CompoundTag();

		// 保存配置
		piercingNbt.putInt("MaxPierce", config.maxPierceCount);
		piercingNbt.putFloat("DamageDecay", config.damageDecay);
		piercingNbt.putBoolean("WallPassThrough", config.wallPassThrough);
		piercingNbt.putFloat("OriginalDamage", config.originalDamage);

		nbt.put(PIERCING_CONFIG_KEY, piercingNbt);

		// 立即启用穿墙效果
		if (config.wallPassThrough) {
			enableWallPassThrough(projectile);
		}
	}

	/**
	 * 为投射物启用穿墙效果
	 * 调用后投射物将无视方块碰撞
	 *
	 * @param projectile 投射物实体
	 */
	public static void enableWallPassThrough(@NotNull Projectile projectile) {
		projectile.noPhysics = true;
	}

	/**
	 * 快速设置穿透效果（使用默认值：5次穿透，0.75衰减，穿墙）
	 *
	 * @param projectile 弹射物
	 * @param damage     原始伤害
	 */
	public static void setPiercingDefault(@NotNull Projectile projectile, float damage) {
		PierceData config = new PierceData(5, 0.75f, true);
		config.originalDamage = damage;
		addPiercingTag(projectile, config);
	}

	/**
	 * 投射物穿透数据容器
	 */
	public static class PierceData {
		private final Set<Integer> hitEntityIds = new HashSet<>();
		private int currentPierceCount = 0;
		private int maxPierceCount = -1;  // -1 = 无限
		private float damageDecay = 0.75f;
		private boolean wallPassThrough = true;
		private float originalDamage = 0;

		public PierceData() {
		}

		public PierceData(int maxPierce, float damageDecay, boolean wallPassThrough) {
			this.maxPierceCount = maxPierce;
			this.damageDecay = damageDecay;
			this.wallPassThrough = wallPassThrough;
		}

		public PierceData maxPierce(int count) {
			this.maxPierceCount = count;
			return this;
		}

		public PierceData damageDecay(float rate) {
			this.damageDecay = rate;
			return this;
		}

		public PierceData wallPassThrough(boolean enabled) {
			this.wallPassThrough = enabled;
			return this;
		}

		public PierceData originalDamage(float damage) {
			this.originalDamage = damage;
			return this;
		}
		//#endregion
		//#region 状态查询

		public boolean canPierce() {
			return maxPierceCount < 0 || currentPierceCount < maxPierceCount;
		}

		public boolean hasHitEntity(int entityId) {
			return hitEntityIds.contains(entityId);
		}

		public void recordHit(int entityId) {
			hitEntityIds.add(entityId);
			currentPierceCount++;
		}

		public float getCurrentDamage() {
			return calculatePierceDamage(originalDamage, currentPierceCount, damageDecay);
		}

		public int getCurrentPierceCount() {
			return currentPierceCount;
		}

		public boolean isWallPassThroughEnabled() {
			return wallPassThrough;
		}

		public Set<Integer> getHitEntityIds() {
			return hitEntityIds;
		}
	}
	//#endregion
}
