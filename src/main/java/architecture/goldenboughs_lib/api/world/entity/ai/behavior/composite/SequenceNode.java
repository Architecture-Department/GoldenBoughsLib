package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTFactory;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT;
import org.jetbrains.annotations.NotNull;

/**
 * 序列节点 - 按顺序执行子节点，全部成功才算成功
 * <p>类似逻辑与 (AND) 操作，所有子节点都成功才返回成功</p>
 */
public class SequenceNode extends CompositeNode {
	private int currentIndex = 0;

	public SequenceNode addChild(BTNode child) {
		children.add(child);
		return this;
	}

	public SequenceNode addWithCondition(ConditionBT condition, BTNode child) {
		children.add(BTFactory.condition(condition, child));
		return this;
	}

	public SequenceNode addWithCondition(ConditionBT condition, String desc, BTNode child) {
		children.add(BTFactory.condition(condition, child).setDesc(desc));
		return this;
	}

	@Override
	public BTStatus execute() {
		while (currentIndex < children.size()) {
			BTNode child = children.get(currentIndex);

			child.tryStart();

			child.tick();

			if (child.canContinueToUse()) {
				return BTStatus.RUNNING;
			}

			BTStatus childResult = child.getStatus();
			child.stop();

			if (childResult == BTStatus.FAILURE) {
				return BTStatus.FAILURE;
			}

			currentIndex++;
		}

		return BTStatus.SUCCESS;
	}

	@Override
	public @NotNull String toString() {

		return children.stream().reduce(
			new StringBuilder("SequenceNode[").append(currentIndex).append("/").append(children.size()).append("|"),
			(sb, node) -> sb.append(",").append(node.getClass().getSimpleName()),
			(a, b) -> a).append("]").toString();
	}

	@Override
	protected void cleanup() {
		currentIndex = 0;
		for (BTNode child : children) {
//            if (child.getStatus() == BTStatus.RUNNING) {
			child.stop();
//            }
		}
	}

	public int getCurrentIndex() {
		return currentIndex;
	}
}
