package architecture.goldenboughs_lib.events.client;

import architecture.goldenboughs_lib.client.model.ShieldSphereMesh;
import architecture.goldenboughs_lib.core.Lib;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ShieldRenderer {
	private static final ResourceLocation SHIELD_TEXTURE = Lib.modRl("textures/entity/shield.png");
	private static final ShieldSphereMesh SPHERE_MESH = new ShieldSphereMesh();

	private static final RenderType SHIELD_RENDER_TYPE = RenderType.create(
		"shield_emissive",
		DefaultVertexFormat.NEW_ENTITY,
		VertexFormat.Mode.QUADS,
		4194304,
		true,
		true,
		RenderType.CompositeState.builder()
			.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(SHIELD_TEXTURE, false, false))
			.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
			.setCullState(RenderStateShard.NO_CULL)
			.setWriteMaskState(RenderStateShard.COLOR_WRITE)
			.setOverlayState(RenderStateShard.OVERLAY)
			.createCompositeState(true)
	);

	public static void renderShieldIfPresent(LivingEntity entity,
	                                         PoseStack poseStack,
	                                         MultiBufferSource buffer,
	                                         int packedLight) {
		final var list = architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry.getAll();
		final var livingEffects = entity.getActiveEffectsMap().keySet();

		list.parallelStream()
			.filter(it -> livingEffects.contains(it.effect()))
			.findFirst()
			.ifPresent(it -> renderShield(entity, poseStack, buffer, packedLight, it));
	}

	private static void renderShield(LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry.ShieldEntry entry) {
		float radius = Math.max(entity.getBbWidth(), entity.getBbHeight()) * 1.2f;
		int color = entry.effect().value().getColor();
		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;
		float a = 0.5f;

		poseStack.pushPose();
		poseStack.translate(0, entity.getBbHeight() * 0.5, 0);
		poseStack.scale(radius, radius, radius);

		VertexConsumer consumer = buffer.getBuffer(SHIELD_RENDER_TYPE);

		SPHERE_MESH.render(consumer, poseStack.last(), r, g, b, a, packedLight);

		poseStack.popPose();
	}
}
