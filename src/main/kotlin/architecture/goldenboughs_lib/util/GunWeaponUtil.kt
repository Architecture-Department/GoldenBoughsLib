package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.api.world.item.IGunWeapon
import architecture.goldenboughs_lib.init.LibAttachmentTypes
import architecture.goldenboughs_lib.init.LibAttributes
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import kotlin.math.max
import kotlin.math.min

/**
 * 枪械武器工具类
 * 提供对枪械武器的各种操作和属性管理功能
 */
object GunWeaponUtil {
	/**
	 * 检查指定手是否可以进行攻击
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 如果指定手可以攻击则返回true，否则返回false
	 */
	@JvmStatic
	fun isAttack(player: Player, handUsed: InteractionHand?): Boolean {
		return isAttack(player, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 检查指定手是否可以进行攻击
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 如果指定手可以攻击则返回true，否则返回false
	 */
	@JvmStatic
	fun isAttack(player: Player, isHandUsed: Boolean): Boolean {
		return if (isHandUsed) player.getData<Boolean>(LibAttachmentTypes.IS_GUN_ATTACK_MAIN_HAND)
		else player.getData<Boolean>(LibAttachmentTypes.IS_GUN_ATTACK_OFF_HAND)
	}

	/**
	 * 设置指定手的攻击状态
	 *
	 * @param player   玩家对象
	 * @param isAttack 攻击状态
	 * @param handUsed 使用的手（主手或副手）
	 */
	@JvmStatic
	fun setIsAttack(player: Player, isAttack: Boolean, handUsed: InteractionHand?) {
		setIsAttack(player, isAttack, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 设置指定手的攻击状态
	 *
	 * @param player     玩家对象
	 * @param isAttack   攻击状态
	 * @param isHandUsed 是否为主手
	 */
	@JvmStatic
	fun setIsAttack(player: Player, isAttack: Boolean, isHandUsed: Boolean) {
		player.setData<Boolean?>(
			if (isHandUsed) LibAttachmentTypes.IS_GUN_ATTACK_MAIN_HAND else LibAttachmentTypes.IS_GUN_ATTACK_OFF_HAND,
			isAttack
		)
	}

	/**
	 * 获取指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 指定手的蓄力值
	 */
	@JvmStatic
	fun getChargeUpValue(player: Player, handUsed: InteractionHand?): Int {
		return getChargeUpValue(player, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 获取指定手的蓄力值
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 指定手的蓄力值
	 */
	@JvmStatic
	fun getChargeUpValue(player: Player, isHandUsed: Boolean): Int {
		return player.getData(if (isHandUsed) LibAttachmentTypes.GUN_CHARGE_UP_TICK_MAIN_HAND else LibAttachmentTypes.GUN_CHARGE_UP_TICK_OFF_HAND)
	}

	/**
	 * 设置指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param newValue 新的蓄力值
	 * @param handUsed 使用的手（主手或副手）
	 */
	@JvmStatic
	fun setChargeUpValue(player: Player, newValue: Int, handUsed: InteractionHand?) {
		setChargeUpValue(player, newValue, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 设置指定手的蓄力值
	 *
	 * @param player     玩家对象
	 * @param newValue   新的蓄力值
	 * @param isHandUsed 是否为主手
	 */
	@JvmStatic
	fun setChargeUpValue(player: Player, newValue: Int, isHandUsed: Boolean) {
		player.setData<Int?>(
			if (isHandUsed) LibAttachmentTypes.GUN_CHARGE_UP_TICK_MAIN_HAND else LibAttachmentTypes.GUN_CHARGE_UP_TICK_OFF_HAND,
			max(0, min(newValue, getMaxChargeUpValue(player, isHandUsed)))
		)
	}

	/**
	 * 获取指定手的最大蓄力值
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 指定手的最大蓄力值
	 */
	@JvmStatic
	fun getMaxChargeUpValue(player: Player, isHandUsed: Boolean): Int {
		return (1.0f / getAttackSpeed(player, isHandUsed) * 20.0f).toInt()
	}

	/**
	 * 获取指定手的攻击速度
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 指定手的攻击速度
	 */
	@JvmStatic
	fun getAttackSpeed(player: Player, isHandUsed: Boolean): Double {
		return player.getAttributeValue(Attributes.ATTACK_SPEED) + player.getAttributeValue(if (isHandUsed) LibAttributes.ATTACK_SPEED_MAIN_HAND else LibAttributes.ATTACK_SPEED_OFF_HAND)
	}

	/**
	 * 修改指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param value    要增加的蓄力值
	 * @param handUsed 使用的手（主手或副手）
	 */
	@JvmStatic
	fun modifyChargeUpValue(player: Player, value: Int, handUsed: InteractionHand?) {
		modifyChargeUpValue(player, value, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 修改指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param value    要增加的蓄力值
	 * @param handUsed 使用的手（主手或副手）
	 */
	@JvmStatic
	fun modifyChargeUpValue(player: Player, value: Int, handUsed: Boolean) {
		setChargeUpValue(player, getChargeUpValue(player, handUsed) + value, handUsed)
	}

	/**
	 * 重置指定手的蓄力值为0
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 */
	@JvmStatic
	fun resetChargeUp(player: Player, handUsed: InteractionHand?) {
		resetChargeUp(player, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 重置指定手的蓄力值为0
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 */
	@JvmStatic
	fun resetChargeUp(player: Player, isHandUsed: Boolean) {
		setChargeUpValue(player, 0, isHandUsed)
	}

	/**
	 * 获取指定手的蓄力百分比
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 蓄力百分比，范围在0.0f到1.0f之间
	 */
	@JvmStatic
	fun getChargeUpPercentage(player: Player, handUsed: InteractionHand?): Float {
		return getChargeUpPercentage(player, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 获取指定手的蓄力百分比
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 蓄力百分比，范围在0.0f到1.0f之间
	 */
	@JvmStatic
	fun getChargeUpPercentage(player: Player, isHandUsed: Boolean): Float {
		return Math.clamp(
			getChargeUpValue(player, isHandUsed).toFloat() / getMaxChargeUpValue(player, isHandUsed),
			0.0f,
			1.0f
		)
	}

	/**
	 * 设置指定手的蓄力百分比
	 *
	 * @param player   玩家对象
	 * @param newValue 新的蓄力百分比
	 * @param handUsed 使用的手（主手或副手）
	 */
	@JvmStatic
	fun setChargeUpPercentage(player: Player, newValue: Float, handUsed: InteractionHand?) {
		setChargeUpPercentage(player, newValue, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 设置指定手的蓄力百分比
	 *
	 * @param player     玩家对象
	 * @param newValue   新的蓄力百分比
	 * @param isHandUsed 是否为主手
	 */
	@JvmStatic
	fun setChargeUpPercentage(player: Player, newValue: Float, isHandUsed: Boolean) {
		val maxChargeUpValue = getMaxChargeUpValue(player, isHandUsed)
		if (maxChargeUpValue <= 0) {
			return
		}
		setChargeUpValue(player, Math.clamp(newValue / maxChargeUpValue, 0.0f, 1.0f).toInt(), isHandUsed)
	}

	/**
	 * 修改指定手的蓄力百分比
	 *
	 * @param player     玩家对象
	 * @param value      要增加的蓄力百分比
	 * @param isHandUsed 是否为主手
	 */
	@JvmStatic
	fun modifyChargeUpPercentage(player: Player, value: Float, isHandUsed: Boolean) {
		setChargeUpPercentage(player, getChargeUpPercentage(player, isHandUsed) + value, isHandUsed)
	}

	/**
	 * 修改指定手的蓄力百分比
	 *
	 * @param player   玩家对象
	 * @param value    要增加的蓄力百分比
	 * @param handUsed 使用的手（主手或副手）
	 */
	@JvmStatic
	fun modifyChargeUpPercentage(player: Player, value: Float, handUsed: InteractionHand?) {
		modifyChargeUpPercentage(player, value, handUsed == InteractionHand.MAIN_HAND)
	}

	//  /**
	//   * 检查指定手是否正在射击
	//   *
	//   * @param player 玩家对象
	//   * @param isHandUsed 是否为主手
	//   * @return 如果指定手正在射击则返回true，否则返回false
	//   */
	//  public static boolean isShooting(Player player, boolean isHandUsed) {
	//    return getAttackSpeed(player, isHandUsed) > 0;
	//  }
	//
	//  /**
	//   * 检查指定手是否正在射击
	//   *
	//   * @param player 玩家对象
	//   * @param handUsed 使用的手（主手或副手）
	//   * @return 如果指定手正在射击则返回true，否则返回false
	//   */
	//  public static boolean isShooting(Player player, InteractionHand handUsed) {
	//    return isShooting(player, handUsed == InteractionHand.MAIN_HAND);
	//  }
	/**
	 * 获取指定手的最大蓄力值
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 指定手的最大蓄力值
	 */
	@JvmStatic
	fun getMaxChargeUpValue(player: Player, handUsed: InteractionHand?): Int {
		return getMaxChargeUpValue(player, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 获取指定手的攻击速度
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 指定手的攻击速度
	 */
	@JvmStatic
	private fun getAttackSpeed(player: Player, handUsed: InteractionHand?): Double {
		return getAttackSpeed(player, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 检查生物是否持有枪械武器
	 *
	 * @param livingEntity 生物对象
	 * @return 如果持有枪械武器则返回true，否则返回false
	 */
	@JvmStatic
	fun `is`(livingEntity: LivingEntity): Boolean {
		return livingEntity.mainHandItem.item is IGunWeapon ||
			livingEntity.offhandItem.item is IGunWeapon
	}

	/**
	 * 检查指定手是否持有枪械武器
	 *
	 * @param livingEntity 生物对象
	 * @param handUsed     使用的手（主手或副手）
	 * @return 如果指定手持有枪械武器则返回true，否则返回false
	 */
	@JvmStatic
	fun `is`(livingEntity: LivingEntity, handUsed: InteractionHand?): Boolean {
		return `is`(livingEntity, handUsed == InteractionHand.MAIN_HAND)
	}

	/**
	 * 检查指定手是否持有枪械武器
	 *
	 * @param livingEntity 生物对象
	 * @param isHandUsed   是否为主手
	 * @return 如果指定手持有枪械武器则返回true，否则返回false
	 */
	@JvmStatic
	fun `is`(livingEntity: LivingEntity, isHandUsed: Boolean): Boolean {
		return (if (isHandUsed) livingEntity.mainHandItem.item
		else livingEntity.offhandItem.item) is IGunWeapon
	}
}
