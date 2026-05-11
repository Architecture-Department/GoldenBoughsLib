package architecture.goldenboughs_lib.common.item

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.api.capability.item.IItemLcDamageType
import architecture.goldenboughs_lib.api.world.item.IMeleeEgoWeaponItem
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibDataComponentTypes
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.SimpleTier

/** 混沌刀 */
class ChaosKnifeItem(itemProperties: Properties, builder: IMeleeEgoWeaponItem.Builder) : SwordItem(
	SimpleTier(
		BlockTags.INCORRECT_FOR_IRON_TOOL,
		0,
		builder.attackSpeed,
		builder.weaponDamage,
		5
	) { Ingredient.of() },
	itemProperties.component(
		LibDataComponentTypes.LC_DAMAGE_TYPE.get(),
		LcDamageType.Component(LcDamageType.PHYSICS, *LcDamageType.values())
	)
), IMeleeEgoWeaponItem, IItemLcDamageType {
	override fun use(
		world: Level,
		playerEntity: Player,
		handUsed: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val itemStackInHand = playerEntity.getItemInHand(handUsed)
		itemStackInHand.update(
			LibDataComponentTypes.LC_DAMAGE_TYPE, DEFAULT_COMPONENT
		) { damageType: LcDamageType.Component ->
			val values: Array<LcDamageType> = LcDamageType.entries.toTypedArray()
			val i = damageType.lcDamageType!!.index + 1
			LcDamageType.Component(values[if (i >= values.size) 0 else i], *LcDamageType.values())
		}
		return InteractionResultHolder.success(itemStackInHand)
	}

	override fun getLcDamageType(stack: ItemStack): LcDamageType {
		return getComponent(stack).lcDamageType!!
	}

	override fun getCanCauseLcDamageTypes(stack: ItemStack): Set<LcDamageType> {
		return getComponent(stack).canCauseLcDamageTypes
	}

	companion object {
		@JvmField
		val KEY: String = Lib.ID + ".item_tooltip.geo_describe.damage_type"

		@JvmStatic
		private val DEFAULT_COMPONENT = LcDamageType.Component(LcDamageType.PHYSICS, *LcDamageType.values())

		private fun getComponent(stack: ItemStack): LcDamageType.Component {
			return stack.getOrDefault(LibDataComponentTypes.LC_DAMAGE_TYPE, DEFAULT_COMPONENT)
		}
	}
}
