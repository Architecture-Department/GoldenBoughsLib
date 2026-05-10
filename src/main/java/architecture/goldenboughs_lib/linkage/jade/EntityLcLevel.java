package architecture.goldenboughs_lib.linkage.jade;

import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.util.LcLevelUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EntityLcLevel implements IEntityComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
		Entity entity = entityAccessor.getEntity();
		@Nullable LcLevel level = LcLevelUtil.getLevel(entity);
		if (entity instanceof ItemEntity itemEntity) {
			level = LcLevelUtil.getLevel(itemEntity.getItem());
		}
		if (level == null) {
			return;
		}
		iTooltip.add(1, Component.literal(level.getLevelName().toUpperCase()).withColor(level.getColourValue()));
	}

	@Override
	public ResourceLocation getUid() {
		return ModJadePlugin.ENTITY_LC_LEVEL;
	}
}
