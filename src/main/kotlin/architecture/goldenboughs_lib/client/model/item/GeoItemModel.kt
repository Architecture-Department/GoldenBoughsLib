package architecture.goldenboughs_lib.client.model.item

import architecture.goldenboughs_lib.client.model.GeoModelExpand
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable

class GeoItemModel<T : GeoAnimatable>(
	modelPath: ResourceLocation,
	textureName: ResourceLocation,
	animationsName: ResourceLocation
) : GeoModelExpand<T>(
	model(getPath(modelPath)),
	texturePath(getPath(textureName)),
	animationsPath(getPath(animationsName))
) {
	constructor(pathName: ResourceLocation) : this(pathName, pathName, pathName)

	companion object {
		@JvmStatic
		fun getPath(modelPath: ResourceLocation): ResourceLocation {
			return modelPath.withPrefix("item/")
		}
	}
}
