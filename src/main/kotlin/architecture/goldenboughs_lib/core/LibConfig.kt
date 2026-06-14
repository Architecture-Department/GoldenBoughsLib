package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.api.BasicConfigMapper
import architecture.goldenboughs_lib.config.LibClientConfig
import architecture.goldenboughs_lib.config.LibCommonConfig
import architecture.goldenboughs_lib.config.LibServerConfig
import architecture.goldenboughs_lib.util.LibUtil
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.config.ModConfigEvent
import net.neoforged.neoforge.common.ModConfigSpec

@EventBusSubscriber(modid = LibUtil.ID)
object LibConfig : BasicConfigMapper() {
	@JvmField
	val COMMON: LibCommonConfig

	@JvmField
	val COMMON_SPEC: ModConfigSpec

	@JvmField
	val SERVER: LibServerConfig

	@JvmField
	val SERVER_SPEC: ModConfigSpec

	@JvmField
	val CLIENT: LibClientConfig

	@JvmField
	val CLIENT_SPEC: ModConfigSpec

	init {
		val commonPair = configure(::LibCommonConfig)
		COMMON = commonPair.getLeft()
		COMMON_SPEC = commonPair.getRight()
		val serverPair = configure(::LibServerConfig)
		SERVER = serverPair.getLeft()
		SERVER_SPEC = serverPair.getRight()
		val clientPair = configure(::LibClientConfig)
		CLIENT = clientPair.getLeft()
		CLIENT_SPEC = clientPair.getRight()
	}

	@JvmStatic
	fun register(modContainer: ModContainer) {
		LibUtil.LOGGER.info("Initialize the ${LibUtil.NAME} config files")
		modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC)
		modContainer.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC)
		modContainer.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC)
	}

	@SubscribeEvent
	fun onLoad(configEvent: ModConfigEvent.Loading) {
		LibUtil.LOGGER.info("Loaded ${LibUtil.NAME} config file ${configEvent.config.fileName}")
	}

	@SubscribeEvent
	fun onFileChange(configEvent: ModConfigEvent.Reloading) {
		LibUtil.LOGGER.info("${LibUtil.NAME} config just got changed on the file system!")
	}
}