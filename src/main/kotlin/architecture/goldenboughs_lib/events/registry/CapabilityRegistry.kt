package architecture.goldenboughs_lib.events.registry

import architecture.goldenboughs_lib.api.capability.item.IItemUsageReq
import architecture.goldenboughs_lib.init.LibCapabilitys
import architecture.goldenboughs_lib.init.LibItems
import architecture.goldenboughs_lib.module.lc_damage.api.LcLevel
import architecture.goldenboughs_lib.module.lc_damage.capability.*
import architecture.goldenboughs_lib.module.lc_damage.util.LcLevelUtil
import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import java.util.function.Supplier

/**
 * 注册能力
 */
@EventBusSubscriber(modid = LibUtil.ID)
object CapabilityRegistry {
	@JvmField
	val ITEM_ZAYIN: MutableList<ItemLike> = ArrayList()

	@JvmField
	val ITEM_TETH: MutableList<ItemLike> = ArrayList()

	@JvmField
	val ITEM_HE: MutableList<ItemLike> = ArrayList()

	@JvmField
	val ITEM_WAW: MutableList<ItemLike> = ArrayList()

	@JvmField
	val ITEM_ALEPH: MutableList<ItemLike> = ArrayList()

	@JvmField
	val BLOCK_ZAYIN: MutableList<Supplier<Block>> = ArrayList()

	@JvmField
	val BLOCK_TETH: MutableList<Supplier<Block>> = ArrayList()

	@JvmField
	val BLOCK_HE: MutableList<Supplier<Block>> = ArrayList()

	@JvmField
	val BLOCK_WAW: MutableList<Supplier<Block>> = ArrayList()

	@JvmField
	val BLOCK_ALEPH: MutableList<Supplier<Block>> = ArrayList()

	@JvmField
	val ENTITY_ZAYIN: MutableList<Supplier<EntityType<*>>> = ArrayList()

	@JvmField
	val ENTITY_TETH: MutableList<Supplier<EntityType<*>>> = ArrayList()

	@JvmField
	val ENTITY_HE: MutableList<Supplier<EntityType<*>>> = ArrayList()

	@JvmField
	val ENTITY_WAW: MutableList<Supplier<EntityType<*>>> = ArrayList()

	@JvmField
	val ENTITY_ALEPH: MutableList<Supplier<EntityType<*>>> = ArrayList()

	init {
		ENTITY_ALEPH.add(Supplier { EntityType.WITHER })
		ENTITY_ALEPH.add(Supplier { EntityType.ENDER_DRAGON })
		ENTITY_ALEPH.add(Supplier { EntityType.WARDEN })

		ENTITY_WAW.add(Supplier { EntityType.RAVAGER })
		ENTITY_WAW.add(Supplier { EntityType.ELDER_GUARDIAN })
		ENTITY_WAW.add(Supplier { EntityType.IRON_GOLEM })

		ENTITY_HE.add(Supplier { EntityType.WITHER_SKELETON })
		ENTITY_HE.add(Supplier { EntityType.WITCH })
		ENTITY_HE.add(Supplier { EntityType.VINDICATOR })
		ENTITY_HE.add(Supplier { EntityType.EVOKER })
		ENTITY_HE.add(Supplier { EntityType.ZOGLIN })
		ENTITY_HE.add(Supplier { EntityType.SHULKER })
		ENTITY_HE.add(Supplier { EntityType.PIGLIN_BRUTE })
		ENTITY_HE.add(Supplier { EntityType.HOGLIN })
		ENTITY_HE.add(Supplier { EntityType.GHAST })
		ENTITY_HE.add(Supplier { EntityType.ENDERMAN })
		ENTITY_HE.add(Supplier { EntityType.GUARDIAN })

		ENTITY_TETH.add(Supplier { EntityType.CAVE_SPIDER })
		ENTITY_TETH.add(Supplier { EntityType.SPIDER })
		ENTITY_TETH.add(Supplier { EntityType.PIGLIN })
		ENTITY_TETH.add(Supplier { EntityType.PILLAGER })
		ENTITY_TETH.add(Supplier { EntityType.VEX })
		ENTITY_TETH.add(Supplier { EntityType.SILVERFISH })
		ENTITY_TETH.add(Supplier { EntityType.ENDERMITE })
		ENTITY_TETH.add(Supplier { EntityType.PHANTOM })
		ENTITY_TETH.add(Supplier { EntityType.MAGMA_CUBE })
		ENTITY_TETH.add(Supplier { EntityType.HUSK })
		ENTITY_TETH.add(Supplier { EntityType.CREEPER })
		ENTITY_TETH.add(Supplier { EntityType.BREEZE })
		ENTITY_TETH.add(Supplier { EntityType.DROWNED })
		ENTITY_TETH.add(Supplier { EntityType.ZOMBIFIED_PIGLIN })
		ENTITY_TETH.add(Supplier { EntityType.ZOMBIE })
		ENTITY_TETH.add(Supplier { EntityType.STRAY })
		ENTITY_TETH.add(Supplier { EntityType.SKELETON })
		ENTITY_TETH.add(Supplier { EntityType.BOGGED })
		ENTITY_TETH.add(Supplier { EntityType.BLAZE })
		ENTITY_TETH.add(Supplier { EntityType.SLIME })
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun registerHighest(event: RegisterCapabilitiesEvent) {
		itemLevel(event, LcLevel.ZAYIN, ITEM_ZAYIN)
		itemLevel(event, LcLevel.TETH, ITEM_TETH)
		itemLevel(event, LcLevel.HE, ITEM_HE)
		itemLevel(event, LcLevel.WAW, ITEM_WAW)
		itemLevel(event, LcLevel.ALEPH, ITEM_ALEPH)
		itemLevel(event, LcLevel.ALEPH, ITEM_ALEPH)
		itemLevel(event, ItemLcLevel.NULL, LibItems.CHAOS_SWORD, LibItems.CREATIVE_RATIONALITY_TOOL)
		entityLevel(event, LcLevel.ZAYIN, ENTITY_ZAYIN)
		entityLevel(event, LcLevel.TETH, ENTITY_TETH)
		entityLevel(event, LcLevel.HE, ENTITY_HE)
		entityLevel(event, LcLevel.WAW, ENTITY_WAW)
		entityLevel(event, LcLevel.ALEPH, ENTITY_ALEPH)
		blockLevel(event, LcLevel.ZAYIN, BLOCK_ZAYIN)
		blockLevel(event, LcLevel.TETH, BLOCK_TETH)
		blockLevel(event, LcLevel.HE, BLOCK_HE)
		blockLevel(event, LcLevel.WAW, BLOCK_WAW)
		blockLevel(event, LcLevel.ALEPH, BLOCK_ALEPH)
	}

	@JvmStatic
	fun itemLevel(event: RegisterCapabilitiesEvent, lcLevel: IItemLcLevel, vararg items: ItemLike) {
		event.registerItem(
			LibCapabilitys.LcLevel.LC_LEVEL_ITEM, { stack: ItemStack, _ -> lcLevel }, *items
		)
	}

	@JvmStatic
	fun itemLevel(event: RegisterCapabilitiesEvent, lcLevel: LcLevel?, items: List<ItemLike>) {
		if (items.isEmpty()) {
			return
		}
		event.registerItem(
			LibCapabilitys.LcLevel.LC_LEVEL_ITEM, { stack: ItemStack, _ ->
				when (lcLevel) {
					LcLevel.ZAYIN -> ItemLcLevel.ZAYIN
					LcLevel.TETH -> ItemLcLevel.TETH
					LcLevel.HE -> ItemLcLevel.HE
					LcLevel.WAW -> ItemLcLevel.WAW
					LcLevel.ALEPH -> ItemLcLevel.ALEPH
					null -> ItemLcLevel.NULL
				}
			}, *items.toTypedArray()
		)
	}

	@JvmStatic
	fun blockLevel(event: RegisterCapabilitiesEvent, lcLevel: LcLevel?, items: List<Supplier<Block>>) {
		if (items.isEmpty()) {
			return
		}
		event.registerBlock(
			LibCapabilitys.LcLevel.LC_LEVEL_BLOCK,
			{ level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: BlockEntity?, _ ->
				when (lcLevel) {
					LcLevel.ZAYIN -> BlockLcLevel.ZAYIN
					LcLevel.TETH -> BlockLcLevel.TETH
					LcLevel.HE -> BlockLcLevel.HE
					LcLevel.WAW -> BlockLcLevel.WAW
					LcLevel.ALEPH -> BlockLcLevel.ALEPH
					null -> BlockLcLevel.NULL
				}
			},
			*items.stream().map { it.get() }.toArray() as Array<Block>
		)
	}

	@JvmStatic
	fun entityLevel(event: RegisterCapabilitiesEvent, lcLevel: LcLevel?, items: List<Supplier<EntityType<*>>>) {
		if (items.isEmpty()) {
			return
		}
		for (entityTypeSupplier in items) {
			val entityType = entityTypeSupplier.get()
			LcLevelUtil.ENTITY_TYPE_LEVEL[entityType] = lcLevel
			event.registerEntity(
				LibCapabilitys.LcLevel.LC_LEVEL_ENTITY, entityType
			) { stack: Any?, ctx: Void? ->
				when (lcLevel) {
					LcLevel.ZAYIN -> EntityLcLevel.ZAYIN
					LcLevel.TETH -> EntityLcLevel.TETH
					LcLevel.HE -> EntityLcLevel.HE
					LcLevel.WAW -> EntityLcLevel.WAW
					LcLevel.ALEPH -> EntityLcLevel.ALEPH
					null -> EntityLcLevel.NULL
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun registerLowest(event: RegisterCapabilitiesEvent) {
		for (item in BuiltInRegistries.ITEM) {
			registerItem(
				event, item, IItemLcDamageType::class.java, LibCapabilitys.LC_DAMAGE_TYPE_ITEM
			)
			registerItem(
				event, item, IItemUsageReq::class.java, LibCapabilitys.USAGE_REQ_ITEM
			)
			registerItem(
				event, item, IItemLcLevel::class.java, LibCapabilitys.LcLevel.LC_LEVEL_ITEM
			)
		}

		for (entityType in BuiltInRegistries.ENTITY_TYPE) {
			registerEntity(
				event, entityType, IEntityLcLevel::class.java, LibCapabilitys.LcLevel.LC_LEVEL_ENTITY
			)
		}

		for (blockEntityType in BuiltInRegistries.BLOCK_ENTITY_TYPE) {
			registerBlockEntity(
				event, blockEntityType, IBlockLcLevel::class.java, LibCapabilitys.LcLevel.LC_LEVEL_BLOCK
			)
		}

		for (block in BuiltInRegistries.BLOCK) {
			registerBlock(
				event, block, IBlockLcLevel::class.java, LibCapabilitys.LcLevel.LC_LEVEL_BLOCK
			)
		}
	}

	fun <T, C> registerItem(
		event: RegisterCapabilitiesEvent, item: Item, capabilityClass: Class<T>, capability: ItemCapability<T?, C?>
	) {
		if (!capabilityClass.isInstance(item) || event.isItemRegistered(capability, item)) {
			return
		}
		event.registerItem(
			capability, { stack: ItemStack, ctx: C -> capabilityClass.cast(item) }, item
		)
	}

	fun <T, C> registerBlock(
		event: RegisterCapabilitiesEvent, block: Block, capabilityClass: Class<T>, capability: BlockCapability<T?, C?>
	) {
		if (!capabilityClass.isInstance(block) || event.isBlockRegistered(capability, block)) {
			return
		}
		event.registerBlock(
			capability, { level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: BlockEntity?, c: C ->
				capabilityClass.cast(block)
			}, block
		)
	}

	fun <T, C> registerBlockEntity(
		event: RegisterCapabilitiesEvent,
		block: BlockEntityType<*>,
		capabilityClass: Class<T>,
		capability: BlockCapability<T?, C?>
	) {
		event.registerBlockEntity(
			capability, block
		) { blockEntity: Any, c: C -> if (capabilityClass.isInstance(blockEntity)) capabilityClass.cast(blockEntity) else null }
	}

	fun <T, C> registerEntity(
		event: RegisterCapabilitiesEvent,
		entityType: EntityType<*>,
		capabilityClass: Class<T>,
		capability: EntityCapability<T?, C?>
	) {
		if (event.isEntityRegistered(capability, entityType)) {
			return
		}
		event.registerEntity(
			capability, entityType
		) { entity: Any, ctx: C -> if (capabilityClass.isInstance(entity)) capabilityClass.cast(entity) else null }
	}
}
