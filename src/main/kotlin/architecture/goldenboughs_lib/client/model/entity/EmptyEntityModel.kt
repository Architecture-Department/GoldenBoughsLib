package architecture.goldenboughs_lib.client.model.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.EntityModel
import net.minecraft.world.entity.Entity

class EmptyEntityModel<T : Entity> : EntityModel<T>() {
	override fun renderToBuffer(
		poseStack: PoseStack,
		buffer: VertexConsumer,
		packedLight: Int,
		packedOverlay: Int,
		color: Int
	) {
	}

	override fun setupAnim(
		entity: T,
		limbSwing: Float,
		limbSwingAmount: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {
	}
}
