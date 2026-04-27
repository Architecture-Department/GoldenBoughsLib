package architecture.goldenboughs_lib.api.virtue;

import architecture.goldenboughs_lib.init.LibAttributes;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

public record VirtueAttributeModifier(
	@Nullable Builder.AttributeModifierBuilder fortitudeModifier,
	@Nullable Builder.AttributeModifierBuilder prudenceModifier,
	@Nullable Builder.AttributeModifierBuilder temperanceModifier,
	@Nullable Builder.AttributeModifierBuilder justiceModifier
) {
	/**
	 * 添加属性
	 */
	private static void addAttributeModifier(Multimap<Holder<Attribute>, AttributeModifier> multimap, double value, ResourceLocation id, Holder<Attribute> holder, AttributeModifier.Operation operation) {
		if (value == 0) {
			return;
		}
		multimap.put(holder, new AttributeModifier(id, value, operation));
	}

	/**
	 * 获取
	 */
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(LivingEntity entity, ResourceLocation id, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> modifier = LinkedHashMultimap.create();
		addAttributeModifier(modifier, entity, id, stack, this.fortitudeModifier);
		addAttributeModifier(modifier, entity, id, stack, this.prudenceModifier);
		addAttributeModifier(modifier, entity, id, stack, this.temperanceModifier);
		addAttributeModifier(modifier, entity, id, stack, this.justiceModifier);
		return modifier;
	}

	/**
	 * 添加属性
	 */
	private void addAttributeModifier(Multimap<Holder<Attribute>, AttributeModifier> multimap, final LivingEntity entity, final ResourceLocation id, final ItemStack stack, final @Nullable Builder.AttributeModifierBuilder modifierBuilder) {
		if (modifierBuilder == null) {
			return;
		}
		multimap.putAll(modifierBuilder.apply(entity, id, stack));
	}

	/**
	 * 应用属性
	 */
	public void applyAttributeModifiers(LivingEntity entity, ResourceLocation id, ItemStack stack) {
		addAttributeModifier(entity, id, stack, this.fortitudeModifier);
		addAttributeModifier(entity, id, stack, this.prudenceModifier);
		addAttributeModifier(entity, id, stack, this.temperanceModifier);
		addAttributeModifier(entity, id, stack, this.justiceModifier);
	}

	/**
	 * 添加属性
	 */
	private static void addAttributeModifier(final LivingEntity entity, final ResourceLocation id, final ItemStack stack, final @Nullable Builder.AttributeModifierBuilder modifierBuilder) {
		if (modifierBuilder == null) {
			return;
		}
		entity.getAttributes().addTransientAttributeModifiers(modifierBuilder.apply(entity, id, stack));
	}

	/**
	 * 移除属性
	 */
	public void removeAttributeModifiers(LivingEntity entity, ResourceLocation id, ItemStack stack) {
		removeAttributeModifier(entity, id, stack, this.fortitudeModifier);
		removeAttributeModifier(entity, id, stack, this.prudenceModifier);
		removeAttributeModifier(entity, id, stack, this.temperanceModifier);
		removeAttributeModifier(entity, id, stack, this.justiceModifier);
	}

	/**
	 * 移除属性
	 */
	private static void removeAttributeModifier(final LivingEntity entity, final ResourceLocation id, final ItemStack stack, final @Nullable Builder.AttributeModifierBuilder modifierBuilder) {
		if (modifierBuilder == null || entity == null) {
			return;
		}
		entity.getAttributes().removeAttributeModifiers(modifierBuilder.apply(entity, id, stack));
	}

	/**
	 * 属性修改器
	 */
	public static class Builder {
		private @Nullable AttributeModifierBuilder fortitudeModifier;
		private @Nullable AttributeModifierBuilder prudenceModifier;
		private @Nullable AttributeModifierBuilder temperanceModifier;
		private @Nullable AttributeModifierBuilder justiceModifier;

		public VirtueAttributeModifier build() {
			return new VirtueAttributeModifier(this.fortitudeModifier, this.prudenceModifier, this.temperanceModifier, this.justiceModifier);
		}

		/**
		 * 勇气
		 *
		 * @param healthBonus 最大生命值加成
		 */
		public Builder fortitude(int healthBonus) {
			if (healthBonus == 0) {
				return this;
			}
			this.fortitudeModifier = (entity, id, stack) -> {
				Multimap<Holder<Attribute>, AttributeModifier> modifier = LinkedHashMultimap.create();
				VirtueAttributeModifier.addAttributeModifier(modifier, healthBonus, id, Attributes.MAX_HEALTH, AttributeModifier.Operation.ADD_VALUE);
				return modifier;
			};
			return this;
		}

		/**
		 * 谨慎
		 *
		 * @param rationalityBonus 最大理智加成
		 */
		public Builder prudence(int rationalityBonus) {
			if (rationalityBonus == 0) {
				return this;
			}
			this.prudenceModifier = (entity, id, stack) -> {
				Multimap<Holder<Attribute>, AttributeModifier> modifier = LinkedHashMultimap.create();
				VirtueAttributeModifier.addAttributeModifier(modifier, rationalityBonus, id, LibAttributes.MAX_RATIONALITY, AttributeModifier.Operation.ADD_VALUE);
				return modifier;
			};
			return this;
		}

		/**
		 * 自律
		 *
		 * @param blockBreakSpeedBonus 挖掘速度加成
		 * @param attackKnockbackBonus 攻击击退加成
		 * @param workValueBonus       工作成功率和工作速度加成
		 */
		public Builder temperance(int blockBreakSpeedBonus, int attackKnockbackBonus, int workValueBonus) {
			return temperance(blockBreakSpeedBonus, attackKnockbackBonus, workValueBonus, workValueBonus);
		}

		/**
		 * 自律
		 *
		 * @param blockBreakSpeedBonus 挖掘速度加成
		 * @param attackKnockbackBonus 攻击击退加成
		 * @param workSuccessRateBonus 工作成功率加成
		 * @param workSpeedBonus       工作速度加成
		 */
		public Builder temperance(int blockBreakSpeedBonus, int attackKnockbackBonus, int workSuccessRateBonus, int workSpeedBonus) {
			if (blockBreakSpeedBonus == 0 && attackKnockbackBonus == 0) {
				return this;
			}
			this.temperanceModifier = (entity, id, stack) -> {
				Multimap<Holder<Attribute>, AttributeModifier> modifier = LinkedHashMultimap.create();
				// TODO 补充 成功率，工作速度
				VirtueAttributeModifier.addAttributeModifier(modifier, blockBreakSpeedBonus * VirtueTemperance.BLOCK_BREAK_SPEED, id, Attributes.BLOCK_BREAK_SPEED, AttributeModifier.Operation.ADD_VALUE);
				VirtueAttributeModifier.addAttributeModifier(modifier, attackKnockbackBonus * VirtueTemperance.ATTACK_KNOCKBACK_SPEED, id, Attributes.ATTACK_KNOCKBACK, AttributeModifier.Operation.ADD_VALUE);
				return modifier;
			};
			return this;
		}

		/**
		 * 自律
		 */
		public Builder temperance(int valueBonus) {
			return temperance(valueBonus, valueBonus, valueBonus, valueBonus);
		}

		/**
		 * 正义
		 *
		 * @param speedBonus       移动速度和游泳速度加成
		 * @param attackSpeedBonus 攻击速度加成
		 */
		public Builder justice(int speedBonus, int attackSpeedBonus) {
			return justice(speedBonus, speedBonus, attackSpeedBonus);
		}

		/**
		 * 正义
		 *
		 * @param movementSpeedBonus 移动速度加成
		 * @param swimSpeedBonus     游泳速度加成
		 * @param attackSpeedBonus   攻击速度加成
		 */
		public Builder justice(int movementSpeedBonus, int swimSpeedBonus, int attackSpeedBonus) {
			if (movementSpeedBonus == 0 && attackSpeedBonus == 0 && swimSpeedBonus == 0) {
				return this;
			}
			this.justiceModifier = (entity, id, stack) -> {
				Multimap<Holder<Attribute>, AttributeModifier> modifier = LinkedHashMultimap.create();
				VirtueAttributeModifier.addAttributeModifier(modifier, movementSpeedBonus * VirtueJustice.MOVEMENT_SPEED, id, Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.ADD_VALUE);
				VirtueAttributeModifier.addAttributeModifier(modifier, attackSpeedBonus * VirtueJustice.ATTACK_SPEED, id, Attributes.ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE);
				VirtueAttributeModifier.addAttributeModifier(modifier, swimSpeedBonus * VirtueJustice.SWIM_SPEED, id, NeoForgeMod.SWIM_SPEED, AttributeModifier.Operation.ADD_VALUE);
				return modifier;
			};
			return this;
		}

		/**
		 * 正义
		 */
		public Builder justice(int valueBonus) {
			return justice(valueBonus, valueBonus, valueBonus);
		}

		@FunctionalInterface
		public interface AttributeModifierBuilder {
			default void applyToEntity(LivingEntity entity, ResourceLocation id, ItemStack stack) {
				entity.getAttributes().addTransientAttributeModifiers(apply(entity, id, stack));
			}

			Multimap<Holder<Attribute>, AttributeModifier> apply(LivingEntity entity, ResourceLocation id, ItemStack stack);

			default void removeFromEntity(LivingEntity entity, ResourceLocation id, ItemStack stack) {
				entity.getAttributes().removeAttributeModifiers(apply(entity, id, stack));
			}
		}
	}
}
