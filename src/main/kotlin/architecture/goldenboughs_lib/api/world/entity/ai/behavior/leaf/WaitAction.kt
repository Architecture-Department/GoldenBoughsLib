package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 等待动作 - 在指定 tick 数内保持运行状态，超时后返回成功
 * 
 * 用于延迟执行或暂停行为树
 */
class WaitAction(var waitTicks: Int) : BTNode() {
	var currentTicks: Int = 0
		protected set

	override fun execute(): BTStatus {
		if (++currentTicks >= waitTicks) {
			return BTStatus.SUCCESS
		}
		return BTStatus.RUNNING
	}

	override fun cleanup() {
		currentTicks = 0
	}
}
