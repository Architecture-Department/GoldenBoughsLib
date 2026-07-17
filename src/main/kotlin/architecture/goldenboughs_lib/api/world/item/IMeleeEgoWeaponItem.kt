package architecture.goldenboughs_lib.api.world.item

import architecture.goldenboughs_lib.api.AllOpen
import architecture.goldenboughs_lib.util.world.item.ItemBuilderUtil.addAttributeModifier
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers

/**
 * 所有E.G.O近战武器都应该继承这个
 */
@AllOpen
interface IMeleeEgoWeaponItem : IEgoWeaponItem {
	// TODO 近战的替换成史诗战斗的实际攻击范围
	//  ResourceLocation ENTITY_RANGE = GoldenBoughsLib.modRl("weapon_entity_range");
	class Builder : IEgoWeaponItem.Builder<Builder>() {
		/**
		 * 近战攻击速度
		 */
		var attackSpeed: Float = 0f

		/**
		 * 近战攻击距离
		 */
		var attackDistance: Float = 0f

		/**
		 * 近战攻击速度
		 */
		fun attackSpeed(meleeAttackSpeed: Float): Builder {
			this.attackSpeed = meleeAttackSpeed
			return this
		}

		/**
		 * 近战攻击距离
		 */
		fun attackDistance(meleeAttackDistance: Float): Builder {
			this.attackDistance = meleeAttackDistance
			return this
		}

		override val itemAttributeModifiers: ItemAttributeModifiers
			get() {
				val attributeBuilder =
					ItemAttributeModifiers.builder()
				addAttributeModifier(
					attributeBuilder,
					Attributes.ATTACK_DAMAGE,
					Item.BASE_ATTACK_DAMAGE_ID,
					this.weaponDamage.toDouble(),
					AttributeModifier.Operation.ADD_VALUE,
					EquipmentSlotGroup.HAND
				)
				addAttributeModifier(
					attributeBuilder,
					Attributes.ATTACK_SPEED,
					Item.BASE_ATTACK_SPEED_ID,
					this.attackSpeed.toDouble(),
					AttributeModifier.Operation.ADD_VALUE,
					EquipmentSlotGroup.HAND
				)
				// TODO 近战的替换成史诗战斗的实际攻击范围
//      ItemBuilderUtil.addAttributeModifier(attributeBuilder, Attributes.ENTITY_INTERACTION_RANGE, ENTITY_RANGE, this.attackDistance, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.HAND);
				return attributeBuilder.build()
			}
	}
}
