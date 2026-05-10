package architecture.goldenboughs_lib.api.capability.block

import architecture.goldenboughs_lib.api.LcLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

/**
 * 一些预制对象
 */
enum class BlockLcLevel : IBlockLcLevel {
	NULL {
		override fun getLcLevel(level: Level, pos: BlockPos): LcLevel? {
			return null
		}
	},
	ZAYIN {
		override fun getLcLevel(level: Level, pos: BlockPos): LcLevel {
			return LcLevel.ZAYIN
		}
	},
	TETH {
		override fun getLcLevel(level: Level, pos: BlockPos): LcLevel {
			return LcLevel.TETH
		}
	},
	HE {
		override fun getLcLevel(level: Level, pos: BlockPos): LcLevel {
			return LcLevel.HE
		}
	},
	WAW {
		override fun getLcLevel(level: Level, pos: BlockPos): LcLevel {
			return LcLevel.WAW
		}
	},
	ALEPH {
		override fun getLcLevel(level: Level, pos: BlockPos): LcLevel {
			return LcLevel.ALEPH
		}
	}
}
