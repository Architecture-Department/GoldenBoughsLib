package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.core.Lib
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import architecture.goldenboughs_lib.core.LibConstants

class LibDatagenBlockState(output: PackOutput, exFileHelper: ExistingFileHelper) :
	BlockStateProvider(output, LibConstants.ID, exFileHelper) {
	override fun registerStatesAndModels() {
	}
}
