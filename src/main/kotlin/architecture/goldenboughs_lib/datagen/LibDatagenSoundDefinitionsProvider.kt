package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper

class LibDatagenSoundDefinitionsProvider(
	output: PackOutput,
	existingFileHelper: ExistingFileHelper
) : BasicSoundDefinitionsProvider(output, LibUtil.ID, existingFileHelper) {
	override fun registerSounds() {
	}
}
