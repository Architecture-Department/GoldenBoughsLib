package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.api.world.item.IMeleeEgoWeaponItem;
import architecture.goldenboughs_lib.common.item.ChaosKnifeItem;
import architecture.goldenboughs_lib.common.item.CreativeRationalityToolItem;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class LibItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(GoldenBoughsLib.ID);

	public static final DeferredItem<Item> CREATIVE_RATIONALITY_TOOL = register(
		"creative_rationality_tool", "理智控制器", CreativeRationalityToolItem::new);
	public static final DeferredItem<Item> CHAOS_SWORD = register(
		"chaos_sword", "混沌刃", properties -> new ChaosKnifeItem(properties, new IMeleeEgoWeaponItem.Builder()
			.damage(7)
			.attackSpeed(-1.4F)));

	@NotNull
	private static DeferredItem<Item> register(String name, String zhName, Item.Properties properties) {
		return register(name, zhName, Item::new, properties);
	}

	@NotNull
	private static <I extends Item> DeferredItem<I> register(String name, String zhName,
	                                                         Function<Item.Properties, ? extends I> item,
	                                                         Item.Properties properties) {
		DeferredItem<I> deferredItem = REGISTRY.registerItem(name, item, properties);
		LibZhCn.addI18nItemText(zhName, deferredItem);
		return deferredItem;
	}

	@NotNull
	private static DeferredItem<Item> register(String name, String zhName, Function<Item.Properties, ? extends Item> item) {
		return register(name, zhName, item, new Item.Properties());
	}
}
