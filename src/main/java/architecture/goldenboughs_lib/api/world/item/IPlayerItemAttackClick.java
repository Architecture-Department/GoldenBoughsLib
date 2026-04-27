package architecture.goldenboughs_lib.api.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * 左键->攻击键
 * <p>
 * 右键->使用键
 */
public interface IPlayerItemAttackClick {

	/**
	 * 点击
	 */
	default void onAttackClick(Player player, InteractionHand hand) {

	}

	/**
	 * 松开点击
	 */
	default void onAttackClickRelease(Player player, InteractionHand hand) {

	}
}
