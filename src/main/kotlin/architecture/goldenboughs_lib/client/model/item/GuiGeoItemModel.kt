package architecture.goldenboughs_lib.client.model.item

import architecture.goldenboughs_lib.core.Lib
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable

class GuiGeoItemModel<T : GeoAnimatable> : GeoItemModel<T> {
	private val texturesRl: ResourceLocation

	constructor(path: ResourceLocation) : super(path) {
		this.texturesRl = path.withPrefix("textures/item/").withSuffix(".png")
	}

	constructor(modelPath: ResourceLocation, textureName: ResourceLocation, animationsName: ResourceLocation) : super(
		modelPath,
		textureName,
		animationsName
	) {
		this.texturesRl = textureName.withPrefix("textures/item/").withSuffix(".png")
	}

	override fun getModelResource(animatable: T?): ResourceLocation {
		return MODELLED_PATH
	}

	override fun getTextureResource(animatable: T?): ResourceLocation {
		return texturesRl
	}

	companion object {
		private val MODELLED_PATH: ResourceLocation = Lib.modRl("geo/item/gui_item_model.geo.json")
	}
}
