package architecture.goldenboughs_lib.event.client;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import java.util.function.Consumer;

/**
 * 在{@link ItemStack#addToTooltip(DataComponentType, Item.TooltipContext, Consumer, TooltipFlag)}中调用
 *
 * @param <T> TooltipProvider
 */
public abstract class AddItemDataComponentTooltipEvent<T extends TooltipProvider> extends Event {
	private final ItemStack itemStack;
	private final DataComponentType<T> component;
	private final Item.TooltipContext context;
	private final Consumer<Component> tooltipAdder;
	private final TooltipFlag tooltipFlag;

	public AddItemDataComponentTooltipEvent(ItemStack itemStack, DataComponentType<T> component, Item.TooltipContext context,
	                                        Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
		this.itemStack = itemStack;
		this.component = component;
		this.context = context;
		this.tooltipAdder = tooltipAdder;
		this.tooltipFlag = tooltipFlag;
	}

	public DataComponentType<T> getComponent() {
		return component;
	}

	public Item.TooltipContext getContext() {
		return context;
	}

	public Consumer<Component> getTooltipAdder() {
		return tooltipAdder;
	}

	public TooltipFlag getTooltipFlag() {
		return tooltipFlag;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * 在添加{@link DataComponentType}提示信息的之前调用 可取消本次添加 implements ICancellableEvent
	 */
	public static class Up<T extends TooltipProvider> extends AddItemDataComponentTooltipEvent<T> implements ICancellableEvent {
		public Up(ItemStack itemStack, DataComponentType<T> component, Item.TooltipContext context,
		          Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
			super(itemStack, component, context, tooltipAdder, tooltipFlag);
		}
	}

	/**
	 * 在添加{@link DataComponentType}提示信息的之后调用
	 */
	public static class Down<T extends TooltipProvider> extends AddItemDataComponentTooltipEvent<T> {
		public Down(ItemStack itemStack, DataComponentType<T> component, Item.TooltipContext context,
		            Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
			super(itemStack, component, context, tooltipAdder, tooltipFlag);
		}
	}
}
