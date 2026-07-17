package architecture.goldenboughs_lib.api.world.item

import architecture.goldenboughs_lib.api.AllOpen
import architecture.goldenboughs_lib.api.capability.item.IItemUsageReq
import architecture.goldenboughs_lib.module.lc_damage.api.LcDamageType
import architecture.goldenboughs_lib.module.lc_damage.capability.IItemLcDamageType
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers

/**
 * 所有E.G.O武器都应该继承这个
 */
@AllOpen
interface IEgoWeaponItem : IEgoItem, IItemUsageReq, IItemLcDamageType {
	/**
	 * 武器属性构造器
	 */
	class Builder<T : Builder<T>> : IEgoItem.Builder<T>() {
		var lcDamageType: LcDamageType? = null

		var canCauseLcDamageTypes: Set<LcDamageType> = setOf()

		var weaponDamage: Float = 0f

		fun damage(weaponDamageValue: Float): T {
			this.weaponDamage = weaponDamageValue
			return self()
		}

		fun meleeLcDamageType(
			meleeLcDamageType: LcDamageType?,
			lcDamageTypesThatCanBeCaused: Set<LcDamageType>
		): T {
			this.lcDamageType = meleeLcDamageType
			this.canCauseLcDamageTypes = lcDamageTypesThatCanBeCaused
			return self()
		}

		fun meleeLcDamageType(meleeLcDamageType: LcDamageType?): T {
			this.lcDamageType = meleeLcDamageType
			meleeLcDamageType?.apply { canCauseLcDamageTypes = setOf(this) }

			return self()
		}

		fun meleeLcDamageType(
			meleeLcDamageType: LcDamageType?,
			vararg lcDamageTypesThatCanBeCaused: LcDamageType
		): T {
			this.lcDamageType = meleeLcDamageType
			this.canCauseLcDamageTypes = setOf(*lcDamageTypesThatCanBeCaused)
			return self()
		}

		val itemAttributeModifiers: ItemAttributeModifiers
			get() = ItemAttributeModifiers.builder().build()
	}

	companion object {
		@JvmStatic
		fun add(properties: Item.Properties, builder: Builder<*>): Item.Properties {
			return IEgoItem.add(properties.attributes(builder.itemAttributeModifiers), builder)
		}
	}
}
