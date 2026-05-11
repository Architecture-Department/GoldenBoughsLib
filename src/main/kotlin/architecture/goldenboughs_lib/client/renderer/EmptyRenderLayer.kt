package architecture.goldenboughs_lib.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.world.entity.Entity

class EmptyRenderLayer<T : Entity, M : EntityModel<T>>(renderer: RenderLayerParent<T, M>) :
	RenderLayer<T, M>(renderer) {
	override fun render(
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		livingEntity: T,
		limbSwing: Float,
		limbSwingAmount: Float,
		partialTick: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {
	}
}
