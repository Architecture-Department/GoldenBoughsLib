package architecture.goldenboughs_lib.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.function.Supplier;

public final class ItemUtil {
	@SafeVarargs
	public static boolean anyMatchIs(ItemStack itemStack, Supplier<? extends Item>... itemSupplier) {
		return Arrays.stream(itemSupplier).anyMatch(item -> itemStack.is(item.get()));
	}

	@SafeVarargs
	public static boolean allMatchIs(ItemStack itemStack, Supplier<? extends Item>... itemSupplier) {
		return Arrays.stream(itemSupplier).allMatch(item -> itemStack.is(item.get()));
	}

	public static boolean anyMatchIs(ItemStack itemStack, Item... item) {
		return Arrays.stream(item).anyMatch(itemStack::is);
	}

	public static boolean allMatchIs(ItemStack itemStack, Item... item) {
		return Arrays.stream(item).allMatch(itemStack::is);
	}
}
