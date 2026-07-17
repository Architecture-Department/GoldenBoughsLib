package architecture.goldenboughs_lib.common.item

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim

@AllOpen
class FoodItem
@JvmOverloads
constructor(
	properties: Properties,
	foodProperties: FoodProperties,
	val isEat: Boolean = true
) : Item(properties.food(foodProperties)) {
	override fun getUseAnimation(stack: ItemStack): UseAnim {
		return if (isEat) UseAnim.EAT else UseAnim.DRINK
	}
}