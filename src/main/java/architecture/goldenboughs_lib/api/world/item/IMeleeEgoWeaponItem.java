package architecture.goldenboughs_lib.api.world.item;

import architecture.goldenboughs_lib.util.ItemBuilderUtil;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/**
 * 所有E.G.O近战武器都应该继承这个
 */
public interface IMeleeEgoWeaponItem extends IEgoWeaponItem {
	// TODO 近战的替换成史诗战斗的实际攻击范围
//  ResourceLocation ENTITY_RANGE = GoldenBoughsLib.modRl("weapon_entity_range");

	class Builder extends IEgoWeaponItem.Builder<Builder> {
		/**
		 * 近战攻击速度
		 */
		public float attackSpeed;
		/**
		 * 近战攻击距离
		 */
		public float attackDistance;

		/**
		 * 近战攻击速度
		 */
		public Builder attackSpeed(float meleeAttackSpeed) {
			this.attackSpeed = meleeAttackSpeed;
			return this;
		}

		/**
		 * 近战攻击距离
		 */
		public Builder attackDistance(float meleeAttackDistance) {
			this.attackDistance = meleeAttackDistance;
			return this;
		}

		@Override
		public ItemAttributeModifiers getItemAttributeModifiers() {
			ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
			ItemBuilderUtil.addAttributeModifier(attributeBuilder, Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID, this.weaponDamage, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.HAND);
			ItemBuilderUtil.addAttributeModifier(attributeBuilder, Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID, this.attackSpeed, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.HAND);
			// TODO 近战的替换成史诗战斗的实际攻击范围
//      ItemBuilderUtil.addAttributeModifier(attributeBuilder, Attributes.ENTITY_INTERACTION_RANGE, ENTITY_RANGE, this.attackDistance, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.HAND);
			return attributeBuilder.build();
		}
	}
}
