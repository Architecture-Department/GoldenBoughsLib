package architecture.goldenboughs_lib.eventexecute.client;

import architecture.goldenboughs_lib.api.world.item.IGunWeapon;
import architecture.goldenboughs_lib.common.payload.tos.PlayerIGunWeaponPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 处理玩家输入事件的执行类
 * 主要处理枪械武器的相关输入逻辑
 */
public final class InputEventExecute {
	/**
	 * 处理枪械武器的输入事件
	 *
	 * @param player    本地玩家对象
	 * @param minecraft Minecraft客户端实例
	 */
	public static void handleGunWeaponInput(LocalPlayer player, Minecraft minecraft) {
		Options options = minecraft.options;
		// 检查是否按下使用键
		useKeyDownLabel:
		if (options.keyUse.isDown()) {
			// 同时按下攻击键的情况
			useAndAttackKeyDownLabel:
			if (options.keyAttack.isDown()) {
				// 检查玩家是否正在使用物品
				if (!player.isUsingItem()) {
					break useAndAttackKeyDownLabel;
				}

				// 获取正在使用的物品
				ItemStack usingItem = player.getUseItem();
				if (usingItem.isEmpty()) {
					break useAndAttackKeyDownLabel;
				}

				Item item = usingItem.getItem();
				// 检查是否为有效的枪械瞄准状态
				if (!isValidGunAimState(player, item, usingItem)) {
					break useAndAttackKeyDownLabel;
				}

				// 获取使用物品的手
				InteractionHand usedItemHand = player.getUsedItemHand();
				// 执行枪械瞄准射击
				if (!((IGunWeapon) item).gunAimShoot(player, usingItem, usedItemHand)) {
					return;
				}

				PlayerIGunWeaponPayload.send(usedItemHand, true, true);
				return;
			}

			// 处理副手物品
			ItemStack offHandItem = player.getOffhandItem();
			if (!isValidOffHandShootState(player, offHandItem)) {
				break useKeyDownLabel;
			}

			PlayerIGunWeaponPayload.send(InteractionHand.OFF_HAND, false, true);
			return;
		}

		// 仅按下攻击键的情况
		if (options.keyAttack.isDown()) {
			ItemStack mainHandItem = player.getMainHandItem();
			// 检查主手物品是否为有效枪械
			if (!isValidMainHandGun(player, mainHandItem)) {
				return;
			}

			PlayerIGunWeaponPayload.send(InteractionHand.MAIN_HAND, false, true);
		}
	}

	/**
	 * 检查枪械是否处于有效的瞄准状态
	 *
	 * @param player    玩家对象
	 * @param item      物品对象
	 * @param usingItem 正在使用的物品堆
	 * @return 如果是有效的枪械瞄准状态返回true，否则返回false
	 */
	private static boolean isValidGunAimState(LocalPlayer player, Item item, ItemStack usingItem) {
		return (item instanceof IGunWeapon iGunWeapon) && iGunWeapon.isGunAim(player, usingItem);
	}

	/**
	 * 检查主手物品是否为有效的枪械
	 *
	 * @param player       玩家对象
	 * @param mainHandItem 主手物品
	 * @return 如果是有效的主手枪械返回true，否则返回false
	 */
	private static boolean isValidMainHandGun(LocalPlayer player, ItemStack mainHandItem) {
		return !mainHandItem.isEmpty() &&
			(mainHandItem.getItem() instanceof IGunWeapon mainHandGun) &&
			mainHandGun.gunShoot(player, mainHandItem, InteractionHand.MAIN_HAND);
	}

	/**
	 * 检查副手物品是否为有效的射击状态
	 *
	 * @param player      玩家对象
	 * @param offHandItem 副手物品
	 * @return 如果是有效的副手射击状态返回true，否则返回false
	 */
	private static boolean isValidOffHandShootState(LocalPlayer player, ItemStack offHandItem) {
		return !offHandItem.isEmpty() &&
			(offHandItem.getItem() instanceof IGunWeapon iGunWeapon) &&
			iGunWeapon.isOffHandShoot(player, offHandItem) &&
			iGunWeapon.gunShoot(player, offHandItem, InteractionHand.OFF_HAND);
	}
}
