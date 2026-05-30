package architecture.goldenboughs_lib.module.lc_damage.util

import architecture.goldenboughs_lib.events.registry.CapabilityRegistry
import architecture.goldenboughs_lib.init.LibCapabilitys
import architecture.goldenboughs_lib.module.lc_damage.api.LcLevel
import architecture.goldenboughs_lib.module.lc_damage.capability.IBlockLcLevel
import architecture.goldenboughs_lib.module.lc_damage.capability.IEntityLcLevel
import architecture.goldenboughs_lib.module.lc_damage.capability.IItemLcLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import org.jetbrains.annotations.ApiStatus
import java.util.function.Supplier

object LcLevelUtil {
	// 仅内部开发使用
	@ApiStatus.Internal
	@JvmField
	val ENTITY_TYPE_LEVEL: MutableMap<EntityType<*>, LcLevel?> = HashMap()

	@JvmStatic
	fun <T : Entity> lcLevel(lcLevel: LcLevel, holder: DeferredHolder<EntityType<*>, EntityType<T>>) {
		@Suppress("UNCHECKED_CAST")
		(when (lcLevel) {
			LcLevel.ZAYIN -> CapabilityRegistry.ENTITY_ZAYIN
			LcLevel.TETH -> CapabilityRegistry.ENTITY_TETH
			LcLevel.HE -> CapabilityRegistry.ENTITY_HE
			LcLevel.WAW -> CapabilityRegistry.ENTITY_WAW
			LcLevel.ALEPH -> CapabilityRegistry.ENTITY_ALEPH
		}).add(holder as Supplier<EntityType<*>>)
	}

	/**
	 * 如果没注册到能力中返回LcLevel.ZAYIN
	 *
	 *
	 * 如果有，则尝试从能力系统中获取如果获取的是null那就返回null
	 */
	@JvmStatic
	fun Entity.getLevel(): LcLevel? {
		val capability = getLevelCapability()
		return if (capability == null) LcLevel.ZAYIN else capability.getLcLevel()
	}

	@JvmStatic
	fun Entity.getLevelCapability(): IEntityLcLevel? {
		return getCapability(LibCapabilitys.LcLevel.LC_LEVEL_ENTITY)
	}

	/**
	 * 如果没注册到能力中返回LcLevel.ZAYIN
	 *
	 *
	 * 如果有，则尝试从能力系统中获取如果获取的是null那就返回null
	 */
	@JvmStatic
	fun ItemStack.getLevel(): LcLevel? {
		// 如果是刷怪蛋就按照生物等级来获取
		(item as? SpawnEggItem)?.run { return getType(this@getLevel).getLevel() }

		return getLevelCapability()?.run { getLcLevel(this@getLevel) } ?: LcLevel.ZAYIN
	}

	@JvmStatic
	fun ItemStack.getLevelCapability(): IItemLcLevel? {
		return getCapability(LibCapabilitys.LcLevel.LC_LEVEL_ITEM)
	}

	/**
	 * 如果没注册到能力中返回LcLevel.ZAYIN
	 *
	 *
	 * 如果有，则尝试从能力系统中获取如果获取的是null那就返回null
	 */
	@JvmStatic
	fun EntityType<*>.getLevel(): LcLevel? {
		if (ENTITY_TYPE_LEVEL.containsKey(this)) {
			return ENTITY_TYPE_LEVEL[this]
		}
		ENTITY_TYPE_LEVEL[this] = LcLevel.ZAYIN
		return LcLevel.ZAYIN
	}

	/**
	 * 如果没注册到能力中返回LcLevel.ZAYIN
	 *
	 *
	 * 如果有，则尝试从能力系统中获取如果获取的是null那就返回null
	 */
	@JvmStatic
	fun getLevel(level: Level, pos: BlockPos): LcLevel? {
		return getLevel(level, pos, level.getBlockState(pos), level.getBlockEntity(pos))
	}

	/**
	 * 如果没注册到能力中返回LcLevel.ZAYIN
	 *
	 *
	 * 如果有，则尝试从能力系统中获取如果获取的是null那就返回null
	 */
	@JvmStatic
	fun getLevel(level: Level, pos: BlockPos, state: BlockState?, blockEntity: BlockEntity?): LcLevel? {
		val capability = getLevelCapability(level, pos, state, blockEntity)
		return if (capability == null) LcLevel.ZAYIN else capability.getLcLevel(level, pos)
	}

	@JvmStatic
	fun getLevelCapability(level: Level, pos: BlockPos, state: BlockState?, blockEntity: BlockEntity?): IBlockLcLevel? {
		return level.getCapability(LibCapabilitys.LcLevel.LC_LEVEL_BLOCK, pos, state, blockEntity)
	}

	/**
	 * 获取伤害比例
	 *
	 * @param attackedLevel 被攻击的等级
	 * @param attackerLevel 攻击者的等级
	 */
	@JvmStatic
	fun getDamageMultiple(attackedLevel: LcLevel?, attackerLevel: LcLevel?): Float {
		if (attackedLevel == null || attackerLevel == null) {
			return 1.0f
		}
		val attackedLevelValue = attackedLevel.levelValue
		val attackerLevelValue = attackerLevel.levelValue
		return when (val i = attackedLevelValue - attackerLevelValue) {
			4 -> 0.4f
			3 -> 0.6f
			2 -> 0.7f
			1 -> 0.8f
			0, -1 -> 1.0f
			-2 -> 1.2f
			-3 -> 1.5f
			-4 -> 2.0f
			else -> throw IllegalArgumentException("Invalid levelValue difference: $i")
		}
	}

	/**
	 * 添加物品等级能力
	 *
	 * @param lcLevel      物品等级
	 * @param deferredItem 物品
	 */
	@JvmStatic
	fun <I : Item> addItemLcLevelCapability(lcLevel: LcLevel, deferredItem: DeferredItem<I?>) {
		when (lcLevel) {
			LcLevel.ZAYIN -> CapabilityRegistry.ITEM_ZAYIN.add(deferredItem)
			LcLevel.TETH -> CapabilityRegistry.ITEM_TETH.add(deferredItem)
			LcLevel.HE -> CapabilityRegistry.ITEM_HE.add(deferredItem)
			LcLevel.WAW -> CapabilityRegistry.ITEM_WAW.add(deferredItem)
			LcLevel.ALEPH -> CapabilityRegistry.ITEM_ALEPH.add(deferredItem)
		}
	}
}
