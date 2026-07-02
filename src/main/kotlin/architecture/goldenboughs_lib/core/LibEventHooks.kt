package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.event.PlayerDropItemEvent
import architecture.goldenboughs_lib.event.PlayerHotbarChangeEvent
import architecture.goldenboughs_lib.module.rationality.event.RationalityEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.NeoForge

object LibEventHooks {
	@JvmStatic
	fun sourceRationalityPre(player: Player, oldValue: Float, newValue: Float): RationalityEvent.Modify.Pre {
		return NeoForge.EVENT_BUS.post(RationalityEvent.Modify.Pre(player, oldValue, newValue))
	}

	@JvmStatic
	fun sourceRationalityPost(player: Player, oldValue: Float, newValue: Float) {
		NeoForge.EVENT_BUS.post(RationalityEvent.Modify.Post(player, oldValue, newValue))
	}

	@JvmStatic
	fun playerHotbarChange(
		player: Player,
		fromSlot: Int,
		toSlot: Int,
		fromStack: ItemStack,
		toStack: ItemStack,
	): PlayerHotbarChangeEvent {
		return NeoForge.EVENT_BUS.post(PlayerHotbarChangeEvent(player, fromSlot, toSlot, fromStack, toStack))
	}

	@JvmStatic
	fun playerDropItem(
		player: Player,
		hand: InteractionHand,
	): PlayerDropItemEvent {
		return NeoForge.EVENT_BUS.post(PlayerDropItemEvent(player, hand))
	}
}
