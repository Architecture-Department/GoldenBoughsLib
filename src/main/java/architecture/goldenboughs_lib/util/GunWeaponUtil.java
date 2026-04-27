package architecture.goldenboughs_lib.util;

import architecture.goldenboughs_lib.api.world.item.IGunWeapon;
import architecture.goldenboughs_lib.init.LibAttachments;
import architecture.goldenboughs_lib.init.LibAttributes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * 枪械武器工具类
 * 提供对枪械武器的各种操作和属性管理功能
 */
public final class GunWeaponUtil {

	/**
	 * 检查指定手是否可以进行攻击
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 如果指定手可以攻击则返回true，否则返回false
	 */
	public static boolean isAttack(Player player, InteractionHand handUsed) {
		return isAttack(player, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 检查指定手是否可以进行攻击
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 如果指定手可以攻击则返回true，否则返回false
	 */
	public static boolean isAttack(Player player, boolean isHandUsed) {
		return isHandUsed ? player.getData(LibAttachments.IS_GUN_ATTACK_MAIN_HAND) : player.getData(LibAttachments.IS_GUN_ATTACK_OFF_HAND);
	}

	/**
	 * 设置指定手的攻击状态
	 *
	 * @param player   玩家对象
	 * @param isAttack 攻击状态
	 * @param handUsed 使用的手（主手或副手）
	 */
	public static void setIsAttack(Player player, boolean isAttack, InteractionHand handUsed) {
		setIsAttack(player, isAttack, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 设置指定手的攻击状态
	 *
	 * @param player     玩家对象
	 * @param isAttack   攻击状态
	 * @param isHandUsed 是否为主手
	 */
	public static void setIsAttack(Player player, boolean isAttack, boolean isHandUsed) {
		player.setData(isHandUsed ? LibAttachments.IS_GUN_ATTACK_MAIN_HAND : LibAttachments.IS_GUN_ATTACK_OFF_HAND, isAttack);
	}

	/**
	 * 获取指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 指定手的蓄力值
	 */
	public static int getChargeUpValue(Player player, InteractionHand handUsed) {
		return getChargeUpValue(player, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 获取指定手的蓄力值
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 指定手的蓄力值
	 */
	public static int getChargeUpValue(Player player, boolean isHandUsed) {
		return player.getData(isHandUsed ? LibAttachments.GUN_CHARGE_UP_TICK_MAIN_HAND : LibAttachments.GUN_CHARGE_UP_TICK_OFF_HAND);
	}

	/**
	 * 设置指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param newValue 新的蓄力值
	 * @param handUsed 使用的手（主手或副手）
	 */
	public static void setChargeUpValue(Player player, int newValue, InteractionHand handUsed) {
		setChargeUpValue(player, newValue, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 设置指定手的蓄力值
	 *
	 * @param player     玩家对象
	 * @param newValue   新的蓄力值
	 * @param isHandUsed 是否为主手
	 */
	public static void setChargeUpValue(Player player, int newValue, boolean isHandUsed) {
		player.setData(isHandUsed ? LibAttachments.GUN_CHARGE_UP_TICK_MAIN_HAND : LibAttachments.GUN_CHARGE_UP_TICK_OFF_HAND,
			Math.max(0, Math.min(newValue, getMaxChargeUpValue(player, isHandUsed))));
	}

	/**
	 * 获取指定手的最大蓄力值
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 指定手的最大蓄力值
	 */
	public static int getMaxChargeUpValue(Player player, boolean isHandUsed) {
		return (int) (1.0f / getAttackSpeed(player, isHandUsed) * 20.0f);
	}

	/**
	 * 获取指定手的攻击速度
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 指定手的攻击速度
	 */
	public static double getAttackSpeed(Player player, boolean isHandUsed) {
		return player.getAttributeValue(Attributes.ATTACK_SPEED) + player.getAttributeValue(isHandUsed ? LibAttributes.ATTACK_SPEED_MAIN_HAND : LibAttributes.ATTACK_SPEED_OFF_HAND);
	}

	/**
	 * 修改指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param value    要增加的蓄力值
	 * @param handUsed 使用的手（主手或副手）
	 */
	public static void modifyChargeUpValue(Player player, int value, InteractionHand handUsed) {
		modifyChargeUpValue(player, value, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 修改指定手的蓄力值
	 *
	 * @param player   玩家对象
	 * @param value    要增加的蓄力值
	 * @param handUsed 使用的手（主手或副手）
	 */
	public static void modifyChargeUpValue(Player player, int value, boolean handUsed) {
		setChargeUpValue(player, getChargeUpValue(player, handUsed) + value, handUsed);
	}

	/**
	 * 重置指定手的蓄力值为0
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 */
	public static void resetChargeUp(Player player, InteractionHand handUsed) {
		resetChargeUp(player, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 重置指定手的蓄力值为0
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 */
	public static void resetChargeUp(Player player, boolean isHandUsed) {
		setChargeUpValue(player, 0, isHandUsed);
	}

	/**
	 * 获取指定手的蓄力百分比
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 蓄力百分比，范围在0.0f到1.0f之间
	 */
	public static float getChargeUpPercentage(Player player, InteractionHand handUsed) {
		return getChargeUpPercentage(player, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 获取指定手的蓄力百分比
	 *
	 * @param player     玩家对象
	 * @param isHandUsed 是否为主手
	 * @return 蓄力百分比，范围在0.0f到1.0f之间
	 */
	public static float getChargeUpPercentage(Player player, boolean isHandUsed) {
		return Math.clamp((float) getChargeUpValue(player, isHandUsed) / getMaxChargeUpValue(player, isHandUsed), 0.0f, 1.0f);
	}

	/**
	 * 设置指定手的蓄力百分比
	 *
	 * @param player   玩家对象
	 * @param newValue 新的蓄力百分比
	 * @param handUsed 使用的手（主手或副手）
	 */
	public static void setChargeUpPercentage(Player player, float newValue, InteractionHand handUsed) {
		setChargeUpPercentage(player, newValue, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 设置指定手的蓄力百分比
	 *
	 * @param player     玩家对象
	 * @param newValue   新的蓄力百分比
	 * @param isHandUsed 是否为主手
	 */
	public static void setChargeUpPercentage(Player player, float newValue, boolean isHandUsed) {
		int maxChargeUpValue = getMaxChargeUpValue(player, isHandUsed);
		if (maxChargeUpValue <= 0) {
			return;
		}
		setChargeUpValue(player, (int) Math.clamp(newValue / maxChargeUpValue, 0.0f, 1.0f), isHandUsed);
	}

	/**
	 * 修改指定手的蓄力百分比
	 *
	 * @param player     玩家对象
	 * @param value      要增加的蓄力百分比
	 * @param isHandUsed 是否为主手
	 */
	public static void modifyChargeUpPercentage(Player player, float value, boolean isHandUsed) {
		setChargeUpPercentage(player, getChargeUpPercentage(player, isHandUsed) + value, isHandUsed);
	}

	/**
	 * 修改指定手的蓄力百分比
	 *
	 * @param player   玩家对象
	 * @param value    要增加的蓄力百分比
	 * @param handUsed 使用的手（主手或副手）
	 */
	public static void modifyChargeUpPercentage(Player player, float value, InteractionHand handUsed) {
		modifyChargeUpPercentage(player, value, handUsed == InteractionHand.MAIN_HAND);
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
	public static int getMaxChargeUpValue(Player player, InteractionHand handUsed) {
		return getMaxChargeUpValue(player, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 获取指定手的攻击速度
	 *
	 * @param player   玩家对象
	 * @param handUsed 使用的手（主手或副手）
	 * @return 指定手的攻击速度
	 */
	private static double getAttackSpeed(Player player, InteractionHand handUsed) {
		return getAttackSpeed(player, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 检查生物是否持有枪械武器
	 *
	 * @param livingEntity 生物对象
	 * @return 如果持有枪械武器则返回true，否则返回false
	 */
	public static boolean is(LivingEntity livingEntity) {
		return livingEntity.getMainHandItem().getItem() instanceof IGunWeapon || livingEntity.getOffhandItem().getItem() instanceof IGunWeapon;
	}

	/**
	 * 检查指定手是否持有枪械武器
	 *
	 * @param livingEntity 生物对象
	 * @param handUsed     使用的手（主手或副手）
	 * @return 如果指定手持有枪械武器则返回true，否则返回false
	 */
	public static boolean is(LivingEntity livingEntity, InteractionHand handUsed) {
		return is(livingEntity, handUsed == InteractionHand.MAIN_HAND);
	}

	/**
	 * 检查指定手是否持有枪械武器
	 *
	 * @param livingEntity 生物对象
	 * @param isHandUsed   是否为主手
	 * @return 如果指定手持有枪械武器则返回true，否则返回false
	 */
	public static boolean is(LivingEntity livingEntity, boolean isHandUsed) {
		return (isHandUsed ? livingEntity.getMainHandItem().getItem() : livingEntity.getOffhandItem().getItem()) instanceof IGunWeapon;
	}
}
