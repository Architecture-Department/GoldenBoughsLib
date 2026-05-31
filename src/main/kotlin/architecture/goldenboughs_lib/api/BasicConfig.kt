package architecture.goldenboughs_lib.api

import architecture.goldenboughs_lib.datagen.i18n.DatagenI18n
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair
import java.util.function.Function

abstract class BasicConfig(val modId: String, val builder: ModConfigSpec.Builder) {
	protected fun define(
		defaultValue: Double,
		min: Double,
		max: Double,
		key: String,
		vararg comment: String
	): ModConfigSpec.DoubleValue {
		return builder.comment(*comment)
			.translation(DatagenI18n.getConfigTranslation(modId, key))
			.defineInRange(key, defaultValue, min, max)
	}

	protected fun define(
		defaultValue: Int,
		min: Int,
		max: Int,
		key: String,
		vararg comment: String
	): ModConfigSpec.IntValue {
		return builder.comment(*comment)
			.translation(DatagenI18n.getConfigTranslation(modId, key))
			.defineInRange(key, defaultValue, min, max)
	}

	protected fun define(
		defaultValue: Boolean,
		key: String,
		vararg comment: String
	): ModConfigSpec.BooleanValue {
		return builder.comment(*comment)
			.translation(DatagenI18n.getConfigTranslation(modId, key))
			.define(key, defaultValue)
	}

	protected fun <T> configure(consumer: Function<ModConfigSpec.Builder, T>): Pair<T, ModConfigSpec> {
		return ModConfigSpec.Builder().configure<T>(consumer)
	}
}