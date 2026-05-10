package architecture.goldenboughs_lib.util

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.*
import java.util.function.Predicate
import java.util.function.Supplier

object ItemUtil {
	@SafeVarargs
	@JvmStatic
	fun anyMatchIs(itemStack: ItemStack, vararg itemSupplier: Supplier<out Item>): Boolean {
		return Arrays.stream<Supplier<out Item>>(itemSupplier)
			.anyMatch(Predicate { item: Supplier<out Item> -> itemStack.`is`(item.get()) })
	}

	@SafeVarargs
	@JvmStatic
	fun allMatchIs(itemStack: ItemStack, vararg itemSupplier: Supplier<out Item>): Boolean {
		return Arrays.stream<Supplier<out Item>>(itemSupplier)
			.allMatch(Predicate { item: Supplier<out Item> -> itemStack.`is`(item.get()) })
	}

	@JvmStatic
	fun anyMatchIs(itemStack: ItemStack, vararg item: Item): Boolean {
		return Arrays.stream<Item>(item).anyMatch(Predicate { item: Item -> itemStack.`is`(item) })
	}

	@JvmStatic
	fun allMatchIs(itemStack: ItemStack, vararg item: Item): Boolean {
		return Arrays.stream<Item>(item).allMatch(Predicate { item: Item -> itemStack.`is`(item) })
	}
}
