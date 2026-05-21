package architecture.goldenboughs_lib.util

import net.minecraft.resources.ResourceLocation

object LibUtils {
	@JvmStatic
	fun getTextureLight(path: ResourceLocation): ResourceLocation {
		return getTextureLight(path.toString())
	}

	@JvmStatic
	fun getTextureLight(path: String): ResourceLocation {
		return ResourceLocation.parse(path.substring(0, path.lastIndexOf(".png")) + "_glowmask.png")
	}
}
