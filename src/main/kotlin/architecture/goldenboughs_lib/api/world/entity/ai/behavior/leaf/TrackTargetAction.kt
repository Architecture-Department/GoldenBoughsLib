package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3
import kotlin.math.atan2

/**
 * 追踪目标动作 - 实体使用惯性物理系统平滑追踪目标
 * 
 * 实现类似追踪导弹的效果，包含加速度限制、速度限制和空气阻力模拟
 */
class TrackTargetAction @JvmOverloads constructor(
	private val mob: Mob,
	private val speed: Float,
	private val maxAcceleration: Float = 0.1f
) : BTNode() {
	private var currentVelocity: Vec3

	init {
		this.currentVelocity = Vec3.ZERO
	}

	override fun start() {
		super.start()
		currentVelocity = Vec3.ZERO
	}

	override fun execute(): BTStatus {
		val target = this.mob.target
		if (target == null || !target.isAlive) {
			return BTStatus.FAILURE
		}

		val targetPos = target.position()
		val curPos = this.mob.position()

		// 计算期望速度方向
		val desiredDirection = targetPos.subtract(curPos).normalize()

		// 计算期望速度
		val desiredVelocity = desiredDirection.scale(speed.toDouble())

		// 计算转向力（加速度）
		var steeringForce = desiredVelocity.subtract(currentVelocity)

		// 限制加速度
		val steeringMagnitude = steeringForce.length()
		if (steeringMagnitude > maxAcceleration) {
			steeringForce = steeringForce.normalize().scale(maxAcceleration.toDouble())
		}

		// 应用加速度
		currentVelocity = currentVelocity.add(steeringForce)

		// 限制最大速度
		val currentSpeed = currentVelocity.length()
		if (currentSpeed > speed) {
			currentVelocity = currentVelocity.normalize().scale(speed.toDouble())
		}

		// 应用惯性阻尼（模拟空气阻力）
		currentVelocity = currentVelocity.scale(0.95)

		// 设置实体的运动
		mob.deltaMovement = currentVelocity

		// 使实体朝向运动方向
		if (currentVelocity.lengthSqr() > 0.01) {
			val yaw = Math.toDegrees(atan2(-currentVelocity.x, currentVelocity.z)).toFloat()
			mob.yRot = yaw
			mob.setYHeadRot(yaw)
		}

		return BTStatus.RUNNING
	}

	override fun cleanup() {
		super.cleanup()
		// 停止时逐渐减速
		currentVelocity = currentVelocity.scale(0.5)
		mob.deltaMovement = currentVelocity
	}
}
