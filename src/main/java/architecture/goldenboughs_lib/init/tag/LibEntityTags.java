package architecture.goldenboughs_lib.init.tag;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class LibEntityTags {
	/**
	 * 异想体
	 */
	public static final TagKey<EntityType<?>> EGO = createTag("ego");
	//region 考验
	/**
	 * 考验
	 */
	public static final TagKey<EntityType<?>> ORDEALS = createTag("ordeals");
	/**
	 * 考验/紫罗兰
	 */
	public static final TagKey<EntityType<?>> ORDEALS_VIOLET = createTag("ordeals/violet");
	/**
	 * 考验/琥珀色
	 */
	public static final TagKey<EntityType<?>> ORDEALS_AMBER = createTag("ordeals/amber");
	/**
	 * 考验/绿色
	 */
	public static final TagKey<EntityType<?>> ORDEALS_GREEN = createTag("ordeals/green");
	/**
	 * 考验/血色
	 */
	public static final TagKey<EntityType<?>> ORDEALS_CRIMSON = createTag("ordeals/crimson");
	//endregion
	/**
	 * 清道夫
	 */
	public static final TagKey<EntityType<?>> THE_SWEEPERS = createTag("the_sweepers");

	private static TagKey<EntityType<?>> createTag(String name) {
		return createTag(GoldenBoughsLib.modRl(name));
	}

	private static TagKey<EntityType<?>> createTag(ResourceLocation location) {
		return TagKey.create(Registries.ENTITY_TYPE, location);
	}

	private static TagKey<EntityType<?>> createCTag(String name) {
		return createTag(ResourceLocation.fromNamespaceAndPath("c", name));
	}

	private static TagKey<EntityType<?>> createMcTag(String name) {
		return createTag(ResourceLocation.withDefaultNamespace(name));
	}
}
