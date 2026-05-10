package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 重复节点（重复执行指定次数）
 */
class RepeaterNode(
	/**
	 * @param repeatCount 当小于0时infinite为true
	 */
	repeatCount: Int,
	child: BTNode
) : DecorationNode(child) {
	val repeatCount: Int = if (repeatCount < 0) 1 else repeatCount
	val isInfinite: Boolean = repeatCount < 0

	var currentCount: Int = 0

	override fun execute(): BTStatus? {
		while (currentCount < repeatCount) {
			child.tryStart()

			if (child.status == BTStatus.RUNNING) {
				child.tick()
			}

			if (child.status == BTStatus.RUNNING) {
				return BTStatus.RUNNING
			}

			child.stop()

			if (child.status == BTStatus.FAILURE) {
				return BTStatus.FAILURE
			}

			currentCount++
		}
		if (this.isInfinite) {
			currentCount = 0
			cleanup()
			return BTStatus.RUNNING
		}
		return BTStatus.SUCCESS
	}

	override fun cleanup() {
		super.cleanup()
		currentCount = 0
	}
}
