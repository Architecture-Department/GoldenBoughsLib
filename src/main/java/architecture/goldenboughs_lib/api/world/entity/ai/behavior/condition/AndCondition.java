package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import java.util.List;

/**
 * 与条件
 */
public class AndCondition extends CompositeCondition {
	public AndCondition() {
	}

	public AndCondition(List<ConditionBT> children) {
		super(children);
	}

	@Override
	public AndCondition addChild(ConditionBT child) {
		this.children.add(child);
		return this;
	}

	@Override
	public boolean check() {
		return children.stream().allMatch(ConditionBT::check);
	}
}
