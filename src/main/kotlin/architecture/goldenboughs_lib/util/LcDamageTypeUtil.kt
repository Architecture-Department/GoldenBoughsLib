package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.api.capability.item.IItemLcDamageType
import architecture.goldenboughs_lib.init.LibCapabilitys
import architecture.goldenboughs_lib.init.LibDataComponentTypes
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

object LcDamageTypeUtil {
	/**
	 * 获取伤害物品
	 */
	@JvmStatic
	fun DamageSource.getDamageItemStack(): ItemStack? {
		return weaponItem
	}

	@JvmStatic
	fun ItemStack.getLcDamageType(): LcDamageType? {
		val colorDamageTypeItem = getLcDamageTypeCapability()
		colorDamageTypeItem?.let { return it.getLcDamageType(this) }

		(item as? IItemLcDamageType)?.let {
			return it.getLcDamageType(this)
		}

		return LcDamageType.PHYSICS
	}

	@JvmStatic
	fun ItemStack.getLcDamageTypeCapability(): IItemLcDamageType? {
		return getCapability(LibCapabilitys.LC_DAMAGE_TYPE_ITEM)
	}

	@JvmStatic
	fun ItemStack.getCanCauseLcDamageTypes(): Set<LcDamageType> {
		if (has(LibDataComponentTypes.LC_DAMAGE_TYPE)) {
			val component = get(LibDataComponentTypes.LC_DAMAGE_TYPE)
			return component?.canCauseLcDamageTypes ?: mutableSetOf()
		}

		(item as? IItemLcDamageType)?.let {
			return it.getCanCauseLcDamageTypes(this)
		}

		return mutableSetOf()
	}
}
