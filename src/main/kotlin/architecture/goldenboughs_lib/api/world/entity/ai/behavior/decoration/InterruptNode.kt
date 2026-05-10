package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 
 */
class InterruptNode protected constructor(child: BTNode) : DecorationNode(child) {
	override fun execute(): BTStatus? {
		return null
	}
}
