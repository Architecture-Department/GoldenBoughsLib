@file:Suppress("UNCHECKED_CAST")

package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.api.capability.block.IBlockLcLevel
import architecture.goldenboughs_lib.api.capability.entity.IEntityLcLevel
import architecture.goldenboughs_lib.api.capability.item.IItemLcDamageType
import architecture.goldenboughs_lib.api.capability.item.IItemLcLevel
import architecture.goldenboughs_lib.api.capability.item.IItemUsageReq
import architecture.goldenboughs_lib.core.LibConstants.modRl
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import architecture.goldenboughs_lib.core.LibConstants

object LibCapabilitys {
	/**
	 * 物品使用条件
	 */
	@JvmField
	val USAGE_REQ_ITEM: ItemCapability<IItemUsageReq?, Void?> = registerItemCapability("usage_req_item")

	@JvmField
	val LC_DAMAGE_TYPE_ITEM: ItemCapability<IItemLcDamageType?, Void?> =
		registerItemCapability("lobotomy_corporation_damage_type_item")

	/**
	 * 等级
	 */
	object LcLevel {
		@JvmField
		val LC_LEVEL_ITEM: ItemCapability<IItemLcLevel?, Void?> =
			registerItemCapability("lobotomy_corporation_level_item")

		@JvmField
		val LC_LEVEL_BLOCK: BlockCapability<IBlockLcLevel?, Void?> =
			registerBlockCapability("lobotomy_corporation_block_block")

		@JvmField
		val LC_LEVEL_ENTITY: EntityCapability<IEntityLcLevel?, Void?> =
			registerEntityCapability("lobotomy_corporation_level_entity")
	}

	private inline fun <reified T> registerItemCapability(name: String): ItemCapability<T?, Void?> {
		return ItemCapability.createVoid(LibConstants.modRl(name), T::class.java)
	}

	private inline fun <reified T> registerBlockCapability(name: String): BlockCapability<T?, Void?> {
		return BlockCapability.createVoid(LibConstants.modRl(name), T::class.java)
	}

	private inline fun <reified T> registerEntityCapability(name: String): EntityCapability<T?, Void?> {
		return EntityCapability.createVoid(LibConstants.modRl(name), T::class.java)
	}
}
