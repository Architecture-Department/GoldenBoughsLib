package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

/**
 * 条件组合逻辑 - 或
 */
class OrCondition(children: MutableList<ConditionBT> = ArrayList()) : CompositeCondition(children) {

	override fun addChild(child: ConditionBT): OrCondition {
		this.children.add(child)
		return this
	}

	override fun check(): Boolean {
		return children.stream().anyMatch { obj -> obj.check() }
	}
}
