package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.config.LibConfig
import architecture.goldenboughs_lib.init.*
import architecture.goldenboughs_lib.util.LibUtil.rlOf
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.Contract
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(Lib.ID)
@EventBusSubscriber
object Lib {
	const val ID: String = "goldenboughs_lib"
	const val NAME: String = "GoldenBoughsLib"

	@JvmField
	val LOGGER: Logger = LogManager.getLogger(ID)

	init {
		val modContainer = LOADING_CONTEXT.activeContainer
		val modBus = MOD_BUS

		LibConfig.register(modContainer)
		LibParticleTypes.REGISTRY.register(modBus)
		LibAttachmentTypes.REGISTRY.register(modBus)
		LibMobEffects.REGISTRY.register(modBus)
		LibSoundEvents.REGISTRY.register(modBus)
		LibDataComponentTypes.REGISTRY.register(modBus)
		LibAttributes.REGISTRY.register(modBus)
		LibItems.REGISTRY.register(modBus)
		LibEntityTypes.REGISTRY.register(modBus)
		LibAbsorptionShieldsRegistry.register()
	}

	@SubscribeEvent
	fun onServerStarting(event: ServerStartingEvent) {
		LOGGER.info("HELLO from server starting")
	}

	@JvmStatic
	@Contract("_ -> new")
	fun modRl(name: String): ResourceLocation {
		return rlOf(ID, name)
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
