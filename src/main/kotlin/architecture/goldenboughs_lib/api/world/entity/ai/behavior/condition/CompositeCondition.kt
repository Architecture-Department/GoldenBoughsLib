package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

/**
 * 组合条件抽象基类
 */
abstract class CompositeCondition(
	var children: MutableList<ConditionBT> = ArrayList()
) : AbstractConditionLeaf() {

	fun addChild(child: ConditionBT): CompositeCondition {
		this.children.add(child)
		return this
	}
}
