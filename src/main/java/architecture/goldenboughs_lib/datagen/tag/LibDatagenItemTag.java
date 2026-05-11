package architecture.goldenboughs_lib.datagen.tag;

import architecture.goldenboughs_lib.core.Lib;
import architecture.goldenboughs_lib.init.tag.LibItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class LibDatagenItemTag extends ItemTagsProvider {
	public LibDatagenItemTag(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final CompletableFuture<TagLookup<Block>> blockTags, final ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTags, Lib.ID, existingFileHelper);
	}

	@Override
	protected void addTags(final HolderLookup.Provider provider) {
		tag(LibItemTags.EGO);
	}

	private @NotNull IntrinsicTagAppender<Item> addSet(TagKey<Item> tag, @NotNull Set<DeferredItem<? extends Item>> set) {
		return tag(tag).add(set.stream().map(DeferredHolder::get).toArray(Item[]::new));
	}
}
