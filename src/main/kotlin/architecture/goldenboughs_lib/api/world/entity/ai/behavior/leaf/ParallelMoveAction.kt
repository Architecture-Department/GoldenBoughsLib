package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.motion.DashComponent
import net.minecraft.world.entity.Mob

/**
 * 平行移动动作 - 实体平行于目标位置移动或悬停在目标上方
 * 
 * 典型应用：魔焰眼一阶段的环绕移动、激光眼二阶段的头顶悬停
 */
class ParallelMoveAction(
	val mob: Mob,
	var dist: Float,
	val speed: Float,
	val offsetY: Float
) : BTNode() {
	val component: DashComponent = DashComponent(mob)

	override fun execute(): BTStatus? {
		if (mob.target == null) {
			return BTStatus.FAILURE
		}
		if (dist <= 0) {
			component.hangAbove(mob.target, this.offsetY, this.speed)
		} else {
			component.hangOn(mob.target, dist, this.offsetY, this.speed)
		}
		return BTStatus.RUNNING
	}
}
