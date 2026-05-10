package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT


/**
 * 条件检查动作 - 根据条件返回成功或失败，用于短路 Sequence 或 Selector 节点
 * 
 * 当条件满足时返回 SUCCESS，否则返回 FAILURE
 */
class ConditionAction(val condition: ConditionBT) : BTNode() {
	override fun execute(): BTStatus {
		if (condition.check()) {
			return BTStatus.SUCCESS
		}
		return BTStatus.FAILURE
	}
}
