package architecture.goldenboughs_lib.events.registry.client

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibDataComponentTypes
import architecture.goldenboughs_lib.init.LibItems
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

/**
 * 物品渲染附加
 */
@EventBusSubscriber(modid = LibConstants.ID, value = [Dist.CLIENT])
object LibItemPropertyRenderersRegistrar {
	@JvmField
	val MODE_BOOLEAN: ResourceLocation = LibConstants.modRl("current_lobotomy.mode_boolean")

	@JvmField
	val CURRENT_LC_DAMAGE_TYPE: ResourceLocation = LibConstants.modRl("current_lobotomy.corporation_damage_type")

	@JvmField
	val PROPERTY_MODE_BOOLEAN: ClampedItemPropertyFunction =
		ClampedItemPropertyFunction { itemStack, clientLevel, livingEntity, i ->
			if (itemStack.get(LibDataComponentTypes.MODE_BOOLEAN) == true) 1.0f else 0.0f
		}

	/**
	 * 注册物品渲染附加
	 */
	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {
		event.enqueueWork {
			createProperties(LibItems.CREATIVE_RATIONALITY_TOOL.asItem(), MODE_BOOLEAN, PROPERTY_MODE_BOOLEAN)
			createProperties(
				LibItems.CHAOS_SWORD.asItem(),
				CURRENT_LC_DAMAGE_TYPE
			) { itemStack, clientLevel, livingEntity, i ->
				val t = itemStack.get(LibDataComponentTypes.LC_DAMAGE_TYPE)
				if (t == null) 0f else when (t.lcDamageType) {
					LcDamageType.PHYSICS -> 0.0f
					LcDamageType.SPIRIT -> 0.1f
					LcDamageType.EROSION -> 0.2f
					LcDamageType.THE_SOUL -> 0.3f
					null -> 0.0f
				}
			}
		}
	}

	@JvmStatic
	private fun createProperties(
		item: Item,
		propertiesName: ResourceLocation,
		propertyFunction: ClampedItemPropertyFunction
	) {
		ItemProperties.register(item, propertiesName, propertyFunction)
	}
}
