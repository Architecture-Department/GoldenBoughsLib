package architecture.goldenboughs_lib.init.tag;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class LibBlockTags {

	private static TagKey<Block> createTag(String name) {
		return createTag(GoldenBoughsLib.modRl(name));
	}

	private static TagKey<Block> createTag(ResourceLocation location) {
		return BlockTags.create(location);
	}

	private static TagKey<Block> createCTag(String name) {
		return createTag(ResourceLocation.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Block> createMcTag(String name) {
		return createTag(ResourceLocation.withDefaultNamespace(name));
	}
}
