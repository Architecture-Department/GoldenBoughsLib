package architecture.goldenboughs_lib.util.client

import net.minecraft.client.Minecraft

// 分类以避免导致客户端类加载
object ClientLibUtil {
	@JvmStatic
	fun isClientSingleplayer(): Boolean = Minecraft.getInstance().isSingleplayer
}