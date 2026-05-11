@file:Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")

package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.client.model.ShieldSphereMesh
import architecture.goldenboughs_lib.core.Lib.modRl
import architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.entity.LivingEntity
import kotlin.math.max

object ShieldRenderer {
	private val SHIELD_TEXTURE = modRl("textures/entity/shield.png")
	private val SPHERE_MESH = ShieldSphereMesh()

	private val SHIELD_RENDER_TYPE: RenderType = RenderType.create(
		"shield_emissive",
		DefaultVertexFormat.NEW_ENTITY,
		VertexFormat.Mode.QUADS,
		4194304,
		true,
		true,
		RenderType.CompositeState.builder()
			.setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
			.setTextureState(RenderStateShard.TextureStateShard(SHIELD_TEXTURE, false, false))
			.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
			.setCullState(RenderStateShard.NO_CULL)
			.setWriteMaskState(RenderStateShard.COLOR_WRITE)
			.setOverlayState(RenderStateShard.OVERLAY)
			.createCompositeState(true)
	)

	fun renderShieldIfPresent(
		entity: LivingEntity,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int
	) {
		val list = LibAbsorptionShieldsRegistry.shields
		val livingEffects = entity.activeEffectsMap.keys

		list.parallelStream()
			.filter { livingEffects.contains(it.effect) }
			.findFirst()
			.ifPresent { renderShield(entity, poseStack, buffer, packedLight, it) }
	}

	@JvmStatic
	private fun renderShield(
		entity: LivingEntity,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int,
		entry: LibAbsorptionShieldsRegistry.ShieldEntry
	) {
		val radius = max(entity.bbWidth, entity.bbHeight) * 1.2f
		val color = entry.effect.value().color
		val r = ((color shr 16) and 0xFF) / 255f
		val g = ((color shr 8) and 0xFF) / 255f
		val b = (color and 0xFF) / 255f
		val a = 0.5f

		poseStack.pushPose()
		poseStack.translate(0.0, entity.bbHeight * 0.5, 0.0)
		poseStack.scale(radius, radius, radius)

		val consumer = buffer.getBuffer(SHIELD_RENDER_TYPE)

		SPHERE_MESH.render(consumer, poseStack.last(), r, g, b, a, packedLight)

		poseStack.popPose()
	}
}
