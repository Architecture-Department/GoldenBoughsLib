package architecture.goldenboughs_lib.util

import net.minecraft.util.Mth
import org.joml.Vector3f
import kotlin.math.sin

/**
 * 计算基于正弦波的周期性波动值
 *
 * @param minValue        最小值
 * @param maxValue        最大值
 * @param durationSeconds 周期时长（秒）
 * @return 在 [minValue, maxValue] 范围内的周期性波动值
 */
fun calculateSineCycle(minValue: Float, maxValue: Float, durationSeconds: Float): Float {
	return calculateSineCycle(minValue, maxValue, durationSeconds, System.currentTimeMillis())
}

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
fun mapSinToRange(min: Float, max: Float, sinValue: Float): Float {
	return min + (1 + sinValue) * (max - min) / 2
}

fun lerp(delta: Vector3f, start: Vector3f, t: Float): Vector3f {
	return Vector3f(
		Mth.lerp(delta.x, start.x, t),
		Mth.lerp(delta.y, start.y, t),
		Mth.lerp(delta.z, start.z, t)
	)
}