package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

/**
 * 逻辑非条件
 */
public class NotCondition extends AbstractConditionLeaf {
	ConditionBT child;

	public NotCondition(ConditionBT child) {
		this.child = child;
	}

	@Override
	public boolean check() {
		return !child.check();
	}
}
