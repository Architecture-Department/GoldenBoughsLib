package architecture.goldenboughs_lib.module.virtue.api

import architecture.goldenboughs_lib.init.LibAttributes
import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimap
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.NeoForgeMod

@JvmRecord
data class VirtueAttributeModifier(
	val fortitudeModifier: Builder.AttributeModifierBuilder?,
	val prudenceModifier: Builder.AttributeModifierBuilder?,
	val temperanceModifier: Builder.AttributeModifierBuilder?,
	val justiceModifier: Builder.AttributeModifierBuilder?
) {
	/**
	 * 获取
	 */
	fun getAttributeModifiers(
		entity: LivingEntity,
		id: ResourceLocation,
		stack: ItemStack
	): Multimap<Holder<Attribute>, AttributeModifier> {
		val modifier: Multimap<Holder<Attribute>, AttributeModifier> =
			LinkedHashMultimap.create<Holder<Attribute>, AttributeModifier>()
		addAttributeModifier(modifier, entity, id, stack, this.fortitudeModifier)
		addAttributeModifier(modifier, entity, id, stack, this.prudenceModifier)
		addAttributeModifier(modifier, entity, id, stack, this.temperanceModifier)
		addAttributeModifier(modifier, entity, id, stack, this.justiceModifier)
		return modifier
	}

	/**
	 * 添加属性
	 */
	private fun addAttributeModifier(
		multimap: Multimap<Holder<Attribute>, AttributeModifier>,
		entity: LivingEntity,
		id: ResourceLocation,
		stack: ItemStack,
		modifierBuilder: Builder.AttributeModifierBuilder?
	) {
		if (modifierBuilder == null) {
			return
		}
		multimap.putAll(modifierBuilder.apply(entity, id, stack))
	}

	/**
	 * 应用属性
	 */
	fun applyAttributeModifiers(entity: LivingEntity, id: ResourceLocation, stack: ItemStack) {
		addAttributeModifier(entity, id, stack, this.fortitudeModifier)
		addAttributeModifier(entity, id, stack, this.prudenceModifier)
		addAttributeModifier(entity, id, stack, this.temperanceModifier)
		addAttributeModifier(entity, id, stack, this.justiceModifier)
	}

	/**
	 * 移除属性
	 */
	fun removeAttributeModifiers(entity: LivingEntity, id: ResourceLocation, stack: ItemStack) {
		removeAttributeModifier(entity, id, stack, this.fortitudeModifier)
		removeAttributeModifier(entity, id, stack, this.prudenceModifier)
		removeAttributeModifier(entity, id, stack, this.temperanceModifier)
		removeAttributeModifier(entity, id, stack, this.justiceModifier)
	}

	/**
	 * 属性修改器
	 */
	class Builder {
		private var fortitudeModifier: AttributeModifierBuilder? = null
		private var prudenceModifier: AttributeModifierBuilder? = null
		private var temperanceModifier: AttributeModifierBuilder? = null
		private var justiceModifier: AttributeModifierBuilder? = null

		fun build(): VirtueAttributeModifier {
			return VirtueAttributeModifier(
				this.fortitudeModifier,
				this.prudenceModifier,
				this.temperanceModifier,
				this.justiceModifier
			)
		}

		/**
		 * 勇气
		 * 
		 * @param healthBonus 最大生命值加成
		 */
		fun fortitude(healthBonus: Int = 0): Builder {
			if (healthBonus == 0) {
				return this
			}
			this.fortitudeModifier =
				AttributeModifierBuilder { entity, id, stack ->
					val modifier: Multimap<Holder<Attribute>, AttributeModifier> = LinkedHashMultimap.create()
					addAttributeModifier(
						modifier,
						healthBonus.toDouble(),
						id,
						Attributes.MAX_HEALTH,
						AttributeModifier.Operation.ADD_VALUE
					)
					modifier
				}
			return this
		}

		/**
		 * 谨慎
		 * 
		 * @param rationalityBonus 最大理智加成
		 */
		fun prudence(rationalityBonus: Int = 0): Builder {
			if (rationalityBonus == 0) {
				return this
			}
			this.prudenceModifier =
				AttributeModifierBuilder { entity, id, stack ->
					val modifier: Multimap<Holder<Attribute>, AttributeModifier> = LinkedHashMultimap.create()
					addAttributeModifier(
						modifier,
						rationalityBonus.toDouble(),
						id,
						LibAttributes.MAX_RATIONALITY,
						AttributeModifier.Operation.ADD_VALUE
					)
					modifier
				}
			return this
		}

		/**
		 * 自律
		 * 
		 * @param blockBreakSpeedBonus 挖掘速度加成
		 * @param attackKnockbackBonus 攻击击退加成
		 * @param workValueBonus       工作成功率和工作速度加成
		 */
		fun temperance(blockBreakSpeedBonus: Int = 0, attackKnockbackBonus: Int = 0, workValueBonus: Int = 0): Builder {
			return temperance(blockBreakSpeedBonus, attackKnockbackBonus, workValueBonus, workValueBonus)
		}

		/**
		 * 自律
		 * 
		 * @param blockBreakSpeedBonus 挖掘速度加成
		 * @param attackKnockbackBonus 攻击击退加成
		 * @param workSuccessRateBonus 工作成功率加成
		 * @param workSpeedBonus       工作速度加成
		 */
		fun temperance(
			blockBreakSpeedBonus: Int = 0,
			attackKnockbackBonus: Int = 0,
			workSuccessRateBonus: Int = 0,
			workSpeedBonus: Int = 0
		): Builder {
			if (blockBreakSpeedBonus == 0 && attackKnockbackBonus == 0) {
				return this
			}
			this.temperanceModifier =
				AttributeModifierBuilder { entity, id, stack ->
					val modifier: Multimap<Holder<Attribute>, AttributeModifier> = LinkedHashMultimap.create()
					// TODO 补充 成功率，工作速度
					addAttributeModifier(
						modifier,
						blockBreakSpeedBonus * VirtueTemperance.BLOCK_BREAK_SPEED,
						id,
						Attributes.BLOCK_BREAK_SPEED,
						AttributeModifier.Operation.ADD_VALUE
					)
					addAttributeModifier(
						modifier,
						attackKnockbackBonus * VirtueTemperance.ATTACK_KNOCKBACK_SPEED,
						id,
						Attributes.ATTACK_KNOCKBACK,
						AttributeModifier.Operation.ADD_VALUE
					)
					modifier
				}
			return this
		}

		/**
		 * 自律
		 */
		fun temperance(valueBonus: Int = 0): Builder {
			return temperance(valueBonus, valueBonus, valueBonus, valueBonus)
		}

		/**
		 * 正义
		 * 
		 * @param speedBonus       移动速度和游泳速度加成
		 * @param attackSpeedBonus 攻击速度加成
		 */
		fun justice(speedBonus: Int = 0, attackSpeedBonus: Int = 0): Builder {
			return justice(speedBonus, speedBonus, attackSpeedBonus)
		}

		/**
		 * 正义
		 * 
		 * @param movementSpeedBonus 移动速度加成
		 * @param swimSpeedBonus     游泳速度加成
		 * @param attackSpeedBonus   攻击速度加成
		 */
		fun justice(movementSpeedBonus: Int = 0, swimSpeedBonus: Int = 0, attackSpeedBonus: Int = 0): Builder {
			if (movementSpeedBonus == 0 && attackSpeedBonus == 0 && swimSpeedBonus == 0) {
				return this
			}
			this.justiceModifier =
				AttributeModifierBuilder { entity, id, stack ->
					val modifier: Multimap<Holder<Attribute>, AttributeModifier> = LinkedHashMultimap.create()
					addAttributeModifier(
						modifier,
						movementSpeedBonus * VirtueJustice.MOVEMENT_SPEED,
						id,
						Attributes.MOVEMENT_SPEED,
						AttributeModifier.Operation.ADD_VALUE
					)
					addAttributeModifier(
						modifier,
						attackSpeedBonus * VirtueJustice.ATTACK_SPEED,
						id,
						Attributes.ATTACK_SPEED,
						AttributeModifier.Operation.ADD_VALUE
					)
					addAttributeModifier(
						modifier,
						swimSpeedBonus * VirtueJustice.SWIM_SPEED,
						id,
						NeoForgeMod.SWIM_SPEED,
						AttributeModifier.Operation.ADD_VALUE
					)
					modifier
				}
			return this
		}

		/**
		 * 正义
		 */
		fun justice(valueBonus: Int): Builder {
			return justice(valueBonus, valueBonus, valueBonus)
		}

		fun interface AttributeModifierBuilder {
			fun applyToEntity(entity: LivingEntity, id: ResourceLocation, stack: ItemStack) {
				entity.attributes.addTransientAttributeModifiers(apply(entity, id, stack))
			}

			fun apply(
				entity: LivingEntity,
				id: ResourceLocation,
				stack: ItemStack
			): Multimap<Holder<Attribute>, AttributeModifier>

			fun removeFromEntity(entity: LivingEntity, id: ResourceLocation, stack: ItemStack) {
				entity.attributes.removeAttributeModifiers(apply(entity, id, stack))
			}
		}
	}

	companion object {
		/**
		 * 添加属性
		 */
		@JvmStatic
		private fun addAttributeModifier(
			multimap: Multimap<Holder<Attribute>, AttributeModifier>,
			value: Double,
			id: ResourceLocation,
			holder: Holder<Attribute>,
			operation: AttributeModifier.Operation = AttributeModifier.Operation.ADD_VALUE
		) {
			if (value == 0.0) return

			multimap.put(holder, AttributeModifier(id, value, operation))
		}

		/**
		 * 添加属性
		 */
		@JvmStatic
		private fun addAttributeModifier(
			entity: LivingEntity,
			id: ResourceLocation,
			stack: ItemStack,
			modifierBuilder: Builder.AttributeModifierBuilder?
		) {
			modifierBuilder ?: return

			entity.attributes.addTransientAttributeModifiers(modifierBuilder.apply(entity, id, stack))
		}

		/**
		 * 移除属性
		 */
		@JvmStatic
		private fun removeAttributeModifier(
			entity: LivingEntity,
			id: ResourceLocation,
			stack: ItemStack,
			modifierBuilder: Builder.AttributeModifierBuilder?
		) {
			modifierBuilder ?: return

			entity.attributes.removeAttributeModifiers(modifierBuilder.apply(entity, id, stack))
		}
	}
}
