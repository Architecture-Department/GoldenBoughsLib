package architecture.goldenboughs_lib.api.world.item

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/**
 * 枪械武器接口
 * 定义了枪械的基本行为和功能，包括普通射击、瞄准射击等操作
 * 该接口允许实现类自定义枪械的射击逻辑、瞄准行为和相关条件判断
 */
@AllOpen
interface IGunWeapon {
	/**
	 * 判断副手是否可以使用该枪械攻击
	 * 
	 * @return 如果副手可以使用该枪械攻击则返回true，否则返回false
	 */
	fun isOffHandShoot(player: Player, stack: ItemStack): Boolean

	/**
	 * 获取枪械射击执行的延迟时间
	 * 
	 * @return 射击执行的延迟时间（以游戏刻为单位）
	 */
	fun gunShootExecuteTick(player: Player, stack: ItemStack, handUsed: InteractionHand): Int

	/**
	 * 执行枪械射击操作
	 * 在手持该物品时按攻击键触发
	 * 
	 * @return 返回false则不在服务器执行射击逻辑
	 */
	fun gunShoot(player: Player, stack: ItemStack, handUsed: InteractionHand): Boolean

	/**
	 * 判断玩家是否可以使用该枪械进行瞄准
	 * 
	 * @return 如果可以瞄准则返回true，否则返回false
	 */
	fun isGunAim(player: Player, itemStack: ItemStack): Boolean

	// TODO 待实装 瞄准状态下移动
	/**
	 * 判断玩家在瞄准状态下是否可以移动
	 * 
	 * @return 如果瞄准时可以移动则返回true，否则返回false
	 */
	fun isGunAimMove(player: Player, itemStack: ItemStack): Boolean

	/**
	 * 执行瞄准射击操作
	 * 在使用该物品时同时按住使用键和攻击键触发
	 * 
	 * @return 返回false则不在服务器执行瞄准射击逻辑
	 */
	fun gunAimShoot(player: Player, stack: ItemStack, handUsed: InteractionHand): Boolean
}
