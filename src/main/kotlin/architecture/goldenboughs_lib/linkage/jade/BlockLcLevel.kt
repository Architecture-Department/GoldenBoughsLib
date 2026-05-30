package architecture.goldenboughs_lib.linkage.jade

import architecture.goldenboughs_lib.module.lc_damage.util.LcLevelUtil.getLevel
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import java.util.*

enum class BlockLcLevel : IBlockComponentProvider {
	INSTANCE;

	override fun getUid(): ResourceLocation {
		return ModJadePlugin.BLOCK_LC_LEVEL
	}

	override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig?) {
		val level = getLevel(accessor.level, accessor.position) ?: return
		tooltip.add(1, Component.literal(level.levelName.uppercase(Locale.getDefault())).withColor(level.colourValue))
	}
}
