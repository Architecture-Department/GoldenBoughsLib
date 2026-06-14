package architecture.goldenboughs_lib.client.model

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.cache.GeckoLibCache
import software.bernie.geckolib.model.GeoModel

@AllOpe
class GeoModelExpand<T : GeoAnimatable>(
	modelPath: ResourceLocation,
	texturePath: ResourceLocation,
	animationsPath: ResourceLocation
) : GeoModel<T>() {
	val modelPath: ResourceLocation = modelPath.withSuffix(".geo.json")
	val texturePath: ResourceLocation = texturePath.withSuffix(".png")
	val animationsPath: ResourceLocation = animationsPath.withSuffix(".animation.json")

	protected val defaultModelResource: ResourceLocation = LibUtil.modRl("geo/item/default.geo.json")

	override fun getModelResource(animatable: T?): ResourceLocation =
		if (GeckoLibCache.getBakedModels()[this.modelPath] == null) this.defaultModelResource
		else this.modelPath

	override fun getTextureResource(animatable: T?): ResourceLocation = texturePath

	override fun getAnimationResource(animatable: T): ResourceLocation = animationsPath

	companion object {
		@JvmStatic
		fun modelPath(path: ResourceLocation): ResourceLocation = path.withPrefix("geo/")

		@JvmStatic
		fun texturePath(path: ResourceLocation): ResourceLocation = path.withPrefix("textures/geo/")

		@JvmStatic
		fun animationsPath(path: ResourceLocation): ResourceLocation = path.withPrefix("animations/")
	}
}
