package architecture.goldenboughs_lib.api.world.entity.ai.motion

import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

/**
 * 冲刺组件 - 为实体提供移动和定位功能
 * 
 * 支持悬停、加速、匀速移动、预判等运动控制
 */
class DashComponent(var owner: Entity) {
	var targetPos: Vec3
	var direction: Vec3

	init {
		this.direction = Vec3.ZERO
		this.targetPos = owner.position()
	}

	/**
	 * 悬挂在目标实体目标位置
	 *
	 * @param target   目标实体
	 * @param distance xz距离
	 * @param height   高度
	 * @param speed    速度
	 */
	fun hangOn(target: LivingEntity?, distance: Float, height: Float, speed: Float) {
		if (target != null) {
			setNearestTargetPos(target, distance, height)
			direction = targetPos.subtract(owner.position())
			owner.addDeltaMovement(direction.scale((speed * 0.01f).toDouble()))
			if (owner.distanceToSqr(target) < 2) {
				owner.deltaMovement = owner.deltaMovement.scale(0.95)
			}
		}
	}

	/**
	 * 获取目标相对直线位置
	 *
	 * @param target   目标实体
	 * @param distance xz距离
	 * @param height   高度
	 */
	fun setNearestTargetPos(target: Entity, distance: Float, height: Float): Vec3 {
		return owner.position().subtract(target.position()).multiply(1.0, 0.0, 1.0).normalize()
			.scale(distance.toDouble()).add(0.0, height.toDouble(), 0.0).add(target.position()).also { targetPos = it }
	}

	/**
	 * 悬挂在目标实体目标头顶
	 *
	 * @param target 目标实体
	 * @param height 高度
	 * @param speed  速度
	 */
	fun hangAbove(target: LivingEntity?, height: Float, speed: Float) {
		if (target != null) {
			targetPos = target.position().add(0.0, height.toDouble(), 0.0)
			direction = targetPos.subtract(owner.position())
			owner.addDeltaMovement(direction.scale((speed * 0.01f).toDouble()))
			if (owner.distanceToSqr(target) < 2) {
				owner.deltaMovement = owner.deltaMovement.scale(0.95)
			}
		}
	}

	fun accelerate(speed: Float) {
		owner.addDeltaMovement(direction.normalize().scale(speed.toDouble()))
	}

	fun uniformMove(speed: Float) {
		owner.deltaMovement = direction.normalize().scale(speed.toDouble())
	}

	/**
	 * 预判冲刺方向
	 *
	 * @param target 目标实体
	 * @return 冲刺方向
	 */
	fun setPredictDirection(target: Entity) {
		direction =
			target.position().add(0.0, 1.0, 0.0).add(target.knownMovement.scale(10.0)).subtract(owner.position())
	}

	fun lookAtDirection() {
		owner.lookAt(EntityAnchorArgument.Anchor.EYES, direction.add(owner.position()))
	}
}
