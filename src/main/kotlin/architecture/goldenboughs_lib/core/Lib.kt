package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.init.*
import architecture.goldenboughs_lib.util.LibUtil
import architecture.goldenboughs_lib.util.LibUtil.LOGGER
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.event.server.ServerStartingEvent
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(LibUtil.ID)
@EventBusSubscriber
object Lib {
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
}
