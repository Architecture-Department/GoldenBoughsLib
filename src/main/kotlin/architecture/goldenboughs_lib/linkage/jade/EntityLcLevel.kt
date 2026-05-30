package architecture.goldenboughs_lib.linkage.jade

import architecture.goldenboughs_lib.module.lc_damage.util.LcLevelUtil.getLevel
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.item.ItemEntity
import snownee.jade.api.EntityAccessor
import snownee.jade.api.IEntityComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import java.util.*

enum class EntityLcLevel : IEntityComponentProvider {
	INSTANCE;

	override fun appendTooltip(iTooltip: ITooltip, entityAccessor: EntityAccessor, iPluginConfig: IPluginConfig?) {
		val entity = entityAccessor.entity
		var level = entity.getLevel()
		if (entity is ItemEntity) {
			level = entity.item.getLevel()
		}
		if (level == null) {
			return
		}
		iTooltip.add(1, Component.literal(level.levelName.uppercase(Locale.getDefault())).withColor(level.colourValue))
	}

	override fun getUid(): ResourceLocation {
		return ModJadePlugin.ENTITY_LC_LEVEL
	}
}
