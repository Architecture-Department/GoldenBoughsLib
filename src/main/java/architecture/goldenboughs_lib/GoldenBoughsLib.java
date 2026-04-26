package architecture.goldenboughs_lib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GoldenBoughsLib.ID)
public class GoldenBoughsLib {

	public static final String ID = "goldenboughs_lib";
	public static final String NAME = "GoldenBoughsLib";
	public static final Logger LOGGER = LogManager.getLogger(ID);

	public GoldenBoughsLib(IEventBus modEventBus, ModContainer modContainer) {
		NeoForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}
}
