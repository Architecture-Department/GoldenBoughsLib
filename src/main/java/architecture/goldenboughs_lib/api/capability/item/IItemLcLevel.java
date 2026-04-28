package architecture.goldenboughs_lib.api.capability.item;

import architecture.goldenboughs_lib.api.LcLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * 可以通过继承该接口实现自定义LcLevel获取但仍然推荐通过能力系统注册
 */
public interface IItemLcLevel {
	/**
	 * 返回null则不参与等级系统处理
	 */
	@Nullable
	LcLevel getLcLevel(ItemStack stack);
}
