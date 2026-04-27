package architecture.goldenboughs_lib.event;

import architecture.goldenboughs_lib.client.gui.hudlayers.chop_flavor.ChopFlavorLayer;
import architecture.goldenboughs_lib.util.ConditionalProviderFactory;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChopFlavorLayerEvent extends Event implements IModBusEvent {
	private static final LinkedHashMap<ResourceLocation, Pair<Predicate<ItemStack>, ChopFlavorLayer.IChopFlavorBar>> LIST = new LinkedHashMap<>();

	public void add(ResourceLocation id, Pair<Predicate<ItemStack>, ChopFlavorLayer.IChopFlavorBar> pair) {
		LIST.put(id, pair);
	}

	public void add(ResourceLocation id, Predicate<ItemStack> predicate, ChopFlavorLayer.IChopFlavorBar chopFlavorLayer) {
		LIST.put(id, Pair.of(predicate, chopFlavorLayer));
	}

	public void clear() {
		LIST.clear();
	}

	@ApiStatus.Internal
	public Function<ItemStack, ChopFlavorLayer.IChopFlavorBar> getChopFlavorBarProvider() {
		return ConditionalProviderFactory.getProvider(null, LIST.values().stream().toList());
	}
}
