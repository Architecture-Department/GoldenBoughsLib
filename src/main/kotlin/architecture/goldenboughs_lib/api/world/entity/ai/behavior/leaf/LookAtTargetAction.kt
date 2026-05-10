package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.Mob

/**
 * 注视目标动作 - 实体持续看向当前目标
 * 
 * 同时设置头部旋转和视线控制
 */
class LookAtTargetAction(
	val mob: Mob
) : BTNode() {
	override fun execute(): BTStatus {
		mob.target ?: return BTStatus.FAILURE

		mob.lookAt(mob.target!!, 90f, 85f)
		mob.getLookControl().setLookAt(mob.target!!)

		return BTStatus.RUNNING
	}
}
