package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.api.capability.block.IBlockLcLevel;
import architecture.goldenboughs_lib.api.capability.entity.IEntityLcLevel;
import architecture.goldenboughs_lib.api.capability.item.IItemLcDamageType;
import architecture.goldenboughs_lib.api.capability.item.IItemLcLevel;
import architecture.goldenboughs_lib.api.capability.item.IItemUsageReq;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

public final class LibCapabilitys {
	/**
	 * 物品使用条件
	 */
	public static final ItemCapability<IItemUsageReq, Void> USAGE_REQ_ITEM =
		ItemCapability.createVoid(GoldenBoughsLib.modRl("usage_req_item"), IItemUsageReq.class);

	public static final ItemCapability<IItemLcDamageType, Void> LC_DAMAGE_TYPE_ITEM =
		ItemCapability.createVoid(GoldenBoughsLib.modRl("lobotomy_corporation_damage_type_item"), IItemLcDamageType.class);

	/**
	 * 等级
	 */
	public static class LcLevel {
		public static final ItemCapability<IItemLcLevel, Void> LC_LEVEL_ITEM =
			ItemCapability.createVoid(GoldenBoughsLib.modRl("lobotomy_corporation_level_item"), IItemLcLevel.class);

		public static final BlockCapability<IBlockLcLevel, Void> LC_LEVEL_BLOCK =
			BlockCapability.createVoid(GoldenBoughsLib.modRl("lobotomy_corporation_block_block"), IBlockLcLevel.class);

		public static final EntityCapability<IEntityLcLevel, Void> LC_LEVEL_ENTITY =
			EntityCapability.createVoid(GoldenBoughsLib.modRl("lobotomy_corporation_level_entity"), IEntityLcLevel.class);
	}
}
