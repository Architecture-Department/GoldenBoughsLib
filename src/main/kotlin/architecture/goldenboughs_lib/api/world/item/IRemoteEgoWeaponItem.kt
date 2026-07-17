package architecture.goldenboughs_lib.api.world.item

import architecture.goldenboughs_lib.api.AllOpen
import architecture.goldenboughs_lib.init.LibAttributes
import architecture.goldenboughs_lib.util.world.item.ItemBuilderUtil.addAttributeModifier
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level

/**
 * 所有E.G.O远程武器都应该继承这个
 */
@AllOpen
interface IRemoteEgoWeaponItem : IEgoWeaponItem {
	@FunctionalInterface
	fun interface CreateProjectile<T : Projectile> {
		fun createProjectile(
			level: Level,
			owner: LivingEntity,
			pickupItemStack: ItemStack,
			firedFromWeapon: ItemStack
		): T
	}

	class Builder : IEgoWeaponItem.Builder<Builder>() {
		/**
		 * 远程攻击间隔
		 */
		var attackInterval: Float = 0f

		var attackIntervalMainHand: Float = 0f

		var attackIntervalOffHand: Float = 0f

		/**
		 * 远程攻击距离
		 */
		var attackDistance: Float = 0f

		var createProjectile: CreateProjectile<out Projectile>? = null

		fun createProjectile(projectileCreator: CreateProjectile<out Projectile>): Builder {
			this.createProjectile = projectileCreator
			return this
		}

		/**
		 * 远程攻击间隔
		 */
		fun attackIntervalHand(weaponAttackInterval: Float): Builder {
			this.attackIntervalMainHand = weaponAttackInterval
			this.attackIntervalOffHand = weaponAttackInterval
			return this
		}

		/**
		 * 远程攻击间隔
		 */
		fun attackIntervalMainHand(weaponAttackInterval: Float): Builder {
			this.attackIntervalMainHand = weaponAttackInterval
			return this
		}

		/**
		 * 远程攻击间隔
		 */
		fun attackIntervalOffHand(weaponAttackInterval: Float): Builder {
			this.attackIntervalOffHand = weaponAttackInterval
			return this
		}

		/**
		 * 远程攻击距离
		 */
		fun attackDistance(weaponAttackDistance: Float): Builder {
			this.attackDistance = weaponAttackDistance
			return this
		}

		override val itemAttributeModifiers: ItemAttributeModifiers
			get() {
				val builder =
					ItemAttributeModifiers.builder()
				addAttributeModifier(
					builder,
					Attributes.ATTACK_DAMAGE,
					Item.BASE_ATTACK_DAMAGE_ID,
					this.weaponDamage.toDouble(),
					AttributeModifier.Operation.ADD_VALUE,
					EquipmentSlotGroup.HAND
				)
				addAttributeModifier(
					builder,
					Attributes.ATTACK_SPEED,
					Item.BASE_ATTACK_SPEED_ID,
					this.attackInterval.toDouble(),
					AttributeModifier.Operation.ADD_VALUE,
					EquipmentSlotGroup.HAND
				)
				addAttributeModifier(
					builder,
					LibAttributes.ATTACK_SPEED_MAIN_HAND,
					Item.BASE_ATTACK_SPEED_ID,
					this.attackIntervalMainHand.toDouble(),
					AttributeModifier.Operation.ADD_VALUE,
					EquipmentSlotGroup.MAINHAND
				)
				addAttributeModifier(
					builder,
					LibAttributes.ATTACK_SPEED_OFF_HAND,
					Item.BASE_ATTACK_SPEED_ID,
					this.attackIntervalOffHand.toDouble(),
					AttributeModifier.Operation.ADD_VALUE,
					EquipmentSlotGroup.OFFHAND
				)
				return builder.build()
			}
	}
}
