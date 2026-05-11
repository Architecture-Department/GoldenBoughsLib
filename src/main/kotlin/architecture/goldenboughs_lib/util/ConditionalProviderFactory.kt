package architecture.goldenboughs_lib.util

import java.util.function.Function
import java.util.function.Predicate

/**
 * 条件化提供者工厂
 * 
 * 创建根据实体条件动态返回值的提供者函数
 */
object ConditionalProviderFactory {
	@JvmStatic
	fun <T, I> getProvider(
		defaultValue: T?,
		conditions: List<Pair<Predicate<I>, T?>>
	): Function<I, T?> {
		return Function { entityPatch: I ->
			if (conditions.isEmpty()) {
				return@Function defaultValue
			}
			for (condition in conditions) {
				val predicate = condition.first
				if (!predicate.test(entityPatch)) {
					continue
				}

				val second = condition.second ?: continue

				return@Function second as T
			}
			defaultValue
		}
	}
}
