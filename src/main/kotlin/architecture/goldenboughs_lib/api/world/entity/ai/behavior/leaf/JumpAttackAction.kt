package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3

/**
 * 跳跃攻击动作 - 实体向目标方向跳跃并发动攻击
 * 
 * 包含水平速度和垂直附加速度的控制
 */
class JumpAttackAction @JvmOverloads constructor(
	val mob: Mob,
	val horizonPower: Float,
	val jumpAdditionSpeed: Float = 0f
) : BTNode() {
	override fun execute(): BTStatus {
		if (this.mob.target == null) {
			return BTStatus.FAILURE
		}

		if (!this.mob.onGround()) {
			return BTStatus.RUNNING
		}

		val targetPos = this.mob.target!!.position()
		val horizonDir = targetPos.subtract(this.mob.position()).multiply(1.0, 0.0, 1.0).normalize()
		this.mob.jumpFromGround()
		this.mob.addDeltaMovement(horizonDir.scale(horizonPower.toDouble()))
		this.mob.addDeltaMovement(Vec3(0.0, jumpAdditionSpeed.toDouble(), 0.0))
		return BTStatus.SUCCESS
	}
}
