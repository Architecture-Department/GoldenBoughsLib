package architecture.goldenboughs_lib.init.tag

import architecture.goldenboughs_lib.util.LibUtil.modRl
import architecture.goldenboughs_lib.util.LibUtil.rlOf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object LibBlockTags {
	private fun createTag(name: String): TagKey<Block?> {
		return createTag(modRl(name))
	}

	private fun createTag(location: ResourceLocation): TagKey<Block?> {
		return BlockTags.create(location)
	}

	private fun createCTag(name: String): TagKey<Block?> {
		return createTag(rlOf("c", name))
	}

	private fun createMcTag(name: String): TagKey<Block?> {
		return createTag(ResourceLocation.withDefaultNamespace(name))
	}
}
