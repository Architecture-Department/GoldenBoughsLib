package architecture.goldenboughs_lib.util

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.pow

/**
 * 穿透工具类
 * 为弹射物提供穿墙效果和射线检测穿透功能
 * 
 * @author GoldenBoughsLib
 */
object PiercingUtil {
	/**
	 * 穿透标签名称
	 */
	const val PIERCING_TAG: String = "goldenboughs_lib:piercing"

	//#region 穿墙效果配置
	const val PIERCING_CONFIG_KEY: String = "PiercingConfig"

	/**
	 * 为投射物禁用穿墙效果
	 *
	 * @param projectile 投射物实体
	 */
	@JvmStatic
	fun disableWallPassThrough(projectile: Projectile) {
		projectile.noPhysics = false
	}

	//#endregion
	//#region 射线检测
	/**
	 * 检查投射物是否启用了穿墙效果
	 *
	 * @param projectile 投射物实体
	 * @return 是否启用了穿墙
	 */
	@JvmStatic
	fun isWallPassThroughEnabled(projectile: Projectile): Boolean {
		return projectile.noPhysics
	}

	/**
	 * 判断实体是否在射线路径上（使用默认容差0.5格）
	 */
	@JvmStatic
	fun isOnRayPath(
		start: Vec3, end: Vec3,
		entity: Entity
	): Boolean {
		return isOnRayPath(start, end, entity, 0.5)
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
	@JvmStatic
	fun isOnRayPath(
		start: Vec3, end: Vec3,
		entity: Entity, tolerance: Double
	): Boolean {
		val ray = end.subtract(start)
		val entityCenter = entity.position().add(0.0, (entity.bbHeight / 2).toDouble(), 0.0)
		val toEntity = entityCenter.subtract(start)

		val rayLenSq = ray.lengthSqr()
		if (rayLenSq < 1e-6) {
			return start.distanceTo(entityCenter) <= tolerance
		}

		val t = Mth.clamp(toEntity.dot(ray) / rayLenSq, 0.0, 1.0)
		val closestPoint = start.add(ray.scale(t))

		return entityCenter.distanceTo(closestPoint) <= tolerance + entity.bbWidth / 2
	}

	/**
	 * 获取射线路径上的所有实体（使用默认容差0.5格）
	 */
	@JvmStatic
	fun getPiercedEntities(
		level: Level,
		shooter: Entity?,
		start: Vec3,
		direction: Vec3,
		maxDistance: Double,
		maxPierce: Int
	): List<Entity?> {
		return getPiercedEntities(level, shooter, start, direction, maxDistance, maxPierce, 0.5)
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
	@JvmStatic
	fun getPiercedEntities(
		level: Level,
		shooter: Entity?,
		start: Vec3,
		direction: Vec3,
		maxDistance: Double,
		maxPierce: Int,
		tolerance: Double
	): List<Entity> {
		val end = start.add(direction.normalize().scale(maxDistance))
		val searchBox = AABB(start, end).inflate(tolerance + 1.0)

		val filter = Predicate { entity: Entity ->
			entity.isPickable &&
				entity.isSpectator &&
				(shooter == null || entity.getUUID() != shooter.getUUID())
		}

		val candidates = level.getEntities(shooter, searchBox, filter)

		return candidates.stream()
			.filter(Predicate { e: Entity -> isOnRayPath(start, end, e, tolerance) })
			.sorted(Comparator.comparingDouble { e: Entity -> e.distanceToSqr(start) })
			.limit(if (maxPierce < 0) Long.MAX_VALUE else maxPierce.toLong())
			.toList()
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
	@JvmStatic
	fun getPiercedEntitiesFromEye(
		shooter: LivingEntity,
		maxDistance: Double,
		maxPierce: Int,
		tolerance: Double
	): List<Entity> {
		val eyePos = shooter.getEyePosition(1.0f)
		val lookVec = shooter.getViewVector(1.0f)
		return getPiercedEntities(shooter.level(), shooter, eyePos, lookVec, maxDistance, maxPierce, tolerance)
	}

	/**
	 * 检查方块是否可穿透
	 *
	 * @param level 世界
	 * @param pos   方块位置
	 * @param state 方块状态
	 * @return 是否可穿透
	 */
	@JvmStatic
	fun canPierceBlock(level: Level, pos: BlockPos, state: BlockState): Boolean {
		// 默认：空气、液体、草、花、蜘蛛网等可穿透
		return state.isAir ||
			!state.fluidState.isEmpty ||
			state.getCollisionShape(level, pos, CollisionContext.empty()).isEmpty
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
	@JvmStatic
	fun getFirstImpenetrableBlock(
		level: Level,
		start: Vec3,
		end: Vec3
	): BlockHitResult? {
		// 使用 clip 获取所有碰撞
		val result = level.clip(
			ClipContext(
				start,
				end,
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				CollisionContext.empty()
			)
		)

		if (result == null || result.type == HitResult.Type.MISS) {
			return null
		}

		val hitPos = result.blockPos
		val state = level.getBlockState(result.blockPos)
		if (canPierceBlock(level, hitPos, state)) {
			// 可穿透，继续检测下一个方块
			val newStart = result.getLocation()
			if (newStart.distanceTo(end) < 0.1) {
				return null // 已经到达终点
			}
			return getFirstImpenetrableBlock(level, newStart, end)
		}

		return result
	}

	/**
	 * 计算穿透伤害（递减模式）
	 *
	 * @param baseDamage 基础伤害
	 * @param hitIndex   命中索引（从0开始）
	 * @param decayRate  衰减率（每命中一个目标伤害乘以此值）
	 * @return 计算后的伤害
	 */
	@JvmStatic
	fun calculatePierceDamage(baseDamage: Float, hitIndex: Int, decayRate: Float): Float {
		return baseDamage * decayRate.toDouble().pow(hitIndex.toDouble()).toFloat()
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
	@JvmStatic
	fun calculatePierceDamageFixed(baseDamage: Float, hitIndex: Int, damageReduction: Float): Float {
		return max(0f, baseDamage - hitIndex * damageReduction)
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
	@JvmStatic
	fun performPierceDetection(
		projectile: Projectile,
		data: PierceData,
		tolerance: Double
	): List<Entity> {
		if (!data.canPierce()) {
			return listOf()
		}

		val level = projectile.level()
		if (level.isClientSide) {
			return listOf()
		}

		// 使用投射物当前位置和速度方向进行检测
		val currentPos = projectile.position()
		val velocity = projectile.deltaMovement
		val speed = velocity.length()

		// 如果速度太小，使用视线方向
		if (speed < 0.01) {
			return listOf()
		}

		// 检测从当前位置到下一帧位置之间的实体
		val nextPos = currentPos.add(velocity)
		val searchBox = projectile.boundingBox.expandTowards(velocity).inflate(tolerance)

		val owner = projectile.owner
		val candidates = level.getEntities(projectile, searchBox) { entity: Entity ->
			entity.isPickable &&
				entity.isSpectator &&
				data.hasHitEntity(entity.id) &&
				(owner == null || entity.getUUID() != owner.getUUID())
		}

		val hitEntities: MutableList<Entity> = ArrayList()
		for (entity in candidates) {
			if (isOnRayPath(currentPos, nextPos, entity, tolerance)) {
				hitEntities.add(entity)
				data.recordHit(entity.id)

				if (!data.canPierce()) {
					break
				}
			}
		}

		return hitEntities
	}

	/**
	 * 快速创建穿透数据
	 */
	@JvmStatic
	fun createPierceData(maxPierce: Int, damageDecay: Float, wallPassThrough: Boolean): PierceData {
		return PierceData(maxPierce, damageDecay, wallPassThrough)
	}

	/**
	 * 创建无限穿透数据
	 */
	@JvmStatic
	fun createInfinitePierceData(damageDecay: Float): PierceData {
		return PierceData(-1, damageDecay, true)
	}

	//#endregion
	//#region 标签系统
	/**
	 * 检查弹射物是否有穿透标签
	 *
	 * @param projectile 弹射物实体
	 * @return 是否有穿透标签
	 */
	@JvmStatic
	fun hasPiercingTag(projectile: Projectile): Boolean {
		return projectile.getPersistentData().contains(PIERCING_CONFIG_KEY)
	}

	/**
	 * 更新弹射物的穿透计数（用于持久化）
	 *
	 * @param projectile 弹射物实体
	 * @param data       当前穿透数据
	 */
	@JvmStatic
	fun updatePiercingProgress(projectile: Projectile, data: PierceData) {
		val nbt = projectile.getPersistentData()
		if (nbt.contains(PIERCING_CONFIG_KEY)) {
			val piercingNbt = nbt.getCompound(PIERCING_CONFIG_KEY)
			piercingNbt.putInt("CurrentPierceCount", data.currentPierceCount)

			// 保存已命中的实体ID列表
			val hitList = ListTag()
			for (entityId in data.hitEntityIds) {
				hitList.add(StringTag.valueOf(entityId.toString()))
			}
			piercingNbt.put("HitEntities", hitList)
		}
	}

	/**
	 * 从 NBT 恢复穿透进度
	 *
	 * @param projectile 弹射物实体
	 * @return 带有进度的穿透数据
	 */
	@JvmStatic
	fun restorePiercingProgress(projectile: Projectile): PierceData? {
		val config = getPiercingConfig(projectile) ?: return null

		val nbt = projectile.getPersistentData()
		val piercingNbt = nbt.getCompound(PIERCING_CONFIG_KEY)

		// 恢复进度
		config.currentPierceCount = piercingNbt.getInt("CurrentPierceCount")

		// 恢复已命中实体列表
		val hitList = piercingNbt.getList("HitEntities", 8) // 8 = StringTag type
		for (i in hitList.indices) {
			try {
				val entityId = Integer.parseInt(hitList.getString(i))
				config.hitEntityIds.add(entityId)
			} catch (ignored: NumberFormatException) {
				// 忽略无效ID
			}
		}

		return config
	}

	/**
	 * 从弹射物获取穿透配置
	 *
	 * @param projectile 弹射物实体
	 * @return 穿透配置，如果没有则返回 null
	 */
	@JvmStatic
	fun getPiercingConfig(projectile: Projectile): PierceData? {
		val nbt = projectile.getPersistentData()
		if (!nbt.contains(PIERCING_CONFIG_KEY)) {
			return null
		}

		val piercingNbt = nbt.getCompound(PIERCING_CONFIG_KEY)
		val config = PierceData()
		config.maxPierceCount = piercingNbt.getInt("MaxPierce")
		config.damageDecay = piercingNbt.getFloat("DamageDecay")
		config.isWallPassThroughEnabled = piercingNbt.getBoolean("WallPassThrough")
		config.originalDamage = piercingNbt.getFloat("OriginalDamage")

		return config
	}

	/**
	 * 快速为弹射物设置穿透效果（简化版）
	 *
	 * @param projectile      弹射物
	 * @param maxPierce       最大穿透数（-1表示无限）
	 * @param damageDecay     伤害衰减率
	 * @param wallPassThrough 是否穿墙
	 */
	@JvmStatic
	fun setPiercing(
		projectile: Projectile, maxPierce: Int,
		damageDecay: Float, wallPassThrough: Boolean
	) {
		val config = PierceData(maxPierce, damageDecay, wallPassThrough)
		addPiercingTag(projectile, config)
	}

	/**
	 * 为弹射物添加穿透标签
	 *
	 * @param projectile 弹射物实体
	 * @param config     穿透配置
	 */
	@JvmStatic
	fun addPiercingTag(projectile: Projectile, config: PierceData) {
		val nbt = projectile.getPersistentData()
		val piercingNbt = CompoundTag()

		// 保存配置
		piercingNbt.putInt("MaxPierce", config.maxPierceCount)
		piercingNbt.putFloat("DamageDecay", config.damageDecay)
		piercingNbt.putBoolean("WallPassThrough", config.isWallPassThroughEnabled)
		piercingNbt.putFloat("OriginalDamage", config.originalDamage)

		nbt.put(PIERCING_CONFIG_KEY, piercingNbt)

		// 立即启用穿墙效果
		if (config.isWallPassThroughEnabled) {
			enableWallPassThrough(projectile)
		}
	}

	/**
	 * 为投射物启用穿墙效果
	 * 调用后投射物将无视方块碰撞
	 *
	 * @param projectile 投射物实体
	 */
	@JvmStatic
	fun enableWallPassThrough(projectile: Projectile) {
		projectile.noPhysics = true
	}

	/**
	 * 快速设置穿透效果（使用默认值：5次穿透，0.75衰减，穿墙）
	 *
	 * @param projectile 弹射物
	 * @param damage     原始伤害
	 */
	@JvmStatic
	fun setPiercingDefault(projectile: Projectile, damage: Float) {
		val config = PierceData(5, 0.75f, true)
		config.originalDamage = damage
		addPiercingTag(projectile, config)
	}

	/**
	 * 投射物穿透数据容器
	 */
	class PierceData {
		val hitEntityIds: MutableSet<Int> = HashSet()
		var currentPierceCount: Int = 0
		var maxPierceCount = -1 // -1 = 无限
		var damageDecay = 0.75f
		var isWallPassThroughEnabled: Boolean = true
		var originalDamage = 0f

		constructor()

		constructor(maxPierce: Int, damageDecay: Float, wallPassThrough: Boolean) {
			this.maxPierceCount = maxPierce
			this.damageDecay = damageDecay
			this.isWallPassThroughEnabled = wallPassThrough
		}

		fun maxPierce(count: Int): PierceData {
			this.maxPierceCount = count
			return this
		}

		fun damageDecay(rate: Float): PierceData {
			this.damageDecay = rate
			return this
		}

		fun wallPassThrough(enabled: Boolean): PierceData {
			this.isWallPassThroughEnabled = enabled
			return this
		}

		fun originalDamage(damage: Float): PierceData {
			this.originalDamage = damage
			return this
		}

		//#endregion
		//#region 状态查询
		fun canPierce(): Boolean {
			return maxPierceCount !in 0..currentPierceCount
		}

		fun hasHitEntity(entityId: Int): Boolean {
			return hitEntityIds.contains(entityId)
		}

		fun recordHit(entityId: Int) {
			hitEntityIds.add(entityId)
			currentPierceCount++
		}

		val currentDamage: Float
			get() = calculatePierceDamage(
				originalDamage,
				currentPierceCount,
				damageDecay
			)
	} //#endregion
}
