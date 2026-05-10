package architecture.goldenboughs_lib.util

import net.minecraft.resources.ResourceLocation
import kotlin.math.sin

object LibUtils {
	/**
	 * 计算基于正弦波的周期性波动值
	 *
	 * @param minValue        最小值
	 * @param maxValue        最大值
	 * @param durationSeconds 周期时长（秒）
	 * @return 在 [minValue, maxValue] 范围内的周期性波动值
	 */
	@JvmStatic
	fun calculateSineCycle(minValue: Float, maxValue: Float, durationSeconds: Float): Float {
		return calculateSineCycle(minValue, maxValue, durationSeconds, System.currentTimeMillis())
	}

	@JvmStatic
	fun calculateSineCycle(minValue: Float, maxValue: Float, durationSeconds: Float, timeMillis: Long): Float {
		val normalizedTime = ((timeMillis % 10000) / 10000.0 * durationSeconds).toFloat()
		val sinValue = sin((normalizedTime * Math.PI.toFloat() * 2).toDouble()).toFloat()
		return mapSinToRange(minValue, maxValue, sinValue)
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
	@JvmStatic
	fun mapSinToRange(min: Float, max: Float, sinValue: Float): Float {
		return min + (1 + sinValue) * (max - min) / 2
	}

	@JvmStatic
	fun getTextureLight(path: ResourceLocation): ResourceLocation {
		return getTextureLight(path.toString())
	}

	@JvmStatic
	fun getTextureLight(path: String): ResourceLocation {
		return ResourceLocation.parse(path.substring(0, path.lastIndexOf(".png")) + "_glowmask.png")
	}
}
