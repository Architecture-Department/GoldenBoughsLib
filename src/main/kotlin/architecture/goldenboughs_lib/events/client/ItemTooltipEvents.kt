package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.util.LcLevelUtil.getLevel
import net.minecraft.network.chat.Component
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.GatherEffectScreenTooltipsEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import java.util.*
import architecture.goldenboughs_lib.core.LibConstants

/**
 * 物品提示/描述处理
 */
@EventBusSubscriber(modid = LibConstants.ID)
object ItemTooltipEvents {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun itemTooltip(event: ItemTooltipEvent) {
		val itemStack = event.itemStack
		val toolTip = event.toolTip
		val size = toolTip.size

		// 添加物品等级 ToolTip
		val lcLevel = itemStack.getLevel()
		val upperCase: String
		val colourValue: Int
		if (lcLevel != null) {
			upperCase = lcLevel.levelName.uppercase(Locale.getDefault())
			colourValue = lcLevel.colourValue
		} else {
			upperCase = "???"
			colourValue = 0xbd7118
		}
		val component = Component.literal(upperCase).withColor(colourValue)
		toolTip.add(Math.clamp(size.toLong(), 0, 1), component)
	}

	@SubscribeEvent
	fun gatherEffectScreenTooltipsEvent(event: GatherEffectScreenTooltipsEvent) {
	}
}
