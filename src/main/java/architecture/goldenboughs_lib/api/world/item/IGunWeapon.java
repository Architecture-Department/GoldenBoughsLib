package architecture.goldenboughs_lib.api.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 枪械武器接口
 * 定义了枪械的基本行为和功能，包括普通射击、瞄准射击等操作
 * 该接口允许实现类自定义枪械的射击逻辑、瞄准行为和相关条件判断
 */
public interface IGunWeapon {
	/**
	 * 判断副手是否可以使用该枪械攻击
	 *
	 * @return 如果副手可以使用该枪械攻击则返回true，否则返回false
	 */
	boolean isOffHandShoot(@NotNull Player player, @NotNull ItemStack stack);

	/**
	 * 获取枪械射击执行的延迟时间
	 *
	 * @return 射击执行的延迟时间（以游戏刻为单位）
	 */
	int gunShootExecuteTick(@NotNull Player player, @NotNull ItemStack stack, @NotNull InteractionHand handUsed);

	/**
	 * 执行枪械射击操作
	 * 在手持该物品时按攻击键触发
	 *
	 * @return 返回false则不在服务器执行射击逻辑
	 */
	boolean gunShoot(@NotNull Player player, @NotNull ItemStack stack, @NotNull InteractionHand handUsed);

	/**
	 * 判断玩家是否可以使用该枪械进行瞄准
	 *
	 * @return 如果可以瞄准则返回true，否则返回false
	 */
	boolean isGunAim(Player player, ItemStack itemStack);

	// TODO 待实装 瞄准状态下移动

	/**
	 * 判断玩家在瞄准状态下是否可以移动
	 *
	 * @return 如果瞄准时可以移动则返回true，否则返回false
	 */
	boolean isGunAimMove(Player player, ItemStack itemStack);

	/**
	 * 执行瞄准射击操作
	 * 在使用该物品时同时按住使用键和攻击键触发
	 *
	 * @return 返回false则不在服务器执行瞄准射击逻辑
	 */
	boolean gunAimShoot(@NotNull Player player, @NotNull ItemStack stack, @NotNull InteractionHand handUsed);
}
