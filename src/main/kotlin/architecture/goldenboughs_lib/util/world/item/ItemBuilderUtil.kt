package architecture.goldenboughs_lib.util.world.item

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.component.ItemAttributeModifiers

object ItemBuilderUtil {
	@JvmStatic
	fun addAttributeModifier(
		builder: ItemAttributeModifiers.Builder,
		attributeHolder: Holder<Attribute?>,
		id: ResourceLocation,
		value: Double,
		operation: AttributeModifier.Operation,
		slot: EquipmentSlotGroup
	) {
		if (value == 0.0) return
		builder.add(attributeHolder, AttributeModifier(id, value, operation), slot)
	}
}
