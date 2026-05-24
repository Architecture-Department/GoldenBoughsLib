package architecture.goldenboughs_lib.util

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.loading.FMLEnvironment

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

// 分类以避免导致客户端类加载
object ClientLibUtil {
	@JvmStatic
	fun isClientSingleplayer(): Boolean = Minecraft.getInstance().isSingleplayer()
}
