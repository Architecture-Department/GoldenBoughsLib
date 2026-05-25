package architecture.goldenboughs_lib.config

import architecture.goldenboughs_lib.core.Lib
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair
import java.util.function.Function
import architecture.goldenboughs_lib.core.LibConstants

abstract class LibConfigUtil {
	companion object {
		@JvmStatic
		protected fun ModConfigSpec.Builder.define(
			defaultValue: Double,
			min: Double,
			max: Double,
			key: String,
			vararg comment: String
		): ModConfigSpec.DoubleValue {
			return comment(*comment)
				.translation(getTranslation(key))
				.defineInRange(key, defaultValue, min, max)
		}

		@JvmStatic
		fun getTranslation(vararg keys: String): String {
			if (keys.isEmpty()) {
				return LibConstants.ID + ".config"
			}
			val builder = StringBuilder()
			for (key in keys) {
				builder.append(".")
				builder.append(key)
			}
			return LibConstants.ID + ".config" + builder
		}

		protected fun ModConfigSpec.Builder.define(
			defaultValue: Int,
			min: Int,
			max: Int,
			key: String,
			vararg comment: String
		): ModConfigSpec.IntValue {
			return comment(*comment)
				.translation(getTranslation(key))
				.defineInRange(key, defaultValue, min, max)
		}

		@JvmStatic
		protected fun ModConfigSpec.Builder.define(
			defaultValue: Boolean,
			key: String,
			vararg comment: String
		): ModConfigSpec.BooleanValue {
			return comment(*comment)
				.translation(getTranslation(key))
				.define(key, defaultValue)
		}

		@JvmStatic
		protected fun <T> configure(consumer: Function<ModConfigSpec.Builder, T>): Pair<T, ModConfigSpec> {
			return ModConfigSpec.Builder().configure<T>(consumer)
		}
	}
}
