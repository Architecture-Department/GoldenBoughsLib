package architecture.goldenboughs_lib.client.model.item

import architecture.goldenboughs_lib.client.model.GeoModelExpand
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable

class GeoItemModel<T : GeoAnimatable>(
	modelPath: ResourceLocation,
	texturePath: ResourceLocation,
	animationsPath: ResourceLocation
) : GeoModelExpand<T>(
	modelPath(getPath(modelPath)),
	texturePath(getPath(texturePath)),
	animationsPath(getPath(animationsPath))
) {
	constructor(pathName: ResourceLocation) : this(pathName, pathName, pathName)

	companion object {
		@JvmStatic
		fun getPath(modelPath: ResourceLocation): ResourceLocation {
			return modelPath.withPrefix("item/")
		}
	}
}
