package architecture.goldenboughs_lib.client.model.item

import architecture.goldenboughs_lib.core.GoldenBoughsLib
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable

class GuiGeoItemModel<T : GeoAnimatable> : GeoItemModel<T> {
	private val resourceLocation: ResourceLocation

	constructor(path: ResourceLocation) : super(path) {
		this.resourceLocation = path.withPrefix("textures/item/").withSuffix(".png")
	}

	constructor(modelPath: ResourceLocation, textureName: ResourceLocation, animationsName: ResourceLocation) : super(
		modelPath,
		textureName,
		animationsName
	) {
		this.resourceLocation = textureName.withPrefix("textures/item/").withSuffix(".png")
	}

	override fun getModelResource(animatable: T?): ResourceLocation {
		return MODELLED_PATH
	}

	override fun getTextureResource(animatable: T?): ResourceLocation {
		return resourceLocation
	}

	companion object {
		private val MODELLED_PATH: ResourceLocation =
			model(GoldenBoughsLib.modRl("item/gui_item_model")).withPrefix(MODEL_SUFFIX)
	}
}
