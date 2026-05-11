package architecture.goldenboughs_lib.core.registry.client

import architecture.goldenboughs_lib.core.GoldenBoughsLib
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@EventBusSubscriber(modid = GoldenBoughsLib.ID, value = [Dist.CLIENT])
object MenuScreensRegistry {
	@SubscribeEvent
	@JvmStatic
	fun registry(event: RegisterMenuScreensEvent) {
	}
}
