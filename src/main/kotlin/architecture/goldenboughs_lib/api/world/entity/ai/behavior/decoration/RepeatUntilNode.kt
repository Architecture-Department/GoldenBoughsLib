package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 重复执行直到成功或失败
 */
class RepeatUntilNode(
	/**
	 * @param targetStatus 若child返回targetStatus，则停止执行。取值为SUCCESS或FAILURE。
	 */
	val targetStatus: BTStatus,
	child: BTNode
) : DecorationNode(child) {
    override fun execute(): BTStatus? {
		this.child.tryStart()
		val status = this.child.execute()
		if (status == targetStatus) {
			return BTStatus.SUCCESS
		}
		if (status == BTStatus.RUNNING) {
			return BTStatus.RUNNING
		}
		this.child.stop()

		return BTStatus.RUNNING
	}
}
