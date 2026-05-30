package architecture.goldenboughs_lib.core.registry.client

import architecture.goldenboughs_lib.core.LibConstants
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@EventBusSubscriber(modid = LibConstants.ID, value = [Dist.CLIENT])
object MenuScreensRegistry {
	@SubscribeEvent
	fun registry(event: RegisterMenuScreensEvent) {
	}
}
