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
	fun getDamageItemStack(damageSource: DamageSource): ItemStack? {
		return damageSource.weaponItem
	}

	@JvmStatic
	fun getLcDamageType(itemStack: ItemStack): LcDamageType? {
		val colorDamageTypeItem = getLcDamageTypeCapability(itemStack)
		if (colorDamageTypeItem != null) {
			return colorDamageTypeItem.getLcDamageType(itemStack)
		}

		(itemStack.item as? IItemLcDamageType)?.run {
			return getLcDamageType(itemStack)
		}

		return LcDamageType.PHYSICS
	}

	@JvmStatic
	fun getLcDamageTypeCapability(itemStack: ItemStack): IItemLcDamageType? {
		return itemStack.getCapability<IItemLcDamageType?>(LibCapabilitys.LC_DAMAGE_TYPE_ITEM)
	}

	@JvmStatic
	fun getCanCauseLcDamageTypes(itemStack: ItemStack): Set<LcDamageType?> {
		if (itemStack.has(LibDataComponentTypes.LC_DAMAGE_TYPE)) {
			val component = itemStack.get(LibDataComponentTypes.LC_DAMAGE_TYPE)
			return component?.canCauseLcDamageTypes ?: mutableSetOf()
		}

		(itemStack.item as? IItemLcDamageType)?.run {
			return getCanCauseLcDamageTypes(itemStack)
		}

		return mutableSetOf()
	}
}
