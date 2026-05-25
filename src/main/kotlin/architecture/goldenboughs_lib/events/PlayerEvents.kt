package architecture.goldenboughs_lib.events

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.eventexecute.RationalityEventExecutes
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import architecture.goldenboughs_lib.core.LibConstants

/**
 * 玩家相关事件
 */
@EventBusSubscriber(modid = LibConstants.ID)
object PlayerEvents {
	@SubscribeEvent
	fun tickPre(event: PlayerTickEvent.Pre) {
		val player = event.entity
		if (player is ServerPlayer) {
			RationalityEventExecutes.refreshRationalityValue(player)
		}
	}

	/**
	 * 玩家重生或维度切换后
	 */
	@SubscribeEvent
	fun reset(event: PlayerEvent.Clone) {
	}

	/**
	 * 登录到世界-此时客户端玩家已创建
	 */
	@SubscribeEvent
	fun loggedIn(event: PlayerEvent.PlayerLoggedInEvent) {
	}
}
