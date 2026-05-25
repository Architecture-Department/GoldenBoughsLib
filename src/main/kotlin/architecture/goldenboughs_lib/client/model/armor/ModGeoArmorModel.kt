package architecture.goldenboughs_lib.client.model.armor

import architecture.goldenboughs_lib.client.model.GeoModelExpand
import architecture.goldenboughs_lib.core.Lib
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable
import architecture.goldenboughs_lib.core.LibConstants

class ModGeoArmorModel<T : GeoAnimatable>(
	modelPath: ResourceLocation,
	textureName: ResourceLocation,
	animationsName: ResourceLocation
) : GeoModelExpand<T>(
	modelPath(getPath(modelPath)), texturePath(getPath(textureName)), animationsPath(getPath(animationsName))
) {
	constructor(pathName: ResourceLocation) : this(pathName, pathName, pathName)

	override val defaultModelResource: ResourceLocation =
		modelPath(LibConstants.modRl("armor/default.geo.json"))

	companion object {
		@JvmStatic
		fun getPath(name: ResourceLocation): ResourceLocation {
			return name.withPrefix("armor/")
		}
	}
}
