package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

/**
 * 与条件
 */
class AndCondition(children: MutableList<ConditionBT> = ArrayList()) : CompositeCondition(children) {

	override fun addChild(child: ConditionBT): AndCondition {
		this.children.add(child)
		return this
	}

	override fun check(): Boolean {
		return children.stream().allMatch(ConditionBT::check)
	}
}
