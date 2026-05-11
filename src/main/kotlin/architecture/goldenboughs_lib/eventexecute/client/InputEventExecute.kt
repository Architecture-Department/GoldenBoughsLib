package architecture.goldenboughs_lib.eventexecute.client

import architecture.goldenboughs_lib.api.world.item.IGunWeapon
import architecture.goldenboughs_lib.common.payload.tos.PlayerIGunWeaponPayload.Companion.send
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * 处理玩家输入事件的执行类
 * 主要处理枪械武器的相关输入逻辑
 */
object InputEventExecute {
	/**
	 * 处理枪械武器的输入事件
	 *
	 * @param player    本地玩家对象
	 * @param minecraft Minecraft客户端实例
	 */
	fun handleGunWeaponInput(player: LocalPlayer, minecraft: Minecraft) {
		val options = minecraft.options
		// 检查是否按下使用键
		run useKeyDownLabel@{
			if (!options.keyUse.isDown()) {
				return@useKeyDownLabel
			}
			// 同时按下攻击键的情况
			run useAndAttackKeyDownLabel@{
				if (!options.keyAttack.isDown()) {
					return@useAndAttackKeyDownLabel
				}

				// 检查玩家是否正在使用物品
				if (!player.isUsingItem) {
					return@useAndAttackKeyDownLabel
				}

				// 获取正在使用的物品
				val usingItem = player.getUseItem()
				if (usingItem.isEmpty) {
					return@useAndAttackKeyDownLabel
				}

				val item = usingItem.item
				// 检查是否为有效的枪械瞄准状态
				if (!isValidGunAimState(player, item, usingItem)) {
					return@useAndAttackKeyDownLabel
				}

				// 获取使用物品的手
				val usedItemHand = player.usedItemHand
				// 执行枪械瞄准射击
				if (!(item as IGunWeapon).gunAimShoot(player, usingItem, usedItemHand)) {
					return
				}

				send(usedItemHand, true, true)
				return
			}

			// 处理副手物品
			val offHandItem = player.offhandItem
			if (!isValidOffHandShootState(player, offHandItem)) {
				return@useKeyDownLabel
			}

			send(InteractionHand.OFF_HAND, false, true)
			return
		}

		// 仅按下攻击键的情况
		if (options.keyAttack.isDown()) {
			val mainHandItem = player.mainHandItem
			// 检查主手物品是否为有效枪械
			if (!isValidMainHandGun(player, mainHandItem)) {
				return
			}

			send(InteractionHand.MAIN_HAND, false, true)
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
	private fun isValidGunAimState(player: LocalPlayer, item: Item?, usingItem: ItemStack): Boolean {
		return (item is IGunWeapon) && item.isGunAim(player, usingItem)
	}

	/**
	 * 检查主手物品是否为有效的枪械
	 *
	 * @param player       玩家对象
	 * @param mainHandItem 主手物品
	 * @return 如果是有效的主手枪械返回true，否则返回false
	 */
	private fun isValidMainHandGun(player: LocalPlayer, mainHandItem: ItemStack): Boolean {
		val mainHandGun = mainHandItem.item
		return !mainHandItem.isEmpty && (mainHandGun is IGunWeapon) && mainHandGun.gunShoot(
			player, mainHandItem, InteractionHand.MAIN_HAND
		)
	}

	/**
	 * 检查副手物品是否为有效的射击状态
	 *
	 * @param player      玩家对象
	 * @param offHandItem 副手物品
	 * @return 如果是有效的副手射击状态返回true，否则返回false
	 */
	private fun isValidOffHandShootState(player: LocalPlayer, offHandItem: ItemStack): Boolean {
		val iGunWeapon = offHandItem.item
		return !offHandItem.isEmpty && (iGunWeapon is IGunWeapon) && iGunWeapon.isOffHandShoot(
			player, offHandItem
		) && iGunWeapon.gunShoot(player, offHandItem, InteractionHand.OFF_HAND)
	}
}
