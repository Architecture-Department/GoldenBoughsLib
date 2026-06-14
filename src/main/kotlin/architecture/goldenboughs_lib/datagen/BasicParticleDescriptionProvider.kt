package architecture.goldenboughs_lib.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider

abstract class BasicParticleDescriptionProvider(output: PackOutput, fileHelper: ExistingFileHelper) :
	ParticleDescriptionProvider(output, fileHelper)