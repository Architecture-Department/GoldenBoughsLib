package architecture.goldenboughs_lib.util

import net.minecraft.data.DataProvider
import net.neoforged.neoforge.data.event.GatherDataEvent

fun <T : DataProvider> GatherDataEvent.buildClient(provider: T): T {
	return generator.addProvider(includeClient(), provider)
}

fun <T : DataProvider> GatherDataEvent.buildClient(vararg providers: T) {
	providers.forEach { buildClient(it) }
}

fun <T : DataProvider> GatherDataEvent.buildServer(provider: T): T {
	return generator.addProvider(includeServer(), provider)
}

fun <T : DataProvider> GatherDataEvent.buildServer(vararg providers: T) {
	providers.forEach { buildServer(it) }
}