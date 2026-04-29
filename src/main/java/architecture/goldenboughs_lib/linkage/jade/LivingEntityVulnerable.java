package architecture.goldenboughs_lib.linkage.jade;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import snownee.jade.JadeInternals;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum LivingEntityVulnerable implements IEntityComponentProvider {
	INSTANCE;

	public static final String ATTRIBUTE_DESCRIPTION_KEY = GoldenBoughsLib.modRlText("entity.attribute_description");
	public static final String PHYSICS_KEY = GoldenBoughsLib.modRlText("entity.attribute_description.physics");
	public static final String SPIRIT_KEY = GoldenBoughsLib.modRlText("entity.attribute_description.spirit");
	public static final String EROSION_KEY = GoldenBoughsLib.modRlText("entity.attribute_description.erosion");
	public static final String THE_SOUL_KEY = GoldenBoughsLib.modRlText("entity.attribute_description.the_soul");

	private static void add(ITooltip iTooltip, String key, String spriteRl, LcDamageType damageType, LivingEntity entity, IElementHelper elements) {
		iTooltip.add(elements.sprite(GoldenBoughsLib.modRl(spriteRl), 8, 8));
		Holder<Attribute> vulnerable = damageType.getVulnerable();
		double text = hasAttribute(entity, vulnerable) ? entity.getAttributeValue(vulnerable) : vulnerable.value().getDefaultValue();
		int colour = damageType.getColourValue();
		iTooltip.append(Component.translatable(key).append(String.format(" %.2f", text)).withColor(colour));
	}

	private static boolean hasAttribute(final LivingEntity entity, Holder<Attribute> attribute) {
		return entity.getAttributes().hasAttribute(attribute);
	}

	@Override
	public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
		if (!(entityAccessor.getEntity() instanceof LivingEntity entity)) {
			return;
		}
		IElementHelper elements = JadeInternals.getElementHelper();
		iTooltip.add(Component.translatable(ATTRIBUTE_DESCRIPTION_KEY));
		add(iTooltip, PHYSICS_KEY, "physics8x", LcDamageType.PHYSICS, entity, elements);
		add(iTooltip, SPIRIT_KEY, "spirit8x", LcDamageType.SPIRIT, entity, elements);
		add(iTooltip, EROSION_KEY, "erosion8x", LcDamageType.EROSION, entity, elements);
		add(iTooltip, THE_SOUL_KEY, "the_soul8x", LcDamageType.THE_SOUL, entity, elements);
	}

	@Override
	public ResourceLocation getUid() {
		return ModJadePlugin.ENTITY_LC_VULNERABLE;
	}
}
