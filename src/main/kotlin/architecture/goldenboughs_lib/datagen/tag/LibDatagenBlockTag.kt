package architecture.goldenboughs_lib.datagen.tag

import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class LibDatagenBlockTag(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : BlockTagsProvider(output, lookupProvider, LibUtil.ID, existingFileHelper) {
	public override fun addTags(provider: HolderLookup.Provider) {
	}
}
