package architecture.goldenboughs_lib.util;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 条件化提供者工厂
 * <p>创建根据实体条件动态返回值的提供者函数</p>
 */
public final class ConditionalProviderFactory {
	public static <T, I> Function<I, T> getProvider(T defaultValue, @Nullable List<Pair<Predicate<I>, T>> conditions) {
		return entitypatch -> {
			if (conditions == null || conditions.isEmpty()) {
				return defaultValue;
			}

			for (Pair<Predicate<I>, T> condition : conditions) {
				Predicate<I> predicate = condition.getFirst();
				if (predicate == null || !predicate.test(entitypatch)) {
					continue;
				}

				var second = condition.getSecond();
				if (second == null) {
					continue;
				}

				return (T) second;
			}

			return defaultValue;
		};
	}
}
