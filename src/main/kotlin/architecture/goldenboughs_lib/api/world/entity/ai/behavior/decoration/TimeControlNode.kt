package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

class TimeControlNode(
	val duration: Int,
	child: BTNode
) : DecorationNode(child) {
	var tick: Int = 0

    override fun execute(): BTStatus? {
		if (tick >= duration) {
			return BTStatus.SUCCESS
		}
		tick++
		child.execute()
		return BTStatus.RUNNING
	}

	override fun cleanup() {
		super.cleanup()
		this.tick = 0
	}
}
