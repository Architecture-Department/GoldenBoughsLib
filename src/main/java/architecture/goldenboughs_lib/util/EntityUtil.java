package architecture.goldenboughs_lib.util;

import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class EntityUtil {
	public static double angleBetween(Vec3 v1, Vec3 v2) {
		return Math.acos(v1.dot(v2) / v1.length() / v2.length());
	}

	/**
	 * 根据权重随机获取物品
	 */
	public static <T> T getRandomByWeightInt(Map<T, Integer> map) {
		// 计算总权重
		float totalWeight = 0.0f;

		for (var pair : map.values()) {
			totalWeight += pair;
		}

		if (totalWeight == 0.0f) {
			throw new IllegalArgumentException("Total weight cannot be zero.");
		}

		float randomValue = ThreadLocalRandom.current().nextFloat(0, totalWeight);

		// 遍历物品，累积权重，直到累积权重超过随机数
		float cumulativeWeight = 0.0f;
		for (var entry : map.entrySet()) {
			cumulativeWeight += entry.getValue();
			if (cumulativeWeight >= randomValue) {
				return entry.getKey();
			}
		}
		// 理论上不会走到这里
		throw new IllegalStateException("Failed to find random item.");
	}

	public static <T> T getRandomByWeightInt(List<T> items, List<Integer> weights) {
		if (items == null || weights == null || items.size() != weights.size() || items.isEmpty()) {
			throw new IllegalArgumentException("Items and weights must be non-null, non-empty, and of the same size.");
		}

		// 计算总权重
		float totalWeight = 0.0f;
		for (var weight : weights) {
			totalWeight += weight;
		}

		if (totalWeight == 0.0f) {
			throw new IllegalArgumentException("Total weight cannot be zero.");
		}

		float randomValue = ThreadLocalRandom.current().nextFloat(0, totalWeight);

		// 遍历物品，累积权重，直到累积权重超过随机数
		float cumulativeWeight = 0.0f;
		for (int i = 0; i < items.size(); i++) {
			cumulativeWeight += weights.get(i);
			if (cumulativeWeight >= randomValue) {
				return items.get(i);
			}
		}
		// 理论上不会走到这里
		throw new IllegalStateException("Failed to find random item.");
	}

	/**
	 * 球坐标
	 *
	 * @param r     半径
	 * @param theta yaw
	 * @param beta  pitch - 90°
	 * @return 方向向量
	 */
	public static Vec3 sphere(float r, float theta, float beta) {
		double x = r * Math.sin(beta) * Math.cos(theta);
		double y = r * Math.cos(beta);
		double z = r * Math.sin(beta) * Math.sin(theta);
		return new Vec3(x, y, z);
	}
}
