package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTFactory
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode.BTStatus.*
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT
import java.util.function.BiFunction
import java.util.function.BinaryOperator

/**
 * 并行节点 - 同时执行所有子节点，根据策略判断成功或失败
 * 
 * 支持两种策略：成功策略和失败策略，每种策略可以是 REQUIRE_ONE（只需一个）或 REQUIRE_ALL（需要全部）
 */
class ParallelNode(
	private val successPolicy: Policy,
	private val failurePolicy: Policy
) : CompositeNode() {
	fun addChild(child: BTNode): ParallelNode {
		children.add(child)
		return this
	}

	/**
	 * 添加子节点，并添加条件
	 * 
	 * @param condition 条件
	 * @param child     子节点
	 * @return this
	 */
	fun addWithCondition(condition: ConditionBT, child: BTNode): ParallelNode {
		children.add(BTFactory.condition(condition, child))
		return this
	}

	/**
	 * 添加子节点，并添加条件
	 * 
	 * @param condition 条件
	 * @param desc      描述
	 * @param child     子节点
	 * @return this
	 */
	fun addWithCondition(condition: ConditionBT, desc: String, child: BTNode): ParallelNode {
		children.add(BTFactory.condition(condition, child).setDescription(desc))
		return this
	}

	override fun execute(): BTStatus? {
		var successCount = 0
		var failureCount = 0
		var runningCount = 0

		for (child in children) {
			child.tryStart()

			if (child.status == RUNNING) {
				child.tick()
//        if(!child.canContinueToUse()) {
//            child.setStatus(BTStatus.FAILURE);
//        }
			}

			when (child.status) {
				SUCCESS -> successCount++
				FAILURE -> failureCount++
				RUNNING -> runningCount++
				READY -> {}
			}
		}

		// 检查成功策略
		if (successPolicy == Policy.REQUIRE_ONE && successCount > 0) {
			return SUCCESS
		}
		if (successPolicy == Policy.REQUIRE_ALL && successCount == children.size) {
			return SUCCESS
		}

		// 检查失败策略
		if (failurePolicy == Policy.REQUIRE_ONE && failureCount > 0) {
			return FAILURE
		}
		if (failurePolicy == Policy.REQUIRE_ALL && failureCount == children.size) {
			return FAILURE
		}

		return if (runningCount > 0) RUNNING else FAILURE
	}

	override fun cleanup() {
		for (child in children) {
//      if(child.status == BTStatus.RUNNING){
			child.stop()
			//      }
		}
	}

	override fun toString(): String {
		return children.stream().reduce<StringBuilder>(
			StringBuilder("ParallelNode[").append(children.size).append("|"),
			BiFunction { sb: StringBuilder, node: BTNode ->
				sb.append(",").append(node.javaClass.getSimpleName())
			},
			BinaryOperator { a: StringBuilder, b: StringBuilder -> a }).append("]").toString()
	}

	enum class Policy {
		/**
		 * 只需一个
		 */
		REQUIRE_ONE,

		/**
		 * 需要全部
		 */
		REQUIRE_ALL
	}
}
