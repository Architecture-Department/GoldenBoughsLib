package architecture.goldenboughs_lib.events.registry.client

import architecture.goldenboughs_lib.util.LibUtil
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@EventBusSubscriber(modid = LibUtil.ID, value = [Dist.CLIENT])
object MenuScreensRegistry {
	@SubscribeEvent
	fun registry(event: RegisterMenuScreensEvent) {
	}
}
