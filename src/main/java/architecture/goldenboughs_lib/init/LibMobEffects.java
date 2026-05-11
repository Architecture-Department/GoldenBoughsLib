package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.common.mobeffect.MobEffectExpand;
import architecture.goldenboughs_lib.core.Lib;
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LibMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = Lib.modRegister(BuiltInRegistries.MOB_EFFECT);

	public static final Holder<MobEffect> PHYSIC_ABSORPTION_SHIELD = register("physic_absorption_shield", "物理吸收护盾",
		MobEffectExpand::new, MobEffectCategory.BENEFICIAL, LcDamageType.PHYSICS.getColourValue());
	public static final Holder<MobEffect> SPIRIT_ABSORPTION_SHIELD = register("spirit_absorption_shield", "精神吸收护盾",
		MobEffectExpand::new, MobEffectCategory.BENEFICIAL, LcDamageType.SPIRIT.getColourValue());
	public static final Holder<MobEffect> EROSION_ABSORPTION_SHIELD = register("erosion_absorption_shield", "侵蚀吸收护盾",
		MobEffectExpand::new, MobEffectCategory.BENEFICIAL, LcDamageType.EROSION.getColourValue());
	public static final Holder<MobEffect> SOUL_ABSORPTION_SHIELD = register("soul_absorption_shield", "灵魂吸收护盾",
		MobEffectExpand::new, MobEffectCategory.BENEFICIAL, LcDamageType.SPIRIT.getColourValue());

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, BiFunction<MobEffectCategory, Integer, T> biFunction, MobEffectCategory category, int color) {
		return register(name, zhCnText, () -> biFunction.apply(category, color));
	}

	private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, String zhCnText, Supplier<T> supplier) {
		DeferredHolder<MobEffect, T> holder = REGISTRY.register(name, supplier);
		LibZhCn.addI18nMobEffectText(zhCnText, holder);
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
			function.apply(apply, Lib.modRl(name));
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
			function.apply(apply, Lib.modRl(name));
			return apply;
		});
	}
}
