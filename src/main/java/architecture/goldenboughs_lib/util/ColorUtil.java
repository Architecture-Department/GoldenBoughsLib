package architecture.goldenboughs_lib.util;

import net.minecraft.network.chat.TextColor;

/**
 * 颜色工具类
 * 提供颜色处理相关的实用方法
 */
public final class ColorUtil {
	/**
	 * 将RGB颜色字符串转换为整数颜色值
	 *
	 * @param color RGB颜色字符串，格式为"#RRGGBB"
	 * @return 整数颜色值
	 */
	public static int rgbColor(String color) {
		return TextColor.parseColor(color).getOrThrow().getValue();
	}

	/**
	 * 将RGBA颜色字符串转换为16位整数颜色值
	 *
	 * @param colorString RGBA颜色字符串，格式为"#RRGGBBAA"
	 * @return 16位整数颜色值
	 * @throws IllegalArgumentException 当颜色字符串格式不正确时抛出
	 */
	public static int rgbaColor(String colorString) {
		if (!colorString.startsWith("#") || colorString.length() != 9) {
			throw new IllegalArgumentException("Invalid color format, expected #RRGGBBAA");
		}

		try {
			String hex = colorString.substring(1);
			int r = Integer.parseInt(hex.substring(0, 2), 16);
			int g = Integer.parseInt(hex.substring(2, 4), 16);
			int b = Integer.parseInt(hex.substring(4, 6), 16);
			int a = Integer.parseInt(hex.substring(6, 8), 16);

			return rgbaColor(r, g, b, a);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex color format: " + colorString);
		}
	}

	public static int rgbaColor(int r, int g, int b, int a) {
		// 转换为16位格式 (RGBA -> 4444)
		int r16 = (r >> 4) & 0xF;
		int g16 = (g >> 4) & 0xF;
		int b16 = (b >> 4) & 0xF;
		int a16 = (a >> 4) & 0xF;
		return (r16 << 12) | (g16 << 8) | (b16 << 4) | a16;
	}

	public static int rgbaColor(float r, float g, float b, float a) {
		return rgbaColor(colorValue(r), colorValue(g), colorValue(b), colorValue(a));
	}

	/**
	 * 浮点颜色值转整数颜色值
	 *
	 * @param colorValue 浮点颜色值 (0.0-1.0)
	 * @return 整数颜色值 (0-255)
	 */
	public static int colorValue(float colorValue) {
		return Math.max(0, Math.min(255, (int) (colorValue * 255.0f)));
	}

	/**
	 * 整数颜色值转浮点颜色值
	 *
	 * @param colorValue 整数颜色值 (0-255)
	 * @return 浮点颜色值 (0.0-1.0)
	 */
	public static float colorValue(int colorValue) {
		return Math.max(0.0f, (Math.min(1.0f, colorValue / 255.0f)));
	}
}
