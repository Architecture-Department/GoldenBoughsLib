package architecture.goldenboughs_lib.datagen.tag;

import architecture.goldenboughs_lib.core.Lib;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public final class LibDatagenBlockTag extends BlockTagsProvider {
	public LibDatagenBlockTag(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Lib.ID, existingFileHelper);
	}

	@Override
	public void addTags(final HolderLookup.Provider provider) {

	}
}
