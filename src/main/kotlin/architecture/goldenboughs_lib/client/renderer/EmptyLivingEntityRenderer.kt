package architecture.goldenboughs_lib.client.renderer

import architecture.goldenboughs_lib.client.model.entity.EmptyEntityModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity

class EmptyLivingEntityRenderer<T : LivingEntity>(
	context: EntityRendererProvider.Context,
	shadowRadius: Float,
	private val texture: ResourceLocation
) : LivingEntityRenderer<T, EmptyEntityModel<T>>(context, EmptyEntityModel<T>(), shadowRadius) {
	override fun getTextureLocation(entity: T): ResourceLocation {
		return texture
	}

	// TODO 需要更好的方案
	override fun shouldShowName(entity: T): Boolean {
		return false
	}
}
