package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import java.util.List;

/**
 * 条件组合逻辑 - 或
 */
public class OrCondition extends CompositeCondition {
	public OrCondition() {
	}

	public OrCondition(List<ConditionBT> children) {
		super(children);
	}

	@Override
	public OrCondition addChild(ConditionBT child) {
		this.children.add(child);
		return this;
	}

	@Override
	public boolean check() {
		return children.stream().anyMatch(ConditionBT::check);
	}

}
