package architecture.goldenboughs_lib.util

import net.minecraft.network.chat.TextColor
import kotlin.math.max
import kotlin.math.min

/**
 * 颜色工具类
 * 提供颜色处理相关的实用方法
 */
object ColorUtil {
	/**
	 * 将RGB颜色字符串转换为整数颜色值
	 *
	 * @param color RGB颜色字符串，格式为"#RRGGBB"
	 * @return 整数颜色值
	 */
	@JvmStatic
	fun rgbColor(color: String): Int {
		return TextColor.parseColor(color).getOrThrow().value
	}

	/**
	 * 将RGBA颜色字符串转换为16位整数颜色值
	 *
	 * @param colorString RGBA颜色字符串，格式为"#RRGGBBAA"
	 * @return 16位整数颜色值
	 * @throws IllegalArgumentException 当颜色字符串格式不正确时抛出
	 */
	@JvmStatic
	fun rgbaColor(colorString: String): Int {
		require(!(!colorString.startsWith("#") || colorString.length != 9)) { "Invalid color format, expected #RRGGBBAA" }

		try {
			val hex = colorString.substring(1)
			val r = Integer.parseInt(hex.substring(0, 2), 16)
			val g = Integer.parseInt(hex.substring(2, 4), 16)
			val b = Integer.parseInt(hex.substring(4, 6), 16)
			val a = Integer.parseInt(hex.substring(6, 8), 16)

			return rgbaColor(r, g, b, a)
		} catch (e: NumberFormatException) {
			throw IllegalArgumentException("Invalid hex color format: $colorString")
		}
	}

	@JvmStatic
	fun rgbaColor(r: Int, g: Int, b: Int, a: Int): Int {
		// 转换为16位格式 (RGBA -> 4444)
		val r16 = (r shr 4) and 0xF
		val g16 = (g shr 4) and 0xF
		val b16 = (b shr 4) and 0xF
		val a16 = (a shr 4) and 0xF
		return (r16 shl 12) or (g16 shl 8) or (b16 shl 4) or a16
	}

	@JvmStatic
	fun rgbaColor(r: Float, g: Float, b: Float, a: Float): Int {
		return rgbaColor(colorValue(r), colorValue(g), colorValue(b), colorValue(a))
	}

	/**
	 * 浮点颜色值转整数颜色值
	 *
	 * @param colorValue 浮点颜色值 (0.0-1.0)
	 * @return 整数颜色值 (0-255)
	 */
	@JvmStatic
	fun colorValue(colorValue: Float): Int {
		return max(0, min(255, (colorValue * 255.0f).toInt()))
	}

	/**
	 * 整数颜色值转浮点颜色值
	 *
	 * @param colorValue 整数颜色值 (0-255)
	 * @return 浮点颜色值 (0.0-1.0)
	 */
	@JvmStatic
	fun colorValue(colorValue: Int): Float {
		return max(0.0f, (min(1.0f, colorValue / 255.0f)))
	}
}
