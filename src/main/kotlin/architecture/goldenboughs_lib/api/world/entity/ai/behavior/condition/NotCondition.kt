package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

/**
 * 逻辑非条件
 */
class NotCondition(
	var child: ConditionBT
) : AbstractConditionLeaf() {
	override fun check(): Boolean {
		return !child.check()
	}
}
