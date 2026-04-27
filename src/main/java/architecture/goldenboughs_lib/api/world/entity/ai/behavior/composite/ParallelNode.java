package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTFactory;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT;
import org.jetbrains.annotations.NotNull;

/**
 * 并行节点 - 同时执行所有子节点，根据策略判断成功或失败
 * <p>支持两种策略：成功策略和失败策略，每种策略可以是 REQUIRE_ONE（只需一个）或 REQUIRE_ALL（需要全部）</p>
 */
public class ParallelNode extends CompositeNode {
	private final Policy successPolicy;
	private final Policy failurePolicy;

	public ParallelNode(Policy successPolicy, Policy failurePolicy) {
		this.successPolicy = successPolicy;
		this.failurePolicy = failurePolicy;
	}

	public ParallelNode addChild(BTNode child) {
		children.add(child);
		return this;
	}

	/**
	 * 添加子节点，并添加条件
	 *
	 * @param condition 条件
	 * @param child     子节点
	 * @return this
	 */
	public ParallelNode addWithCondition(ConditionBT condition, BTNode child) {
		children.add(BTFactory.condition(condition, child));
		return this;
	}

	/**
	 * 添加子节点，并添加条件
	 *
	 * @param condition 条件
	 * @param desc      描述
	 * @param child     子节点
	 * @return this
	 */
	public ParallelNode addWithCondition(ConditionBT condition, String desc, BTNode child) {
		children.add(BTFactory.condition(condition, child).setDesc(desc));
		return this;
	}

	@Override
	public BTStatus execute() {
		int successCount = 0;
		int failureCount = 0;
		int runningCount = 0;

		for (BTNode child : children) {

			child.tryStart();

			if (child.getStatus() == BTStatus.RUNNING) {
				child.tick();
//        if(!child.canContinueToUse()) {
//            child.setStatus(BTStatus.FAILURE);
//        }
			}

			switch (child.getStatus()) {
				case SUCCESS -> successCount++;
				case FAILURE -> failureCount++;
				case RUNNING -> runningCount++;
			}
		}

		// 检查成功策略
		if (successPolicy == Policy.REQUIRE_ONE && successCount > 0) {
			return BTStatus.SUCCESS;
		}
		if (successPolicy == Policy.REQUIRE_ALL && successCount == children.size()) {
			return BTStatus.SUCCESS;
		}

		// 检查失败策略
		if (failurePolicy == Policy.REQUIRE_ONE && failureCount > 0) {
			return BTStatus.FAILURE;
		}
		if (failurePolicy == Policy.REQUIRE_ALL && failureCount == children.size()) {
			return BTStatus.FAILURE;
		}

		return runningCount > 0 ? BTStatus.RUNNING : BTStatus.FAILURE;
	}

	@Override
	protected void cleanup() {
		for (BTNode child : children) {
//      if(child.getStatus() == BTStatus.RUNNING){
			child.stop();
//      }
		}
	}

	@Override
	public @NotNull String toString() {
		return children.stream().reduce(
			new StringBuilder("ParallelNode[").append(children.size()).append("|"),
			(sb, node) -> sb.append(",").append(node.getClass().getSimpleName()),
			(a, b) -> a).append("]").toString();
	}

	public enum Policy {
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
