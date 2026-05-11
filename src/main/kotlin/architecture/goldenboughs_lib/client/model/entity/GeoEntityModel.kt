package architecture.goldenboughs_lib.client.model.entity

import architecture.goldenboughs_lib.client.model.GeoModelExpand
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable

class GeoEntityModel<T : GeoAnimatable>(
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
		fun getPath(location: ResourceLocation): ResourceLocation {
			return location.withPrefix("entity/")
		}
	}
}
