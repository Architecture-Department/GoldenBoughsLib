package architecture.goldenboughs_lib.util

import net.minecraft.Util
import net.minecraft.world.level.Level
import java.time.format.DateTimeParseException
import java.util.function.Consumer

object DateUtils {
	@JvmStatic
	private val TIMES: ShortArray = Util.make<ShortArray>(ShortArray(24 * 60), Consumer { times: ShortArray ->
		for (h in 0..23) {
			val t = h * 60
			var i = (h - 6) * 1000
			if (i < 0) i += 24000
			for (m in 0..59) {
				times[t + m] = (i + (m / 0.06f).toInt()).toShort()
			}
		}
	})

	@JvmField
	val `_00$00`: Int = getDayTime(0, 0)

	@JvmField
	val `_04$30`: Int = getDayTime(4, 30)

	@JvmField
	val `_06$00`: Int = getDayTime(6, 0)

	@JvmField
	val `_18$00`: Int = getDayTime(18, 0)

	@JvmField
	val `_19$30`: Int = getDayTime(19, 30)

	@JvmStatic
	fun isWithinDayTime(start: Int, end: Int, level: Level): Boolean {
		return isWithinDayTime(start, end, getDayTime(level))
	}

	@JvmStatic
	fun getDayTime(level: Level): Int {
		return getDayTime(level.dayTime)
	}

	@JvmStatic
	fun getDayTime(dayTime: Long): Int {
		return (dayTime % 24000L).toInt()
	}

	/** @param start   开始的dayTime
	 * @param end     结束的dayTime
	 * @param dayTime 判断的dayTime
	 * @return start <= dayTime <= end
	 */
	@JvmStatic
	fun isWithinDayTime(start: Int, end: Int, dayTime: Int): Boolean {
		if (start > end) {
			return dayTime >= start || dayTime <= end
		}
		return dayTime >= start && dayTime <= end
	}

	@JvmStatic
	fun isWithinDayTime(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int, level: Level): Boolean {
		return isWithinDayTime(startHour, startMinute, endHour, endMinute, getDayTime(level))
	}

	@JvmStatic
	fun isWithinDayTime(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int, dayTime: Int): Boolean {
		return isWithinDayTime(getDayTime(startHour, startMinute), getDayTime(endHour, endMinute), dayTime)
	}

	/** 映射到游戏内的dayTime */
	@JvmStatic
	fun getDayTime(hour: Int, minute: Int): Int {
		when {
			hour !in 0..23 -> throw DateTimeParseException("hour bounds is [0, 23], currently is $hour", "", 0)
			minute !in 0..59 -> throw DateTimeParseException("minute bounds is [0, 59], currently is $minute", "", 0)

			else -> return TIMES[hour * 60 + minute].toInt()
		}
	}

	@JvmStatic
	fun isDay(level: Level): Boolean {
		return isDay(getDayTime(level))
	}

	@JvmStatic
	fun isDay(dayTime: Int): Boolean {
		return isWithinDayTime(`_04$30`, `_19$30`, dayTime)
	}

	@JvmStatic
	fun isNight(level: Level): Boolean {
		return isNight(getDayTime(level))
	}

	@JvmStatic
	fun isNight(dayTime: Int): Boolean {
		return isWithinDayTime(`_19$30`, `_04$30`, dayTime)
	}
}
