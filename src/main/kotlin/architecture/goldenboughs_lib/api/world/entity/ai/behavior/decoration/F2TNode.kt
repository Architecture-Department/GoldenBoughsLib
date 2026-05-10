package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 当状态为失败时，将其转换为成功状态。用于sequence防止短路
 */
class F2TNode(child: BTNode) : DecorationNode(child) {
	override fun execute(): BTStatus? {
		val status = child.execute()
		if (status == BTStatus.FAILURE) {
			return BTStatus.SUCCESS
		}
		return status
	}
}
