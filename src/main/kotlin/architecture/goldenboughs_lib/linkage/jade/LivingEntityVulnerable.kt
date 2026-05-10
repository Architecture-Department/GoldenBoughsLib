package architecture.goldenboughs_lib.linkage.jade

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.core.GoldenBoughsLib
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import snownee.jade.JadeInternals
import snownee.jade.api.EntityAccessor
import snownee.jade.api.IEntityComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.api.ui.IElementHelper

enum class LivingEntityVulnerable : IEntityComponentProvider {
	INSTANCE;

	override fun appendTooltip(iTooltip: ITooltip, entityAccessor: EntityAccessor, iPluginConfig: IPluginConfig?) {
		if (entityAccessor.entity !is LivingEntity) {
			return
		}
		val entity = entityAccessor.entity as LivingEntity
		val elements = JadeInternals.getElementHelper()
		iTooltip.add(Component.translatable(ATTRIBUTE_DESCRIPTION_KEY))
		add(iTooltip, PHYSICS_KEY, "physics8x", LcDamageType.PHYSICS, entity, elements)
		add(iTooltip, SPIRIT_KEY, "spirit8x", LcDamageType.SPIRIT, entity, elements)
		add(iTooltip, EROSION_KEY, "erosion8x", LcDamageType.EROSION, entity, elements)
		add(iTooltip, THE_SOUL_KEY, "the_soul8x", LcDamageType.THE_SOUL, entity, elements)
	}

	override fun getUid(): ResourceLocation {
		return ModJadePlugin.ENTITY_LC_VULNERABLE
	}

	companion object {
		@JvmField
		val ATTRIBUTE_DESCRIPTION_KEY: String = GoldenBoughsLib.modRlText("entity.attribute_description")

		@JvmField
		val PHYSICS_KEY: String = GoldenBoughsLib.modRlText("entity.attribute_description.physics")

		@JvmField
		val SPIRIT_KEY: String = GoldenBoughsLib.modRlText("entity.attribute_description.spirit")

		@JvmField
		val EROSION_KEY: String = GoldenBoughsLib.modRlText("entity.attribute_description.erosion")

		@JvmField
		val THE_SOUL_KEY: String = GoldenBoughsLib.modRlText("entity.attribute_description.the_soul")

		@JvmStatic
		private fun add(
			iTooltip: ITooltip,
			key: String,
			spriteRl: String,
			damageType: LcDamageType,
			entity: LivingEntity,
			elements: IElementHelper
		) {
			iTooltip.add(elements.sprite(GoldenBoughsLib.modRl(spriteRl), 8, 8))
			val vulnerable: Holder<Attribute> = damageType.vulnerable
			val text =
				if (hasAttribute(entity, vulnerable)) entity.getAttributeValue(vulnerable) else vulnerable.value().defaultValue
			val colour = damageType.colourValue
			iTooltip.append(Component.translatable(key).append(String.format(" %.2f", text)).withColor(colour))
		}

		@JvmStatic
		private fun hasAttribute(entity: LivingEntity, attribute: Holder<Attribute>): Boolean {
			return entity.attributes.hasAttribute(attribute)
		}
	}
}
