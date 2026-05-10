package architecture.goldenboughs_lib.util

import kotlin.math.abs
import kotlin.math.floor

object TextUtil {
	/**
	 * 格式化数字，添加千位分隔符
	 *
	 * @param number 要格式化的数字
	 * @return 格式化后的字符串
	 */
	@JvmOverloads
	@JvmStatic
	fun formatNumberPlaces(number: Double, decimalPlaces: Int = 2): String {
		// 先格式化小数位数
		val formatted = String.format("%." + decimalPlaces + "f", number)

		// 分离整数部分和小数部分
		val dotIndex: Int = formatted.indexOf('.')
		val integerPart = if (dotIndex >= 0) formatted.substring(0, dotIndex) else formatted
		val decimalPart = if (dotIndex >= 0) formatted.substring(dotIndex) else ""

		// 添加千位分隔符
		return getResult(integerPart, decimalPart).toString()
	}

	@JvmStatic
	private fun getResult(integerPart: String, decimalPart: String): StringBuilder {
		val result = StringBuilder()
		val len: Int = integerPart.length
		val isNegative = integerPart.startsWith("-")
		val startIndex = if (isNegative) 1 else 0

		// 添加符号
		if (isNegative) {
			result.append('-')
		}

		// 处理整数部分，每三位添加一个逗号
		for (i in startIndex..<len) {
			// 如果不是第一位且从右往左数是3的倍数，则添加逗号
			if (i > startIndex && (len - i) % 3 == 0) {
				result.append(',')
			}
			result.append(integerPart[i])
		}

		// 添加小数部分
		result.append(decimalPart)
		return result
	}

	/**
	 * 将数字转换为带单位的文本
	 *
	 * @param value 数字
	 * @return 转换后的文本
	 */
	@JvmStatic
	fun getDigitalText(value: Long): String {
		val valueTextLength: Int = abs(value).toString().length
		if (valueTextLength >= 12) {
			return formatNumber(value / 1000000000000.0, "T")
		} else if (valueTextLength >= 10) {
			return formatNumber(value / 1000000000.0, "G")
		} else if (valueTextLength >= 7) {
			return formatNumber(value / 1000000.0, "M")
		} else if (valueTextLength >= 4) {
			return formatNumber(value / 1000.0, "K")
		}
		return value.toString()
	}

	/**
	 * 格式化数字并添加单位
	 *
	 * @param value 需要格式化的数值
	 * @param unit  要添加的单位字符串
	 * @return 格式化后的带单位字符串
	 */
	@JvmStatic
	fun formatNumber(value: Double, unit: String?): String {
		return "${formatNumber(value, 2)} $unit"
	}

	/**
	 * 格式化数字
	 *
	 * @param value         需要格式化的数值
	 * @param decimalLength 小数位数
	 * @return 格式化后的字符串
	 */
	@JvmStatic
	fun formatNumber(value: Double, decimalLength: Int): String {
		var decimalLength = decimalLength
		if (decimalLength < 0) {
			decimalLength = 0
		}

		val absValue = abs(value)
		val fractionalPart = absValue - floor(absValue)
		if (decimalLength == 0 || fractionalPart == 0.0 || (absValue > 1 && fractionalPart < 1e-10)) {
			return String.format("%.0f", value)
		}
		return String.format("%." + decimalLength + "f", value).replace("\\.?0+$", "")
	}

	/**
	 * 分割数字
	 *
	 * @param value  数字
	 * @param length 分割长度
	 * @param symbol 分割符号
	 * @return 分割后的数字
	 */
	@JvmStatic
	fun divideDigital(value: Long, length: Int, symbol: String?): String {
		val text = StringBuilder(value.toString())
		val textLength: Int = text.length
		var i = textLength - length
		while (i > 0) {
			text.insert(i, symbol)
			i -= length
		}
		return text.toString()
	}

	/**
	 * 将游戏刻度转换为时间格式
	 * 每20刻度等于1秒
	 *
	 * @param ticks 游戏刻度数
	 * @return 格式化后的时间字符串
	 */
	@JvmStatic
	fun formatGameTime(ticks: Long): String {
		val seconds = ticks / 20
		val minutes = seconds / 60
		val hours = minutes / 60

		return when {
			hours > 0 -> String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60)
			minutes > 0 -> String.format("%dm %ds", minutes, seconds % 60)

			else -> String.format("%ds", seconds)
		}
	}
}
