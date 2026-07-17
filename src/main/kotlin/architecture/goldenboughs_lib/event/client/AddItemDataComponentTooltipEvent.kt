package architecture.goldenboughs_lib.event.client

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent
import java.util.function.Consumer

/**
 * 在[ItemStack.addToTooltip]中调用
 * 
 * @param <T> TooltipProvider
</T> */
@AllOpen
abstract class AddItemDataComponentTooltipEvent<T : TooltipProvider>(
	val itemStack: ItemStack,
	val component: DataComponentType<T>,
	val context: Item.TooltipContext,
	val tooltipAdder: Consumer<Component>,
	val tooltipFlag: TooltipFlag
) : Event() {
	/**
	 * 在添加[DataComponentType]提示信息的之前调用 可取消本次添加 implements ICancellableEvent
	 */
	class Up<T : TooltipProvider>(
		itemStack: ItemStack, component: DataComponentType<T>, context: Item.TooltipContext,
		tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag
	) : AddItemDataComponentTooltipEvent<T>(itemStack, component, context, tooltipAdder, tooltipFlag),
		ICancellableEvent

	/**
	 * 在添加[DataComponentType]提示信息的之后调用
	 */
	class Down<T : TooltipProvider>(
		itemStack: ItemStack, component: DataComponentType<T>, context: Item.TooltipContext,
		tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag
	) : AddItemDataComponentTooltipEvent<T>(itemStack, component, context, tooltipAdder, tooltipFlag)
}
