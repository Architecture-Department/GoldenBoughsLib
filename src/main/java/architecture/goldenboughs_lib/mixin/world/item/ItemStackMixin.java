package architecture.goldenboughs_lib.mixin.world.item;

import architecture.goldenboughs_lib.event.client.AddItemDataComponentTooltipEvent;
import architecture.goldenboughs_lib.init.LibDataComponentTypes;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder, MutableDataComponentHolder, IItemStackExtension {
	@Shadow
	public abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> component, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag);

	@WrapOperation(method = "addToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/TooltipProvider;addToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", ordinal = 0))
	private <T extends TooltipProvider> void goldenboughs_lib$addItemDataComponentTooltipEvent(
		TooltipProvider instance, Item.TooltipContext context,
		Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag,
		Operation<Void> original,
		@Local(argsOnly = true) DataComponentType<T> component
	) {
		ItemStack goldenboughs_lib$itemStack = getgoldenboughs_lib$itemStack();
		var event = new AddItemDataComponentTooltipEvent.Up<>(goldenboughs_lib$itemStack, component, context, tooltipAdder, tooltipFlag);
		if (event.isCanceled()) {
			return;
		}
		original.call(instance, context, tooltipAdder, tooltipFlag);
		new AddItemDataComponentTooltipEvent.Down<>(goldenboughs_lib$itemStack, component, context, tooltipAdder, tooltipFlag);
	}

	@Unique
	@NotNull
	private ItemStack getgoldenboughs_lib$itemStack() {
		return (ItemStack) (Object) this;
	}

	@Definition(id = "has", method = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z")
	@Definition(id = "HIDE_ADDITIONAL_TOOLTIP", field = "HIDE_ADDITIONAL_TOOLTIP", type = DataComponents.class)
	@Expression("this.has(HIDE_ADDITIONAL_TOOLTIP)")
	@ModifyExpressionValue(method = "getTooltipLines", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean goldenboughs_lib$addTooltip(
		boolean original, @Local Consumer<Component> instance,
		@Local(argsOnly = true) Item.TooltipContext tooltipContext,
		@Local(argsOnly = true) TooltipFlag tooltipFlag
	) {
		// 添加物品使用要求
		addToTooltip(LibDataComponentTypes.ITEM_VIRTUE_USAGE_REQ, tooltipContext, instance, tooltipFlag);
		return original;
	}
}
