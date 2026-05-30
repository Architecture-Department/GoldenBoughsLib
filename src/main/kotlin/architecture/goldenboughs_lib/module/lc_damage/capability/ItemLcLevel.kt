package architecture.goldenboughs_lib.module.lc_damage.capability

import architecture.goldenboughs_lib.module.lc_damage.api.LcLevel
import net.minecraft.world.item.ItemStack

/**
 * 一些预制对象
 */
enum class ItemLcLevel : IItemLcLevel {
	NULL {
		override fun getLcLevel(stack: ItemStack): LcLevel? {
			return null
		}
	},
	ZAYIN {
		override fun getLcLevel(stack: ItemStack): LcLevel {
			return LcLevel.ZAYIN
		}
	},
	TETH {
		override fun getLcLevel(stack: ItemStack): LcLevel {
			return LcLevel.TETH
		}
	},
	HE {
		override fun getLcLevel(stack: ItemStack): LcLevel {
			return LcLevel.HE
		}
	},
	WAW {
		override fun getLcLevel(stack: ItemStack): LcLevel {
			return LcLevel.WAW
		}
	},
	ALEPH {
		override fun getLcLevel(stack: ItemStack): LcLevel {
			return LcLevel.ALEPH
		}
	}
}
