package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3

/**
 * 向前跳跃动作 - 实体向当前朝向方向跳跃
 * 
 * 包含水平速度和垂直附加速度的控制，不依赖目标
 */
class JumpForwardAction @JvmOverloads constructor(
	val mob: Mob,
	val horizonPower: Float,
	val jumpAdditionSpeed: Float = 0f
) : BTNode() {
	override fun execute(): BTStatus? {
		if (!this.mob.onGround()) {
			return BTStatus.RUNNING
		}
		val horizonDir = this.mob.forward.multiply(1.0, 0.0, 1.0).normalize()
		this.mob.jumpFromGround()
		this.mob.addDeltaMovement(horizonDir.scale(horizonPower.toDouble()))
		this.mob.addDeltaMovement(Vec3(0.0, jumpAdditionSpeed.toDouble(), 0.0))
		return BTStatus.SUCCESS
	}
}
