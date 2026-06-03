package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.api.world.item.IGunWeapon
import architecture.goldenboughs_lib.api.world.item.IPlayerItemAttackClick
import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.eventexecute.client.InputEventExecute
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = LibConstants.ID, value = [Dist.CLIENT])
object InputEvents {
	@SubscribeEvent
	fun onClientTickPre(event: ClientTickEvent.Pre) {
		val minecraft = Minecraft.getInstance()
		val player = minecraft.player
		if (player != null) {
			if (minecraft.screen == null) {
				InputEventExecute.handleGunWeaponInput(player, minecraft)
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onInteractionKeyMappingTriggered(event: InputEvent.InteractionKeyMappingTriggered) {
		val instance = Minecraft.getInstance()
		if (instance.screen != null) {
			return
		}

		val player = instance.player ?: return

		if (event.isAttack) {
			val mainHandItem = player.mainHandItem
			if (mainHandItem.item is IPlayerItemAttackClick) {
				event.setSwingHand(false)
				event.setCanceled(true)
				return
			}
			if (mainHandItem.item is IGunWeapon) {
				event.setSwingHand(false)
				event.setCanceled(true)
			}
		}

		if (event.isUseItem) {
			val offHandItem = player.offhandItem
			val item = offHandItem.item
			if (item is IPlayerItemAttackClick) {
				event.setSwingHand(false)
				event.setCanceled(true)
				return
			}
			if (item is IGunWeapon && item.isOffHandShoot(player, offHandItem)) {
				event.setSwingHand(false)
				event.setCanceled(true)
			}
		}
	}

	/**
	 * 左键点击空（客户端）
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerInteractEventLeftClickEmpty(event: PlayerInteractEvent.LeftClickEmpty) {
		val entity = event.entity
		if (entity.isUsingItem && entity.getUseItem().item is IGunWeapon) {
		}
	}
}
