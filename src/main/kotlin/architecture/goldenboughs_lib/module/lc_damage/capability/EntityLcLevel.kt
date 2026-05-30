package architecture.goldenboughs_lib.module.lc_damage.capability

import architecture.goldenboughs_lib.module.lc_damage.api.LcLevel

/**
 * 一些预制对象
 */
enum class EntityLcLevel : IEntityLcLevel {
	NULL {
		override fun getLcLevel(): LcLevel? {
			return null
		}
	},
	ZAYIN {
		override fun getLcLevel(): LcLevel {
			return LcLevel.ZAYIN
		}
	},
	TETH {
		override fun getLcLevel(): LcLevel {
			return LcLevel.TETH
		}
	},
	HE {
		override fun getLcLevel(): LcLevel {
			return LcLevel.HE
		}
	},
	WAW {
		override fun getLcLevel(): LcLevel {
			return LcLevel.WAW
		}
	},
	ALEPH {
		override fun getLcLevel(): LcLevel {
			return LcLevel.ALEPH
		}
	}
}
