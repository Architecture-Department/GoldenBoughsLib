package architecture.goldenboughs_lib.api.world.entity.ai.behavior

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.world.entity.ai.goal.Goal

/**
 * 行为树节点基类
 */
@AllOpe
abstract class BTNode : Goal() {
	var status: BTStatus = BTStatus.READY
	protected var context: BehaviorTreeContext? = null

	var description: String? = null

	fun tryStart() {
		if (this.isReady) {
			start()
		}
	}

	val isReady: Boolean
		get() = canUse() && status == BTStatus.READY

	override fun canUse(): Boolean {
		if (status == BTStatus.READY) {
			return true
		}
		return status == BTStatus.RUNNING
	}

	override fun start() {
		this.status = BTStatus.RUNNING
		this.context = createContext()
	}

	protected fun createContext(): BehaviorTreeContext {
		return BehaviorTreeContext()
	}

	override fun canContinueToUse(): Boolean {
		return status == BTStatus.RUNNING
	}

	override fun tick() {
		if (status == BTStatus.RUNNING) {
			val result = execute()
			if (result != BTStatus.RUNNING) {
				if (result != null) {
					status = result
				}
			}
		}
	}

	abstract fun execute(): BTStatus?

	override fun stop() {
		cleanup()
		status = BTStatus.READY
		context = null
	}

	protected fun cleanup() {
		// 子类可重写清理逻辑
	}

	fun setDescription(desc: String): BTNode {
		this.description = desc
		return this
	}

	override fun toString(): String {
		if (description != null) {
			return description!!
		}
		return super.toString()
	}

	enum class BTStatus {
		READY,  // 准备执行
		RUNNING,  // 执行中
		SUCCESS,  // 执行成功
		FAILURE // 执行失败
	}

	class BehaviorTreeContext {
		private val blackboard: Any? = null

		fun <T> getData(key: String): T? {
			// 实际实现需要存储上下文数据
			return null
		}

		fun setData(key: String, value: Any) {
			// 存储上下文数据
		}
	}
}
