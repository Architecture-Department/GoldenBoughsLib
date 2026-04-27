package architecture.goldenboughs_lib.util;

import org.jetbrains.annotations.NotNull;

public final class TextUtil {

	/**
	 * 格式化数字，添加千位分隔符
	 *
	 * @param number 要格式化的数字
	 * @return 格式化后的字符串
	 */
	public static String formatNumberPlaces(double number) {
		return formatNumberPlaces(number, 2);
	}

	/**
	 * 格式化数字，添加千位分隔符
	 *
	 * @param number        要格式化的数字
	 * @param decimalPlaces 小数位数
	 * @return 格式化后的字符串
	 */
	public static String formatNumberPlaces(double number, int decimalPlaces) {
		// 先格式化小数位数
		String formatted = String.format("%." + decimalPlaces + "f", number);

		// 分离整数部分和小数部分
		int dotIndex = formatted.indexOf('.');
		String integerPart = dotIndex >= 0 ? formatted.substring(0, dotIndex) : formatted;
		String decimalPart = dotIndex >= 0 ? formatted.substring(dotIndex) : "";

		// 添加千位分隔符
		return getResult(integerPart, decimalPart).toString();
	}

	private static @NotNull StringBuilder getResult(final String integerPart, final String decimalPart) {
		StringBuilder result = new StringBuilder();
		int len = integerPart.length();
		boolean isNegative = integerPart.startsWith("-");
		int startIndex = isNegative ? 1 : 0;

		// 添加符号
		if (isNegative) {
			result.append('-');
		}

		// 处理整数部分，每三位添加一个逗号
		for (int i = startIndex; i < len; i++) {
			// 如果不是第一位且从右往左数是3的倍数，则添加逗号
			if (i > startIndex && (len - i) % 3 == 0) {
				result.append(',');
			}
			result.append(integerPart.charAt(i));
		}

		// 添加小数部分
		result.append(decimalPart);
		return result;
	}

	/**
	 * 将数字转换为带单位的文本
	 *
	 * @param value 数字
	 * @return 转换后的文本
	 */
	public static String getDigitalText(long value) {
		int valueTextLength = String.valueOf(Math.abs(value)).length();
		if (valueTextLength >= 12) {
			return formatNumber(value / 1000000000000.0, "T");
		} else if (valueTextLength >= 10) {
			return formatNumber(value / 1000000000.0, "G");
		} else if (valueTextLength >= 7) {
			return formatNumber(value / 1000000.0, "M");
		} else if (valueTextLength >= 4) {
			return formatNumber(value / 1000.0, "K");
		}
		return String.valueOf(value);
	}

	/**
	 * 格式化数字并添加单位
	 *
	 * @param value 需要格式化的数值
	 * @param unit  要添加的单位字符串
	 * @return 格式化后的带单位字符串
	 */
	public static String formatNumber(double value, String unit) {
		return formatNumber(value, 2) + " " + unit;
	}

	/**
	 * 格式化数字
	 *
	 * @param value         需要格式化的数值
	 * @param decimalLength 小数位数
	 * @return 格式化后的字符串
	 */
	public static String formatNumber(double value, int decimalLength) {
		if (decimalLength < 0) {
			decimalLength = 0;
		}

		double absValue = Math.abs(value);
		double fractionalPart = absValue - Math.floor(absValue);
		if (decimalLength == 0 || fractionalPart == 0 || (absValue > 1 && fractionalPart < 1e-10)) {
			return String.format("%.0f", value);
		}
		return String.format("%." + decimalLength + "f", value).replaceAll("\\.?0+$", "");
	}

	/**
	 * 分割数字
	 *
	 * @param value  数字
	 * @param length 分割长度
	 * @param symbol 分割符号
	 * @return 分割后的数字
	 */
	public static String divideDigital(long value, int length, String symbol) {
		final var text = new StringBuilder(String.valueOf(value));
		final int textLength = text.length();
		for (int i = textLength - length; i > 0; i -= length) {
			text.insert(i, symbol);
		}
		return text.toString();
	}

	/**
	 * 将游戏刻度转换为时间格式
	 * 每20刻度等于1秒
	 *
	 * @param ticks 游戏刻度数
	 * @return 格式化后的时间字符串
	 */
	public static String formatGameTime(long ticks) {
		long seconds = ticks / 20;
		long minutes = seconds / 60;
		long hours = minutes / 60;

		if (hours > 0) {
			return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
		} else if (minutes > 0) {
			return String.format("%dm %ds", minutes, seconds % 60);
		} else {
			return String.format("%ds", seconds);
		}
	}
}
