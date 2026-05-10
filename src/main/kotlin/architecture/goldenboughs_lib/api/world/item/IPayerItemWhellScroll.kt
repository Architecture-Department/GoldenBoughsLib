package architecture.goldenboughs_lib.api.world.item

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player

// TODO 待完成
@AllOpe
interface IPayerItemWhellScroll {
	/**
	 * 滚轮
	 * 
	 * @return 返回false表示通过滚轮切换物品
	 */
	fun onWhellScroll(player: Player, hand: InteractionHand, scrollAmount: Int): Boolean {
		return false
	}
}
