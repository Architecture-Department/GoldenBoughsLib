package architecture.goldenboughs_lib.core

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory

@Mod(value = GoldenBoughsLib.ID, dist = [Dist.CLIENT])
@EventBusSubscriber(modid = GoldenBoughsLib.ID, value = [Dist.CLIENT])
class GoldenBoughsLibClient(container: ModContainer) {
	init {
		container.registerExtensionPoint(
			IConfigScreenFactory::class.java,
			IConfigScreenFactory { mod: ModContainer, parent: Screen -> ConfigurationScreen(mod, parent) })
	}

	companion object {
		@SubscribeEvent
		@JvmStatic
		fun onClientSetup(event: FMLClientSetupEvent) {
			GoldenBoughsLib.LOGGER.info("HELLO FROM CLIENT SETUP")
			GoldenBoughsLib.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().user.name)
		}
	}
}
