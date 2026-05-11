package architecture.goldenboughs_lib.client.renderer

import architecture.goldenboughs_lib.client.model.entity.EmptyEntityModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Mob

class EmptyMobRenderer<T : Mob>(
	context: EntityRendererProvider.Context,
	shadowRadius: Float,
	private val texture: ResourceLocation
) : MobRenderer<T, EmptyEntityModel<T>>(context, EmptyEntityModel<T>(), shadowRadius) {
	override fun getTextureLocation(entity: T): ResourceLocation {
		return texture
	}
}
