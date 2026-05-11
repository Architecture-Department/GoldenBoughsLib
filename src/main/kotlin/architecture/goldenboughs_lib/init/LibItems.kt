package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.api.world.item.IMeleeEgoWeaponItem
import architecture.goldenboughs_lib.common.item.ChaosKnifeItem
import architecture.goldenboughs_lib.common.item.CreativeRationalityToolItem
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function

object LibItems {
	@JvmField
	val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(Lib.ID)

	@JvmField
	val CREATIVE_RATIONALITY_TOOL: DeferredItem<Item> = register(
		"creative_rationality_tool", "理智控制器", ::CreativeRationalityToolItem
	)

	@JvmField
	val CHAOS_SWORD: DeferredItem<Item> = register(
		"chaos_sword", "混沌刃"
	) {
		ChaosKnifeItem(it, IMeleeEgoWeaponItem.Builder().damage(7f).attackSpeed(-1.4f))
	}

	private fun register(name: String, zhName: String, properties: Item.Properties): DeferredItem<Item> {
		return register(name, zhName, ::Item, properties)
	}

	private fun <I : Item> register(
		name: String, zhName: String, item: Function<Item.Properties, out I>, properties: Item.Properties
	): DeferredItem<I> {
		val deferredItem = REGISTRY.registerItem(name, item, properties)
		LibZhCn.addI18nItemText(zhName, deferredItem)
		return deferredItem
	}

	private fun register(
		name: String, zhName: String, item: Function<Item.Properties, out Item>
	): DeferredItem<Item> {
		return register(name, zhName, item, Item.Properties())
	}
}
