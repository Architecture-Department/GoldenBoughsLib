package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.motion.DashComponent
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3

/**
 * 冲刺动作 - 实体向正前方快速冲刺
 * 
 * 使用 [DashComponent] 实现匀速移动，并保持朝向目标方向
 */
class DashAction(val mob: Mob, var speed: Float) : BTNode() {
	val component: DashComponent = DashComponent(mob)
	var targetPos: Vec3? = null

	override fun start() {
		super.start()
		this.component.direction = mob.forward
		targetPos = mob.position().add(mob.forward.normalize().scale(200.0))
	}

	override fun execute(): BTStatus? {
		this.component.uniformMove(speed)
		if (targetPos != null) {
			this.mob.getLookControl().setLookAt(targetPos!!)
			this.mob.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos!!)
		}
		return BTStatus.RUNNING
	}
}
