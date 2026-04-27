package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTFactory;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT;
import org.jetbrains.annotations.NotNull;

/**
 * 选择节点 - 按顺序执行子节点，直到有一个子节点成功或全部失败
 * <p>类似逻辑或 (OR) 操作，只要有一个子节点成功就返回成功</p>
 */
public class SelectorNode extends CompositeNode {
	private int currentIndex = 0;

	public SelectorNode addChild(BTNode child) {
		children.add(child);
		return this;
	}

	public SelectorNode addWithCondition(ConditionBT condition, BTNode child) {
		children.add(BTFactory.condition(condition, child));
		return this;
	}

	public SelectorNode addWithCondition(ConditionBT condition, String desc, BTNode child) {
		children.add(BTFactory.condition(condition, child).setDesc(desc));
		return this;
	}

	@Override
	public BTStatus execute() {
		while (currentIndex < children.size()) {

			BTNode child = children.get(currentIndex);
//            if(children.get(0) instanceof SequenceNode) {
//                System.out.println(currentIndex);
//            }
			if (child.isReady()) {
				if (currentIndex > 0) {
					children.get(currentIndex - 1).stop();
				}
				child.start();
			}

			child.tick();

			if (child.canContinueToUse()) {
				return BTStatus.RUNNING;
			}

			BTStatus childResult = child.getStatus();
			child.stop();

			if (childResult == BTStatus.SUCCESS) {
				return BTStatus.SUCCESS;
			}

			currentIndex++;
		}

		return BTStatus.FAILURE;
	}

	@Override
	public @NotNull String toString() {
		return children.stream().reduce(
			new StringBuilder("SelectorNode[").append(currentIndex).append("/").append(children.size()).append("|"),
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
