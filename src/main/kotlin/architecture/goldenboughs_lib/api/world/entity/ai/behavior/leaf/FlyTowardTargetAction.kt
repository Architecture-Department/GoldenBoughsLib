package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.motion.DashComponent
import net.minecraft.world.entity.Mob

/**
 * 飞向目标动作 - 实体径直向目标移动
 * 
 * 使用 [DashComponent] 实现飞行移动，支持 Y 轴偏移
 */
class FlyTowardTargetAction @JvmOverloads constructor(
	val mob: Mob,
	val speed: Float,
	val offsetY: Float = 0f
) : BTNode() {
	val component: DashComponent = DashComponent(mob)

	override fun execute(): BTStatus? {
		val target = mob.target ?: return BTStatus.FAILURE
		val dir = target.position().subtract(mob.position()).add(0.0, offsetY.toDouble(), 0.0)
		component.direction = dir
		component.uniformMove(this.speed)
		return BTStatus.RUNNING
	}
}
