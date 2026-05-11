package architecture.goldenboughs_lib.config

import architecture.goldenboughs_lib.core.Lib
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.config.ModConfigEvent.Loading
import net.neoforged.fml.event.config.ModConfigEvent.Reloading
import net.neoforged.neoforge.common.ModConfigSpec

@EventBusSubscriber(modid = Lib.ID)
object LibConfig : LibConfigUtil() {
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
	fun init(modContainer: ModContainer) {
		Lib.LOGGER.info("Initialize the ${Lib.NAME} config files")
		modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC)
		modContainer.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC)
		modContainer.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC)
	}

	@SubscribeEvent
	fun onLoad(configEvent: Loading) {
		Lib.LOGGER.info("Loaded ${Lib.NAME} config file ${configEvent.config.fileName}")
	}

	@SubscribeEvent
	fun onFileChange(configEvent: Reloading) {
		Lib.LOGGER.info("${Lib.NAME} config just got changed on the file system!")
	}
}
