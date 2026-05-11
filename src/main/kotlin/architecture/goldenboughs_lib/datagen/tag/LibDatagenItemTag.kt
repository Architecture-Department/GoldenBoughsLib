package architecture.goldenboughs_lib.datagen.tag

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.tag.LibItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem
import java.util.concurrent.CompletableFuture

class LibDatagenItemTag(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	blockTags: CompletableFuture<TagLookup<Block>>,
	existingFileHelper: ExistingFileHelper
) : ItemTagsProvider(output, lookupProvider, blockTags, Lib.ID, existingFileHelper) {
	override fun addTags(provider: HolderLookup.Provider) {
		tag(LibItemTags.EGO)
	}

	private fun addSet(tag: TagKey<Item>, set: MutableSet<DeferredItem<out Item>>): IntrinsicTagAppender<Item> {
		return tag(tag).add(*set.stream().map { it.get() }
			.toArray() as Array<Item>)
	}
}
