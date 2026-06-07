package architecture.goldenboughs_lib.config

import architecture.goldenboughs_lib.api.BasicConfig
import architecture.goldenboughs_lib.core.LibConstants
import net.neoforged.neoforge.common.ModConfigSpec

class LibCommonConfig internal constructor(builder: ModConfigSpec.Builder) : BasicConfig(LibConstants.ID, builder)
