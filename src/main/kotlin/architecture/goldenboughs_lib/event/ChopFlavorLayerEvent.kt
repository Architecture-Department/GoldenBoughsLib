package architecture.goldenboughs_lib.event

import architecture.goldenboughs_lib.api.AllOpen
import architecture.goldenboughs_lib.client.gui.hudlayers.chop_flavor.ChopFlavorLayer
import architecture.goldenboughs_lib.util.ConditionalProviderFactory
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import org.jetbrains.annotations.ApiStatus
import java.util.function.Function
import java.util.function.Predicate

@AllOpen
class ChopFlavorLayerEvent : Event(), IModBusEvent {
	fun add(id: ResourceLocation, pair: Pair<Predicate<ItemStack>, ChopFlavorLayer.IChopFlavorBar?>) {
		LIST[id] = pair
	}

	fun add(
		id: ResourceLocation,
		predicate: Predicate<ItemStack>,
		chopFlavorLayer: ChopFlavorLayer.IChopFlavorBar?
	) {
		LIST[id] = predicate to chopFlavorLayer
	}

	fun clear() {
		LIST.clear()
	}

	@get:ApiStatus.Internal
	val chopFlavorBarProvider: Function<ItemStack, ChopFlavorLayer.IChopFlavorBar?>
		get() = ConditionalProviderFactory.getProvider(
			null,
			LIST.values.stream().toList()
		)

	companion object {
		@JvmStatic
		private val LIST: LinkedHashMap<ResourceLocation, Pair<Predicate<ItemStack>, ChopFlavorLayer.IChopFlavorBar?>> =
			LinkedHashMap()
	}
}
