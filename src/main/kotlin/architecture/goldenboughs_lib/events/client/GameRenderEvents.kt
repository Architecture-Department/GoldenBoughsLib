package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.core.Lib
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderLevelStageEvent

@EventBusSubscriber(modid = Lib.ID, value = [Dist.CLIENT])
object GameRenderEvents {
	@SubscribeEvent
	fun levelRender(event: RenderLevelStageEvent) {
		val stage = event.stage
		val minecraft = Minecraft.getInstance()
		val level = minecraft.level
		val frustum = event.frustum
		val pose = event.poseStack
		val camera = event.camera
		val partialTick = event.partialTick
	}
}
