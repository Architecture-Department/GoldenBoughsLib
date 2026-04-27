package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合条件抽象基类
 */
public abstract class CompositeCondition extends AbstractConditionLeaf {
	List<ConditionBT> children;

	public CompositeCondition() {
		this.children = new ArrayList<>();
	}

	public CompositeCondition(List<ConditionBT> children) {
		this.children = children;
	}

	public CompositeCondition addChild(ConditionBT child) {
		this.children.add(child);
		return this;
	}
}
