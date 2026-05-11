package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.config.LibConfig.init
import architecture.goldenboughs_lib.init.*
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.Contract

@Mod(GoldenBoughsLib.ID)
class GoldenBoughsLib(eventBus: IEventBus, container: ModContainer) {
	init {
		NeoForge.EVENT_BUS.register(this)
		init(container)
		LibItems.REGISTRY.register(eventBus)
		LibMobEffects.REGISTRY.register(eventBus)
		LibAttributes.REGISTRY.register(eventBus)
		LibAttachments.REGISTRY.register(eventBus)
		LibParticleTypes.REGISTRY.register(eventBus)
		LibDataComponentTypes.REGISTRY.register(eventBus)
		LibEntityTypes.REGISTRY.register(eventBus)
		LibAbsorptionShieldsRegistry.init()
	}

	@SubscribeEvent
	fun onServerStarting(event: ServerStartingEvent) {
		LOGGER.info("HELLO from server starting")
	}

	companion object {
		const val ID: String = "goldenboughs_lib"
		const val NAME: String = "GoldenBoughsLib"

		@JvmField
		val LOGGER: Logger = LogManager.getLogger(ID)

		@JvmStatic
		@Contract("_ -> new")
		fun modRl(name: String): ResourceLocation {
			return ResourceLocation.fromNamespaceAndPath(ID, name)
		}

		@JvmStatic
		@Contract(pure = true)
		fun modRlText(name: String): String {
			return "$ID:$name"
		}

		@JvmStatic
		fun <T> modRegister(registry: Registry<T>): DeferredRegister<T> {
			return DeferredRegister.create<T>(registry, ID)
		}

		@JvmStatic
		fun <T> modRegister(registry: ResourceKey<Registry<T>>): DeferredRegister<T> {
			return DeferredRegister.create<T>(registry, ID)
		}
	}
}
