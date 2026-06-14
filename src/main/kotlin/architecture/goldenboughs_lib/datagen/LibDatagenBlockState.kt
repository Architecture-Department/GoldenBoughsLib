package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class LibDatagenBlockState(output: PackOutput, exFileHelper: ExistingFileHelper) :
	BlockStateProvider(output, LibUtil.ID, exFileHelper) {
	override fun registerStatesAndModels() {
	}
}
