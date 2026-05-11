package architecture.goldenboughs_lib.api.capability.item

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.api.LcDamageType
import net.minecraft.world.item.ItemStack

/**
 * 该类主要针对物品的近战攻击
 *
 *
 * 如果你可以直接返回[net.minecraft.world.damagesource.DamageSource]请使用[architecture.goldenboughs_lib.mixed.IDamageSource.`goldenboughs_lib$setLcDamageType`]
 */
@AllOpe
interface IItemLcDamageType {
	/**
	 * 获取物品当前的伤害类型
	 */
	fun getLcDamageType(stack: ItemStack): LcDamageType?

	/**
	 * 获取可以造成的伤害类型 一般用于描述
	 */
	fun getCanCauseLcDamageTypes(stack: ItemStack): Set<LcDamageType> {
		return mutableSetOf()
	}
}
