package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.imaginarycraft.common.world.effect.ModMobEffect;
import architecture.imaginarycraft.datagen.i18n.ZhCn;
import architecture.imaginarycraft.init.world.item.ego.EgoWeaponItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yesman.epicfight.registry.entries.EpicFightAttributes;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LibMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = GoldenBoughsLib.modRegister(BuiltInRegistries.MOB_EFFECT);

	public static final Holder<MobEffect> RED_EYES_HUNTING = register("red_eyes_hunting", "赤瞳-狩猎", (category, color) ->
		new ModMobEffect(category, color) {
			@Override
			public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
				if (livingEntity.getMainHandItem().is(EgoWeaponItems.RED_EYES_TACHI) ||
					livingEntity.getOffhandItem().is(EgoWeaponItems.RED_EYES_TACHI)) {
					return super.applyEffectTick(livingEntity, amplifier);
				}
				return false;
			}

			@Override
			public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
				return true;
			}
		}, MobEffectCategory.BENEFICIAL, 0xac2323, (e, id) -> e
		.addAttributeModifier(EpicFightAttributes.MAX_STAMINA, id, 1, AttributeModifier.Operation.ADD_VALUE)
		.addAttributeModifier(EpicFightAttributes.IMPACT, id, -0.2, AttributeModifier.Operation.ADD_VALUE)
		.addAttributeModifier(Attributes.ATTACK_SPEED, id, 0.30, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
		.addAttributeModifier(Attributes.ATTACK_DAMAGE, id, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

	//TODO:改颜色码
	public static final Holder<MobEffect> PHYSIC_ABSORPTION_SHIELD = register("physic_absorption_shield", "物理吸收护盾",
		ModMobEffect::new, MobEffectCategory.BENEFICIAL, LcDamageType.PHYSICS.getColourValue());
	public static final Holder<MobEffect> SPIRIT_ABSORPTION_SHIELD = register("spirit_absorption_shield", "精神吸收护盾",
		ModMobEffect::new, MobEffectCategory.BENEFICIAL, LcDamageType.SPIRIT.getColourValue());
	public static final Holder<MobEffect> EROSION_ABSORPTION_SHIELD = register("erosion_absorption_shield", "侵蚀吸收护盾",
		ModMobEffect::new, MobEffectCategory.BENEFICIAL, LcDamageType.EROSION.getColourValue());
	public static final Holder<MobEffect> SOUL_ABSORPTION_SHIELD = register("soul_absorption_shield", "灵魂吸收护盾",
		ModMobEffect::new, MobEffectCategory.BENEFICIAL, LcDamageType.SPIRIT.getColourValue());

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, BiFunction<MobEffectCategory, Integer, T> biFunction, MobEffectCategory category, int color) {
		return register(name, zhCnText, () -> biFunction.apply(category, color));
	}

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, Supplier<T> supplier) {
		DeferredHolder<MobEffect, T> holder = REGISTRY.register(name, supplier);
		ZhCn.addI18nMobEffectText(zhCnText, holder);
		return holder;
	}

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, Supplier<T> supplier, Function<T, MobEffect> function) {
		return register(name, zhCnText, () -> {
			T apply = supplier.get();
			function.apply(apply);
			return apply;
		});
	}

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, Supplier<T> supplier, BiFunction<T, ResourceLocation, MobEffect> function) {
		return register(name, zhCnText, () -> {
			T apply = supplier.get();
			function.apply(apply, GoldenBoughsLib.modRl(name));
			return apply;
		});
	}

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, BiFunction<MobEffectCategory, Integer, T> biFunction, MobEffectCategory category, int color, Function<T, MobEffect> function) {
		return register(name, zhCnText, () -> {
			T apply = biFunction.apply(category, color);
			function.apply(apply);
			return apply;
		});
	}

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, BiFunction<MobEffectCategory, Integer, T> biFunction, MobEffectCategory category, int color, BiFunction<T, ResourceLocation, MobEffect> function) {
		return register(name, zhCnText, () -> {
			T apply = biFunction.apply(category, color);
			function.apply(apply, GoldenBoughsLib.modRl(name));
			return apply;
		});
	}
}
