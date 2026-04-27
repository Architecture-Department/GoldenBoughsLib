package architecture.goldenboughs_lib.core;

import architecture.goldenboughs_lib.config.LibConfig;
import architecture.goldenboughs_lib.init.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Mod(GoldenBoughsLib.ID)
public final class GoldenBoughsLib {

	public static final String ID = "goldenboughs_lib";
	public static final String NAME = "GoldenBoughsLib";
	public static final Logger LOGGER = LogManager.getLogger(ID);

	public GoldenBoughsLib(IEventBus eventBus, ModContainer container) {
		NeoForge.EVENT_BUS.register(this);
		LibConfig.init(container);
		LibItems.REGISTRY.register(eventBus);
		LibMobEffects.REGISTRY.register(eventBus);
		LibAttributes.REGISTRY.register(eventBus);
		LibAttachments.REGISTRY.register(eventBus);
		LibParticleTypes.REGISTRY.register(eventBus);
		LibDataComponents.REGISTRY.register(eventBus);
		LibAbsorptionShieldsRegistry.init();
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation modRl(final String name) {
		return ResourceLocation.fromNamespaceAndPath(ID, name);
	}

	@Contract(pure = true)
	public static @NotNull String modRlText(final String name) {
		return ID + ":" + name;
	}

	public static <T> @NotNull DeferredRegister<T> modRegister(Registry<T> registry) {
		return DeferredRegister.create(registry, ID);
	}

	public static <T> @NotNull DeferredRegister<T> modRegister(ResourceKey<Registry<T>> registry) {
		return DeferredRegister.create(registry, ID);
	}
}
