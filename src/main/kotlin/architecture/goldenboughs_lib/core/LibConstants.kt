package architecture.goldenboughs_lib.core

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem

object LibConstants {
	// 例如道具类的物品等没有特殊分类的物品，例如：安吉拉的图书馆
	@JvmField
	val EGO: MutableSet<DeferredItem<out Item>> = HashSet()
}
