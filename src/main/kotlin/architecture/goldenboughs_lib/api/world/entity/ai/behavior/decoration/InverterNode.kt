package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 反转节点（成功变失败，失败变成功）
 */
class InverterNode(child: BTNode) : DecorationNode(child) {
	override fun execute(): BTStatus? {
		child.tryStart()

		if (child.status == BTStatus.RUNNING) {
			child.tick()
		}

		if (child.status == BTStatus.RUNNING) {
			return BTStatus.RUNNING
		}

		return if (child.status == BTStatus.SUCCESS) BTStatus.FAILURE else BTStatus.SUCCESS
	}
}
