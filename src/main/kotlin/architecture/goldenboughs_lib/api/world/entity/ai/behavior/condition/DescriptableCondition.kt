package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

/**
 * 可描述条件抽象类
 */
abstract class DescriptableCondition(
	var description: String?
) : ConditionBT {

	override val desc: String?
		get() = description

	override fun setConDesc(desc: String): ConditionBT {
		this.description = desc
		return this
	}

	override fun check(): Boolean {
		return false
	}
}
