package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT;
import org.jetbrains.annotations.Nullable;

/**
 * 条件装饰节点 - 在执行子节点前检查条件，不满足则立即失败
 * <p>继承自 {@link DecorationNode}，用于在行为执行前进行前置条件验证</p>
 */
public class ConditionNode extends DecorationNode {
	private final ConditionBT condition;

	public ConditionNode(ConditionBT condition, BTNode child) {
		super(child);
		this.condition = condition;
	}

	@Override
	public BTStatus execute() {
		if (!condition.check()) {
			return BTStatus.FAILURE;
		}

		child.tryStart();

		if (child.getStatus() == BTStatus.RUNNING) {
			child.tick();
		}

		return child.getStatus();
	}

	@Override
	public @Nullable String getDesc() {
		if (super.getDesc() != null) {
			return super.getDesc();
		}
		return condition.getDesc();
	}
}
