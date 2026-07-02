package architecture.goldenboughs_lib.event

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent

/**
 * 玩家丢弃物品事件 ( TODO 未完善)
 */
class PlayerDropItemEvent(
	val player: Player,
	val hand: InteractionHand,
) : Event(), ICancellableEvent
