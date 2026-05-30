@file:Suppress("UNCHECKED_CAST")

package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.api.capability.item.IItemUsageReq
import architecture.goldenboughs_lib.core.LibConstants.modRl
import architecture.goldenboughs_lib.module.lc_damage.capability.IBlockLcLevel
import architecture.goldenboughs_lib.module.lc_damage.capability.IEntityLcLevel
import architecture.goldenboughs_lib.module.lc_damage.capability.IItemLcDamageType
import architecture.goldenboughs_lib.module.lc_damage.capability.IItemLcLevel
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability

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
		return ItemCapability.createVoid(modRl(name), T::class.java)
	}

	private inline fun <reified T> registerBlockCapability(name: String): BlockCapability<T?, Void?> {
		return BlockCapability.createVoid(modRl(name), T::class.java)
	}

	private inline fun <reified T> registerEntityCapability(name: String): EntityCapability<T?, Void?> {
		return EntityCapability.createVoid(modRl(name), T::class.java)
	}
}
