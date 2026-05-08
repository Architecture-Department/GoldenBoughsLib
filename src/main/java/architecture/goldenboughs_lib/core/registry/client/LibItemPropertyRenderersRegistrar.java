package architecture.goldenboughs_lib.core.registry.client;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibDataComponentTypes;
import architecture.goldenboughs_lib.init.LibItems;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 物品渲染附加
 */
@EventBusSubscriber(modid = GoldenBoughsLib.ID, value = Dist.CLIENT)
public final class LibItemPropertyRenderersRegistrar {
	public static final ResourceLocation MODE_BOOLEAN = GoldenBoughsLib.modRl("current_lobotomy.mode_boolean");
	public static final ResourceLocation CURRENT_LC_DAMAGE_TYPE = GoldenBoughsLib.modRl("current_lobotomy.corporation_damage_type");

	public static final ClampedItemPropertyFunction PROPERTY_MODE_BOOLEAN = (itemStack, clientLevel, livingEntity, i) ->
		Boolean.TRUE == itemStack.get(LibDataComponentTypes.MODE_BOOLEAN) ? 1 : 0;

	/**
	 * 注册物品渲染附加
	 */
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			createProperties(LibItems.CREATIVE_RATIONALITY_TOOL.asItem(), MODE_BOOLEAN, PROPERTY_MODE_BOOLEAN);
			createProperties(LibItems.CHAOS_SWORD.asItem(), CURRENT_LC_DAMAGE_TYPE, (itemStack, clientLevel, livingEntity, i) -> {
				LcDamageType.Component t = itemStack.get(LibDataComponentTypes.LC_DAMAGE_TYPE);
				return t == null ? 0 : switch (t.lcDamageType()) {
					case PHYSICS -> 0;
					case SPIRIT -> 0.1F;
					case EROSION -> 0.2F;
					case THE_SOUL -> 0.3F;
					case null -> 0;
				};
			});
		});
	}

	private static void createProperties(Item item, ResourceLocation propertiesName, ClampedItemPropertyFunction propertyFunction) {
		ItemProperties.register(item, propertiesName, propertyFunction);
	}
}
