package architecture.goldenboughs_lib.init.tag

import architecture.goldenboughs_lib.util.LibUtil.modRl
import architecture.goldenboughs_lib.util.LibUtil.rlOf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object LibItemTags {
	/**
	 * E.G.O
	 */
	@JvmField
	val EGO: TagKey<Item?> = createTag("ego")

	private fun createTag(name: String): TagKey<Item?> {
		return createTag(modRl(name))
	}

	private fun createTag(location: ResourceLocation): TagKey<Item?> {
		return ItemTags.create(location)
	}

	private fun createCTag(name: String): TagKey<Item?> {
		return createTag(rlOf("c", name))
	}

	private fun createMcTag(name: String): TagKey<Item?> {
		return createTag(ResourceLocation.withDefaultNamespace(name))
	}
}
