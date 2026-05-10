package architecture.goldenboughs_lib.util

import net.minecraft.world.phys.Vec3
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object EntityUtil {
	@JvmStatic
	fun angleBetween(v1: Vec3, v2: Vec3): Double {
		return acos(v1.dot(v2) / v1.length() / v2.length())
	}

	/**
	 * 根据权重随机获取物品
	 */
	@JvmStatic
	fun <T> getRandomByWeightInt(map: MutableMap<T?, Int>): T? {
		// 计算总权重
		var totalWeight = 0.0f

		for (pair in map.values) {
			totalWeight += pair.toFloat()
		}

		require(totalWeight != 0.0f) { "Total weight cannot be zero." }

		val randomValue = ThreadLocalRandom.current().nextFloat(0f, totalWeight)

		// 遍历物品，累积权重，直到累积权重超过随机数
		var cumulativeWeight = 0.0f
		for (entry in map.entries) {
			cumulativeWeight += entry.value.toFloat()
			if (cumulativeWeight >= randomValue) {
				return entry.key
			}
		}
		// 理论上不会走到这里
		throw IllegalStateException("Failed to find random item.")
	}

	@JvmStatic
	fun <T> getRandomByWeightInt(items: MutableList<T>, weights: MutableList<Int>): T? {
		require(!(items.size != weights.size || items.isEmpty())) { "Items and weights must be non-null, non-empty, and of the same size." }

		// 计算总权重
		var totalWeight = 0.0f
		for (weight in weights) {
			totalWeight += weight.toFloat()
		}

		require(totalWeight != 0.0f) { "Total weight cannot be zero." }

		val randomValue = ThreadLocalRandom.current().nextFloat(0f, totalWeight)

		// 遍历物品，累积权重，直到累积权重超过随机数
		var cumulativeWeight = 0.0f
		for (i in items.indices) {
			cumulativeWeight += weights[i].toFloat()
			if (cumulativeWeight >= randomValue) {
				return items[i]
			}
		}
		// 理论上不会走到这里
		throw IllegalStateException("Failed to find random item.")
	}

	/**
	 * 球坐标
	 *
	 * @param r     半径
	 * @param theta yaw
	 * @param beta  pitch - 90°
	 * @return 方向向量
	 */
	@JvmStatic
	fun sphere(r: Float, theta: Float, beta: Float): Vec3 {
		val x = r * sin(beta.toDouble()) * cos(theta.toDouble())
		val y = r * cos(beta.toDouble())
		val z = r * sin(beta.toDouble()) * sin(theta.toDouble())
		return Vec3(x, y, z)
	}
}
