package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.core.Lib;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class LibDatagenBlockState extends BlockStateProvider {
	public LibDatagenBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Lib.ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {

	}
}
