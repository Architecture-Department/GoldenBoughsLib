package architecture.goldenboughs_lib.client.model

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.core.GoldenBoughsLib
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
	val modelPath: ResourceLocation = modelPath.withSuffix(MODEL_SUFFIX)
	val texturePath: ResourceLocation = texturePath.withSuffix(TEXTURE_SUFFIX)
	val animationsPath: ResourceLocation = animationsPath.withSuffix(ANIMATIONS_SUFFIX)

	override fun getModelResource(animatable: T?): ResourceLocation {
		return if (GeckoLibCache.getBakedModels()[this.modelPath] == null
		) this.defaultModelResource else this.modelPath
	}

	override fun getTextureResource(animatable: T?): ResourceLocation {
		return texturePath
	}

	override fun getAnimationResource(animatable: T): ResourceLocation {
		return animationsPath
	}

	protected val defaultModelResource: ResourceLocation =
		model(GoldenBoughsLib.modRl("item/default")).withSuffix(MODEL_SUFFIX)

	companion object {
		const val MODEL_SUFFIX: String = ".geo.json"
		const val TEXTURE_SUFFIX: String = ".png"
		const val ANIMATIONS_SUFFIX: String = ".animation.json"

		@JvmStatic
		fun model(path: ResourceLocation): ResourceLocation {
			return path.withPrefix("geo/")
		}

		@JvmStatic
		fun texturePath(path: ResourceLocation): ResourceLocation {
			return path.withPrefix("textures/geo/")
		}

		@JvmStatic
		fun animationsPath(path: ResourceLocation): ResourceLocation {
			return path.withPrefix("animations/")
		}
	}
}
