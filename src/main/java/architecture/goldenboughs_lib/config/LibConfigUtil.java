package architecture.goldenboughs_lib.config;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class LibConfigUtil {
	protected static ModConfigSpec.@NotNull DoubleValue define(
		ModConfigSpec.@NotNull Builder builder,
		double defaultValue, double min, double max,
		String key,
		String... comment
	) {
		return builder.comment(comment)
			.translation(getTranslation(key))
			.defineInRange(key, defaultValue, min, max);
	}

	public static @NotNull String getTranslation(String @NotNull ... keys) {
		if (keys.length == 0) {
			return GoldenBoughsLib.ID + ".config";
		}
		StringBuilder builder = new StringBuilder();
		for (String key : keys) {
			builder.append(".");
			builder.append(key);
		}
		return GoldenBoughsLib.ID + ".config" + builder;
	}

	protected static ModConfigSpec.@NotNull IntValue define(
		ModConfigSpec.@NotNull Builder builder,
		int defaultValue, int min, int max,
		String key,
		String... comment
	) {
		return builder.comment(comment)
			.translation(getTranslation(key))
			.defineInRange(key, defaultValue, min, max);
	}

	protected static ModConfigSpec.@NotNull BooleanValue define(
		ModConfigSpec.@NotNull Builder builder,
		boolean defaultValue,
		String key,
		String... comment
	) {
		return builder.comment(comment)
			.translation(getTranslation(key))
			.define(key, defaultValue);
	}

	protected static <T> @NotNull Pair<T, ModConfigSpec> configure(Function<ModConfigSpec.Builder, T> consumer) {
		return new ModConfigSpec.Builder().configure(consumer);
	}
}
