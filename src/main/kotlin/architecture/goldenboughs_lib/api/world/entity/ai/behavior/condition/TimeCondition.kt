package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import architecture.goldenboughs_lib.util.DateUtils
import net.minecraft.world.level.Level

/**
 * 时间条件
 */
class TimeCondition(
	val from: Int,
	val to: Int,
	val level: Level
) : AbstractConditionLeaf() {
	override fun check(): Boolean {
		val dayTime = (level.dayTime() % 24000).toInt()
		if (from > to) {
			return (dayTime !in (to + 1)..<from)
		}
		return (dayTime in from..to)
	}

	companion object {
		@JvmStatic
		fun isDay(level: Level): TimeCondition {
			return TimeCondition(DateUtils.`_04$30`, DateUtils.`_19$30`, level)
		}

		@JvmStatic
		fun isNight(level: Level): TimeCondition {
			return TimeCondition(DateUtils.`_19$30`, DateUtils.`_04$30`, level)
		}
	}
}
