package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import com.google.common.collect.Lists

/**
 * 行为树条件接口 - 用于检查行为执行的前提条件
 * 
 * 实现此接口的类可用于条件节点或带条件的行为添加
 */
@FunctionalInterface
fun interface ConditionBT {
	fun check(): Boolean

	val desc: String?
		get() = null

	fun setConDesc(desc: String): ConditionBT {
		return this
	}

	companion object {
		/**
		 * 非
		 */
		@JvmStatic
		fun not(condition: ConditionBT): NotCondition {
			return NotCondition(condition)
		}

		/**
		 * 与
		 */
		@JvmStatic
		fun and(vararg conditions: ConditionBT): AndCondition {
			return AndCondition(Lists.newArrayList(*conditions))
		}

		/**
		 * 或
		 */
		@JvmStatic
		fun or(vararg conditions: ConditionBT): OrCondition {
			return OrCondition(Lists.newArrayList(*conditions))
		}
	}
}

