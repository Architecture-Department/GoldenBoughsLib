package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.client.renderer.StaffCorpseRenderer
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.eventexecute.client.EntityRenderEventExecute
import architecture.goldenboughs_lib.init.LibEntityTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RenderLivingEvent
import net.neoforged.neoforge.client.event.RenderPlayerEvent

@EventBusSubscriber(modid = Lib.ID, value = [Dist.CLIENT])
object EntityRenderEvents {
	@SubscribeEvent
	fun onRenderPlayerEventPre(event: RenderPlayerEvent.Pre) {
		val entity = event.entity
		val instance = Minecraft.getInstance()
		val entityRenderDispatcher = instance.entityRenderDispatcher
		val renderer = entityRenderDispatcher.getRenderer(entity) as EntityRenderer<*> as PlayerRenderer
		EntityRenderEventExecute.hiddenParts(entity, renderer.getModel())
	}

	@SubscribeEvent
	fun renderLivingEventPost(event: RenderLivingEvent.Post<*, *>) {
		val entity = event.entity
		val poseStack = event.poseStack
		val multiBufferSource = event.multiBufferSource
		val packedLight = event.packedLight
		ShieldRenderer.renderShieldIfPresent(entity, poseStack, multiBufferSource, packedLight)
	}

	@SubscribeEvent
	fun entityRenderersRegister(event: EntityRenderersEvent.RegisterRenderers) {
		event.registerEntityRenderer(
			LibEntityTypes.STAFF_CORPSE.get(),
			::StaffCorpseRenderer
		)
	}
}
