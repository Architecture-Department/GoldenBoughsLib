package architecture.goldenboughs_lib.events;

import architecture.goldenboughs_lib.core.Lib;
import architecture.goldenboughs_lib.eventexecute.RationalityEventExecutes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * 玩家相关事件
 */
@EventBusSubscriber(modid = Lib.ID)
public final class PlayerEvents {

	@SubscribeEvent
	public static void tickPre(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		if (player instanceof ServerPlayer serverPlayer) {
			RationalityEventExecutes.refreshRationalityValue(serverPlayer);
		}
	}

	/**
	 * 玩家重生或维度切换后
	 */
	@SubscribeEvent
	public static void reset(PlayerEvent.Clone event) {
	}

	/**
	 * 登录到世界-此时客户端玩家已创建
	 */
	@SubscribeEvent
	public static void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {
	}
}
