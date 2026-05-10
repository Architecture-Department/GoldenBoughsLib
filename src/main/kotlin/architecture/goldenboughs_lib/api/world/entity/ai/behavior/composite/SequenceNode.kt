package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTFactory
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT
import java.util.function.BiFunction

/**
 * 序列节点 - 按顺序执行子节点，全部成功才算成功
 * 
 * 类似逻辑与 (AND) 操作，所有子节点都成功才返回成功
 */
class SequenceNode : CompositeNode() {
	final var currentIndex: Int = 0
		private set

	fun addChild(child: BTNode): SequenceNode {
		children.add(child)
		return this
	}

	fun addWithCondition(condition: ConditionBT, child: BTNode): SequenceNode {
		children.add(BTFactory.condition(condition, child))
		return this
	}

	fun addWithCondition(condition: ConditionBT, desc: String, child: BTNode): SequenceNode {
		children.add(BTFactory.condition(condition, child).setDescription(desc))
		return this
	}

	override fun execute(): BTStatus? {
		while (currentIndex < children.size) {
			val child = children[currentIndex]

			child.tryStart()

			child.tick()

			if (child.canContinueToUse()) {
				return BTStatus.RUNNING
			}

			val childResult = child.status
			child.stop()

			if (childResult == BTStatus.FAILURE) {
				return BTStatus.FAILURE
			}

			currentIndex++
		}

		return BTStatus.SUCCESS
	}

	override fun toString(): String {
		return children.stream().reduce(
			StringBuilder("SequenceNode[").append(currentIndex).append("/").append(children.size).append("|"),
			BiFunction { sb: StringBuilder, node: BTNode ->
				sb.append(",").append(node.javaClass.getSimpleName())
			}
		) { a: StringBuilder, b: StringBuilder -> a }.append("]").toString()
	}

	override fun cleanup() {
		currentIndex = 0
		for (child in children) {
//            if (child.getStatus() == BTStatus.RUNNING) {
			child.stop()
//            }
		}
	}
}
