package architecture.goldenboughs_lib.common.item

import architecture.goldenboughs_lib.api.world.item.IMeleeEgoWeaponItem
import architecture.goldenboughs_lib.common.command.LcDamageTypeDataComponent
import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibDataComponentTypes
import architecture.goldenboughs_lib.module.lc_damage.api.LcDamageType
import architecture.goldenboughs_lib.module.lc_damage.capability.IItemLcDamageType
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
		LcDamageTypeDataComponent(LcDamageType.PHYSICS, *LcDamageType.entries.toTypedArray())
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
		) { damageType: LcDamageTypeDataComponent ->
			val values: Array<LcDamageType> = LcDamageType.entries.toTypedArray()
			val i = damageType.lcDamageType!!.index + 1
			LcDamageTypeDataComponent(values[if (i >= values.size) 0 else i], *LcDamageType.entries.toTypedArray())
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
		val KEY: String = "${LibConstants.ID}.item_tooltip.geo_describe.damage_type"

		@JvmStatic
		private val DEFAULT_COMPONENT =
			LcDamageTypeDataComponent(LcDamageType.PHYSICS, *LcDamageType.entries.toTypedArray())

		private fun getComponent(stack: ItemStack): LcDamageTypeDataComponent {
			return stack.getOrDefault(LibDataComponentTypes.LC_DAMAGE_TYPE, DEFAULT_COMPONENT)
		}
	}
}
