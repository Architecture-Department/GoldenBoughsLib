package architecture.goldenboughs_lib.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public final class ItemBuilderUtil {
	public static void addAttributeModifier(final ItemAttributeModifiers.Builder builder, final Holder<Attribute> attributeHolder, final ResourceLocation id, final double value, final AttributeModifier.Operation operation, EquipmentSlotGroup slot) {
		if (value == 0) {
			return;
		}
		builder.add(attributeHolder, new AttributeModifier(id, value, operation), slot);
	}
}
