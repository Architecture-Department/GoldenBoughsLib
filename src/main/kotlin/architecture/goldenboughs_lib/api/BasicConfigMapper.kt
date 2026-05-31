package architecture.goldenboughs_lib.api

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair
import java.util.function.Function

abstract class BasicConfigMapper {
	protected fun <T> configure(consumer: Function<ModConfigSpec.Builder, T>): Pair<T, ModConfigSpec> {
		return ModConfigSpec.Builder().configure<T>(consumer)
	}
}