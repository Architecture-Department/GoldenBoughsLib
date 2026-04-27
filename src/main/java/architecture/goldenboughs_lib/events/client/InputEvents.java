package architecture.goldenboughs_lib.events.client;

import architecture.goldenboughs_lib.api.world.item.IGunWeapon;
import architecture.goldenboughs_lib.api.world.item.IPlayerItemAttackClick;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.eventexecute.client.InputEventExecute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = GoldenBoughsLib.ID, value = Dist.CLIENT)
public final class InputEvents {

	@SubscribeEvent
	public static void onClientTickPre(ClientTickEvent.Pre event) {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player != null) {
			if (minecraft.screen == null) {
				InputEventExecute.handleGunWeaponInput(player, minecraft);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void interactionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
		Minecraft instance = Minecraft.getInstance();
		if (instance.screen != null) {
			return;
		}

		LocalPlayer player = instance.player;
		if (player == null) {
			return;
		}

		if (event.isAttack()) {
			ItemStack mainHandItem = player.getMainHandItem();
			if (mainHandItem.getItem() instanceof IPlayerItemAttackClick) {
				event.setSwingHand(false);
				event.setCanceled(true);
				return;
			}
			if (mainHandItem.getItem() instanceof IGunWeapon) {
				event.setSwingHand(false);
				event.setCanceled(true);
			}
		}

		if (event.isUseItem()) {
			ItemStack offHandItem = player.getOffhandItem();
			if (offHandItem.getItem() instanceof IPlayerItemAttackClick) {
				event.setSwingHand(false);
				event.setCanceled(true);
				return;
			}
			if (offHandItem.getItem() instanceof IGunWeapon iGunWeapon && iGunWeapon.isOffHandShoot(player, offHandItem)) {
				event.setSwingHand(false);
				event.setCanceled(true);
			}
		}
	}

	/**
	 * 左键点击空（客户端）
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void playerInteractEventLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
		Player entity = event.getEntity();
		if (entity.isUsingItem() && entity.getUseItem().getItem() instanceof IGunWeapon) {
		}
	}
}
