package architecture.goldenboughs_lib.events.client;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.eventexecute.client.EntityRenderEventExecute;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = GoldenBoughsLib.ID, value = Dist.CLIENT)
public final class EntityRenderEvents {
	@SubscribeEvent
	public static void onRenderPlayerEventPre(RenderPlayerEvent.Pre event) {
		Player entity = event.getEntity();
		Minecraft instance = Minecraft.getInstance();
		EntityRenderDispatcher entityRenderDispatcher = instance.getEntityRenderDispatcher();
		PlayerRenderer renderer = (PlayerRenderer) (EntityRenderer<?>) entityRenderDispatcher.getRenderer(entity);
		EntityRenderEventExecute.INSTANCE.hiddenParts(entity, renderer.getModel());
	}

	@SubscribeEvent
	public static void renderLivingEventPost(RenderLivingEvent.Post<?, ?> event) {
		LivingEntity entity = event.getEntity();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource multiBufferSource = event.getMultiBufferSource();
		int packedLight = event.getPackedLight();
		ShieldRenderer.renderShieldIfPresent(entity, poseStack, multiBufferSource, packedLight);
	}
}
