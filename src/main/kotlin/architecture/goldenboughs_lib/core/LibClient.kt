package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = LibUtil.ID, dist = [Dist.CLIENT])
@EventBusSubscriber(modid = LibUtil.ID, value = [Dist.CLIENT])
object LibClient {
	init {
		val modContainer = LOADING_CONTEXT.activeContainer
		val modBus = MOD_BUS

		modContainer.registerExtensionPoint(
			IConfigScreenFactory::class.java,
			IConfigScreenFactory(::ConfigurationScreen)
		)
	}

	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {
		LibUtil.LOGGER.info("HELLO FROM CLIENT SETUP")
		LibUtil.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().user.name)
	}
}
