package architecture.goldenboughs_lib.api.capability.item

import architecture.goldenboughs_lib.api.LcLevel
import net.minecraft.world.item.ItemStack

/**
 * 可以通过继承该接口实现自定义LcLevel获取，但仍然推荐通过能力系统注册
 */
interface IItemLcLevel {
	/**
	 * 返回null则不参与等级系统处理
	 */
	fun getLcLevel(stack: ItemStack): LcLevel?
}
