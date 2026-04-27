package architecture.goldenboughs_lib.api.world.item;

import architecture.goldenboughs_lib.init.LibAttributes;
import architecture.goldenboughs_lib.util.ItemBuilderUtil;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 所有E.G.O远程武器都应该继承这个
 */
public interface IRemoteEgoWeaponItem extends IEgoWeaponItem {

	@FunctionalInterface
	interface CreateProjectile<T extends Projectile> {
		T createProjectile(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon);
	}

	class Builder extends IEgoWeaponItem.Builder<Builder> {
		/**
		 * 远程攻击间隔
		 */
		public float attackInterval;
		public float attackIntervalMainHand;
		public float attackIntervalOffHand;
		/**
		 * 远程攻击距离
		 */
		public float attackDistance;

		public CreateProjectile<? extends Projectile> createProjectile;

		public Builder createProjectile(CreateProjectile<? extends Projectile> projectileCreator) {
			this.createProjectile = projectileCreator;
			return this;
		}

		/**
		 * 远程攻击间隔
		 */
		public Builder attackIntervalHand(float weaponAttackInterval) {
			this.attackIntervalMainHand = weaponAttackInterval;
			this.attackIntervalOffHand = weaponAttackInterval;
			return this;
		}

		/**
		 * 远程攻击间隔
		 */
		public Builder attackIntervalMainHand(float weaponAttackInterval) {
			this.attackIntervalMainHand = weaponAttackInterval;
			return this;
		}

		/**
		 * 远程攻击间隔
		 */
		public Builder attackIntervalOffHand(float weaponAttackInterval) {
			this.attackIntervalOffHand = weaponAttackInterval;
			return this;
		}

		/**
		 * 远程攻击距离
		 */
		public Builder attackDistance(float weaponAttackDistance) {
			this.attackDistance = weaponAttackDistance;
			return this;
		}

		@Override
		public ItemAttributeModifiers getItemAttributeModifiers() {
			ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
			ItemBuilderUtil.addAttributeModifier(builder, Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID, this.weaponDamage, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.HAND);
			ItemBuilderUtil.addAttributeModifier(builder, Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID, this.attackInterval, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.HAND);
			ItemBuilderUtil.addAttributeModifier(builder, LibAttributes.ATTACK_SPEED_MAIN_HAND, Item.BASE_ATTACK_SPEED_ID, this.attackIntervalMainHand, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.MAINHAND);
			ItemBuilderUtil.addAttributeModifier(builder, LibAttributes.ATTACK_SPEED_OFF_HAND, Item.BASE_ATTACK_SPEED_ID, this.attackIntervalOffHand, AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.OFFHAND);
			return builder.build();
		}
	}
}
