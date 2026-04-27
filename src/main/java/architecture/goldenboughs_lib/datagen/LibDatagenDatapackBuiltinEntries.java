package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 创建一个数据包内置条目
 */
public final class LibDatagenDatapackBuiltinEntries extends DatapackBuiltinEntriesProvider {

	public LibDatagenDatapackBuiltinEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder) {
		super(output, registries, datapackEntriesBuilder, Set.of(GoldenBoughsLib.ID));
	}
}
