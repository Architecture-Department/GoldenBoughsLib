package architecture.goldenboughs_lib.api.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

// TODO 待完成
public interface IPayerItemWhellScroll {
	/**
	 * 滚轮
	 *
	 * @return 返回false表示通过滚轮切换物品
	 */
	default boolean onWhellScroll(Player player, InteractionHand hand, int scrollAmount) {
		return false;
	}
}
