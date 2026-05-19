package architecture.goldenboughs_lib.util

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.Supplier
import kotlin.reflect.KProperty

operator fun <R, T : R> Supplier<T>.getValue(any: Any?, property: KProperty<*>): T {
	return get()
}

operator fun <T : Block> Supplier<T>.getValue(any: Any?, property: KProperty<*>): T {
	return get()
}

operator fun <T : Item> Supplier<T>.getValue(any: Any?, property: KProperty<*>): T {
	return get()
}

operator fun <R, T : R> Holder<T>.getValue(any: Any?, property: KProperty<*>): T {
	return this.value()
}

operator fun <T : Block> Holder<T>.getValue(any: Any?, property: KProperty<*>): T {
	return this.value()
}

operator fun <T : Item> Holder<T>.getValue(any: Any?, property: KProperty<*>): T {
	return this.value()
}