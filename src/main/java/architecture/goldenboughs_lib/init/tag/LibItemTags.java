package architecture.goldenboughs_lib.init.tag;

import architecture.goldenboughs_lib.core.Lib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class LibItemTags {
	/**
	 * E.G.O
	 */
	public static final TagKey<Item> EGO = createTag("ego");

	private static TagKey<Item> createTag(String name) {
		return createTag(Lib.modRl(name));
	}

	private static TagKey<Item> createTag(ResourceLocation location) {
		return ItemTags.create(location);
	}

	private static TagKey<Item> createCTag(String name) {
		return createTag(ResourceLocation.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Item> createMcTag(String name) {
		return createTag(ResourceLocation.withDefaultNamespace(name));
	}
}
