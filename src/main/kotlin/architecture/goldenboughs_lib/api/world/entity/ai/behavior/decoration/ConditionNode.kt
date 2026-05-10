package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT

/**
 * 条件装饰节点 - 在执行子节点前检查条件，不满足则立即失败
 * 
 * 继承自 [DecorationNode]，用于在行为执行前进行前置条件验证
 */
class ConditionNode(
	private val condition: ConditionBT,
	child: BTNode
) : DecorationNode(child) {
	override fun execute(): BTStatus? {
		if (!condition.check()) {
			return BTStatus.FAILURE
		}

		child.tryStart()

		if (child.status == BTStatus.RUNNING) {
			child.tick()
		}

		return child.status
	}

	override fun getDescription(): String? {
		return super.getDescription() ?: condition.desc
	}
}
