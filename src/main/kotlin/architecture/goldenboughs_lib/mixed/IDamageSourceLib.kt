package architecture.goldenboughs_lib.mixed

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.api.LcLevel
import architecture.goldenboughs_lib.api.NoMixinException
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

interface IDamageSourceLib {
	/**
	 * 设置武器物品
	 *
	 * @param itemStack 武器物品
	 */
	fun `goldenboughs_lib$setWeaponItem`(itemStack: ItemStack?) {
		throw NoMixinException()
	}

	/**
	 * 获取LC伤害类型
	 *
	 * @return LC伤害类型，如果返回空则不受到抗性影响
	 */
	fun `goldenboughs_lib$getLcDamageType`(): LcDamageType? {
		throw NoMixinException()
	}

	/**
	 * 设置LC伤害类型
	 *
	 * @param type LC伤害类型
	 */
	fun `goldenboughs_lib$setLcDamageType`(type: LcDamageType?) {
		throw NoMixinException()
	}

	/**
	 * 获取LC伤害等级
	 *
	 * @return LC伤害等级，如果返回空则不受到等级影响
	 */
	fun `goldenboughs_lib$getLcDamageLevel`(): LcLevel? {
		throw NoMixinException()
	}

	/**
	 * 设置LC伤害等级
	 *
	 * @param pmLevel LC伤害等级
	 */
	fun `goldenboughs_lib$setDamageLevel`(pmLevel: LcLevel?) {
		throw NoMixinException()
	}

	companion object {
		@JvmStatic
		fun of(obj: DamageSource): IDamageSourceLib {
			return obj
		}
	}
}
