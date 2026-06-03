package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.core.LibConstants
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderLevelStageEvent

@EventBusSubscriber(modid = LibConstants.ID, value = [Dist.CLIENT])
object GameRenderEvents {
	@SubscribeEvent
	fun onLevelRender(event: RenderLevelStageEvent) {
		val stage = event.stage
		val minecraft = Minecraft.getInstance()
		val level = minecraft.level
		val frustum = event.frustum
		val pose = event.poseStack
		val camera = event.camera
		val partialTick = event.partialTick
	}
}
