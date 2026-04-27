package architecture.goldenboughs_lib.config;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = GoldenBoughsLib.ID)
public final class LibConfig extends ConfigUtil {
	public static final LibCommonConfig COMMON;
	public static final ModConfigSpec COMMON_SPEC;
	public static final LibServerConfig SERVER;
	public static final ModConfigSpec SERVER_SPEC;
	public static final LibClientConfig CLIENT;
	public static final ModConfigSpec CLIENT_SPEC;

	static {
		var commonPair = configure(LibCommonConfig::new);
		COMMON = commonPair.getLeft();
		COMMON_SPEC = commonPair.getRight();
		var serverPair = configure(LibServerConfig::new);
		SERVER = serverPair.getLeft();
		SERVER_SPEC = serverPair.getRight();
		var clientPair = configure(LibClientConfig::new);
		CLIENT = clientPair.getLeft();
		CLIENT_SPEC = clientPair.getRight();
	}

	public static void init(ModContainer modContainer) {
		GoldenBoughsLib.LOGGER.info("Initialize the {} config files", GoldenBoughsLib.NAME);
		modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);
		modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
		modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		GoldenBoughsLib.LOGGER.info("Loaded {} config file {}", GoldenBoughsLib.NAME, configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		GoldenBoughsLib.LOGGER.info("{} config just got changed on the file system!", GoldenBoughsLib.NAME);
	}
}
