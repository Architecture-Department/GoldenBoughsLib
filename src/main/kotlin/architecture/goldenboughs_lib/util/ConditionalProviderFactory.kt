package architecture.goldenboughs_lib.util

import com.mojang.datafixers.util.Pair
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
		conditions: MutableList<Pair<Predicate<I>, T?>>
	): Function<I, T?> {
		return Function { entitypatch: I ->
			if (conditions.isEmpty()) {
				return@Function defaultValue
			}
			for (condition in conditions) {
				val predicate = condition.getFirst()
				if (predicate == null || !predicate.test(entitypatch)) {
					continue
				}

				val second = condition.getSecond() ?: continue

				return@Function second as T
			}
			defaultValue
		}
	}
}
