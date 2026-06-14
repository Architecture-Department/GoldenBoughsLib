package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ModelEvent

@EventBusSubscriber(modid = LibUtil.ID, value = [Dist.CLIENT])
object ModEvents {
	@SubscribeEvent
	fun onRegisterAdditional(event: ModelEvent.RegisterAdditional) {
		BuiltInRegistries.ITEM.forEach {
			val key = BuiltInRegistries.ITEM.getKey(it)
			val optional =
				Minecraft.getInstance().resourceManager.getResource(key.withPrefix("models/gui/").withSuffix(".json"))
			if (optional.isPresent) {
				event.register(ModelResourceLocation.standalone(key.withPrefix("gui/")))
			}
		}
	}
}