package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class DatagenBlockState extends BlockStateProvider {
	public DatagenBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, GoldenBoughsLib.ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {

	}
}
