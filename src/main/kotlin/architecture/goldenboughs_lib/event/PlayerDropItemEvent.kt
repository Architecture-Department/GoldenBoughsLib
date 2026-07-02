package architecture.goldenboughs_lib.event

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent

/**
 * 玩家丢弃物品事件 —— 在玩家丢弃物品（Q 键、背包拖出、Ctrl+Q 等）前触发。
 *
 * 此事件 [可取消][ICancellableEvent]，取消后物品不会丢弃。
 * 修改 [dropAll] 可改变丢弃方式（单个/整组）。
 *
 * @param player        丢弃物品的玩家
 * @param droppingStack 正在被丢弃的物品
 * @param dropAll       是否丢弃整组（Ctrl+Q = true，Q = false），可修改
 */
class PlayerDropItemEvent(
	val player: Player,
	val droppingStack: ItemStack,
	var dropAll: Boolean,
) : Event(), ICancellableEvent
