package architecture.goldenboughs_lib.api.capability.item

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * 带有需求的物品 如果不满足要求则触发
 * 
 * 
 * 一般捆绑[architecture.goldenboughs_lib.common.data_component.ItemVirtueUsageReq]或
 * [architecture.goldenboughs_lib.init.LibDataComponentTypes.ITEM_VIRTUE_USAGE_REQ]
 */
@AllOpe
interface IItemUsageReq {
	/**
	 * 使用物品时触发
	 */
	fun useImpede(itemStack: ItemStack, level: Level, entity: LivingEntity) {
	}

	/**
	 * 攻击时触发
	 */
	fun attackImpede(itemStack: ItemStack, level: Level, entity: LivingEntity) {
	}

	/**
	 * 在手上时触发
	 */
	fun onTheHandImpede(itemStack: ItemStack, level: Level, entity: LivingEntity) {
	}

	/**
	 * 物品在背包时里触发
	 */
	fun inTheBackpackImpede(itemStack: ItemStack, level: Level, entity: LivingEntity) {
	}

	/**
	 * 在装备里时触发，如盔甲，饰品
	 */
	fun equipmentImpede(itemStack: ItemStack, level: Level, entity: LivingEntity) {
	}
}
