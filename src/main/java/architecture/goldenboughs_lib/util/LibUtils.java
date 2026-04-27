package architecture.goldenboughs_lib.util;

import net.minecraft.resources.ResourceLocation;

public final class LibUtils {
	/**
	 * 计算基于正弦波的周期性波动值
	 *
	 * @param minValue        最小值
	 * @param maxValue        最大值
	 * @param durationSeconds 周期时长（秒）
	 * @return 在 [minValue, maxValue] 范围内的周期性波动值
	 */
	public static float calculateSineCycle(float minValue, float maxValue, float durationSeconds) {
		long timeMillis = System.currentTimeMillis();
		return calculateSineCycle(minValue, maxValue, durationSeconds, timeMillis);
	}

	public static float calculateSineCycle(float minValue, float maxValue, float durationSeconds, long timeMillis) {
		float normalizedTime = (float) ((timeMillis % 10000) / 10000.0 * durationSeconds);
		float sinValue = (float) Math.sin(normalizedTime * (float) Math.PI * 2);
		return mapSinToRange(minValue, maxValue, sinValue);
	}

	/**
	 * 将正弦值映射到指定范围
	 * 正弦值范围 [-1, 1] 映射到 [min, max]
	 *
	 * @param min      目标范围最小值
	 * @param max      目标范围最大值
	 * @param sinValue 正弦值（范围 -1 到 1）
	 * @return 映射后的值
	 */
	public static float mapSinToRange(float min, float max, float sinValue) {
		return min + (1 + sinValue) * (max - min) / 2;
	}

	public static ResourceLocation getTextureLight(ResourceLocation path) {
		return getTextureLight(path.toString());
	}

	public static ResourceLocation getTextureLight(String path) {
		return ResourceLocation.parse(path.substring(0, path.lastIndexOf(".png")) + "_glowmask.png");
	}
}
