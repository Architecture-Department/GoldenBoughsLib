package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTFactory
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT
import java.util.function.BiFunction

/**
 * 选择节点 - 按顺序执行子节点，直到有一个子节点成功或全部失败
 * 
 * 类似逻辑或 (OR) 操作，只要有一个子节点成功就返回成功
 */
class SelectorNode : CompositeNode() {
	final var currentIndex: Int = 0
		private set

	fun addChild(child: BTNode): SelectorNode {
		children.add(child)
		return this
	}

	fun addWithCondition(condition: ConditionBT, child: BTNode): SelectorNode {
		children.add(BTFactory.condition(condition, child))
		return this
	}

	fun addWithCondition(condition: ConditionBT, desc: String, child: BTNode): SelectorNode {
		children.add(BTFactory.condition(condition, child).setDescription(desc))
		return this
	}

	override fun execute(): BTStatus? {
		while (currentIndex < children.size) {
			val child = children[currentIndex]
//            if(children.get(0) instanceof SequenceNode) {
//                System.out.println(currentIndex);
//            }
			if (child.isReady) {
				if (currentIndex > 0) {
					children[currentIndex - 1].stop()
				}
				child.start()
			}

			child.tick()

			if (child.canContinueToUse()) {
				return BTStatus.RUNNING
			}

			val childResult = child.status
			child.stop()

			if (childResult == BTStatus.SUCCESS) {
				return BTStatus.SUCCESS
			}

			currentIndex++
		}

		return BTStatus.FAILURE
	}

	override fun toString(): String {
		return children.stream().reduce<StringBuilder>(
			StringBuilder("SelectorNode[").append(currentIndex).append("/").append(children.size).append("|"),
			BiFunction { sb: StringBuilder, node: BTNode ->
				sb.append(",").append(node.javaClass.getSimpleName())
			}
		) { a: StringBuilder, b: StringBuilder -> a }.append("]").toString()
	}

	override fun cleanup() {
		currentIndex = 0
		for (child in children) {
//      if (child.getStatus() == BTStatus.RUNNING) {
			child.stop()
//      }
		}
	}
}
