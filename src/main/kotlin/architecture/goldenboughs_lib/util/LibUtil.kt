package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.util.client.ClientLibUtil
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.loading.FMLEnvironment
import org.joml.Vector3d
import org.joml.Vector3fc

object LibUtil {
	@JvmStatic
	fun isClientSingleplayer(): Boolean =
		if (FMLEnvironment.dist.isClient) ClientLibUtil.isClientSingleplayer()
		else false

	@JvmStatic
	fun getTextureLight(path: ResourceLocation): ResourceLocation {
		return getTextureLight(path.toString())
	}

	@JvmStatic
	fun getTextureLight(path: String): ResourceLocation {
		return rlOf(path.substring(0, path.lastIndexOf(".png")) + "_glowmask.png")
	}

	@JvmStatic
	fun rlOf(namespace: String, path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(namespace, path)
	}

	@JvmStatic
	fun rlOf(location: String): ResourceLocation {
		return ResourceLocation.parse(location)
	}
}

fun Vector3fc.toVector3d(): Vector3d = get(Vector3d())