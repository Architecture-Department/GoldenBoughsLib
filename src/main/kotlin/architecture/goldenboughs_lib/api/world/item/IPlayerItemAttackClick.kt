package architecture.goldenboughs_lib.api.world.item

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player

/**
 * 左键->攻击键
 * 
 * 
 * 右键->使用键
 */
@Deprecated("攻击输入由 RCF 的 AttackInputEvents 全权接管，不再使用此接口")
@AllOpe
interface IPlayerItemAttackClick {
	/**
	 * 点击
	 */
	fun onAttackClick(player: Player, hand: InteractionHand) {
	}

	/**
	 * 松开点击
	 */
	fun onAttackClickRelease(player: Player, hand: InteractionHand) {
	}
}
