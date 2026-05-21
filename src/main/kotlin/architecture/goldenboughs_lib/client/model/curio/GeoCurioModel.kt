package architecture.goldenboughs_lib.client.model.curio

import architecture.goldenboughs_lib.client.model.GeoModelExpand
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import software.bernie.geckolib.animatable.GeoAnimatable

class GeoCurioModel<T>(
	modelPath: ResourceLocation,
	textureName: ResourceLocation,
	animationsName: ResourceLocation
) : GeoModelExpand<T>(
	modelPath(getPath(modelPath)),
	texturePath(getPath(textureName)),
	animationsPath(getPath(animationsName))
) where T : Item, T : GeoAnimatable {
	constructor(pathName: ResourceLocation) : this(pathName, pathName, pathName)

	companion object {
		@JvmStatic
		fun getPath(pathName: ResourceLocation): ResourceLocation {
			return pathName.withPrefix("curio/")
		}
	}
}
