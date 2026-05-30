package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.core.LibConstants.modRegister
import architecture.goldenboughs_lib.core.LibConstants.modRl
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn
import architecture.goldenboughs_lib.module.lc_damage.api.LcDamageType
import architecture.goldenboughs_lib.module.shield.mobeffect.MobEffectExpand
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

object LibMobEffects {
	@JvmField
	val REGISTRY: DeferredRegister<MobEffect> = modRegister(BuiltInRegistries.MOB_EFFECT)

	@JvmField
	val PHYSIC_ABSORPTION_SHIELD: Holder<MobEffect> = register(
		"physic_absorption_shield",
		"物理吸收护盾",
		::MobEffectExpand,
		MobEffectCategory.BENEFICIAL,
		LcDamageType.PHYSICS.colourValue
	)

	@JvmField
	val SPIRIT_ABSORPTION_SHIELD: Holder<MobEffect> = register(
		"spirit_absorption_shield",
		"精神吸收护盾",
		::MobEffectExpand,
		MobEffectCategory.BENEFICIAL,
		LcDamageType.SPIRIT.colourValue
	)

	@JvmField
	val EROSION_ABSORPTION_SHIELD: Holder<MobEffect> = register(
		"erosion_absorption_shield",
		"侵蚀吸收护盾",
		::MobEffectExpand,
		MobEffectCategory.BENEFICIAL,
		LcDamageType.EROSION.colourValue
	)

	@JvmField
	val SOUL_ABSORPTION_SHIELD: Holder<MobEffect> = register(
		"soul_absorption_shield",
		"灵魂吸收护盾",
		::MobEffectExpand,
		MobEffectCategory.BENEFICIAL,
		LcDamageType.SPIRIT.colourValue
	)

	private fun <T : MobEffect> register(
		name: String,
		zhCnText: String,
		biFunction: BiFunction<MobEffectCategory, Int, T>,
		category: MobEffectCategory,
		color: Int
	): DeferredHolder<MobEffect, T> {
		return register(name, zhCnText) { biFunction.apply(category, color) }
	}

	private fun <T : MobEffect> register(
		name: String, zhCnText: String, supplier: Supplier<T>
	): DeferredHolder<MobEffect, T> {
		val holder = REGISTRY.register(name, supplier)
		LibZhCn.addI18nMobEffectText(zhCnText, holder)
		return holder
	}

	private fun <T : MobEffect> register(
		name: String, zhCnText: String, supplier: Supplier<T>, function: Function<T, MobEffect>
	): DeferredHolder<MobEffect, T> {
		return register(name, zhCnText) {
			val apply = supplier.get()
			function.apply(apply)
			apply
		}
	}

	private fun <T : MobEffect> register(
		name: String, zhCnText: String, supplier: Supplier<T>, function: BiFunction<T, ResourceLocation, MobEffect>
	): DeferredHolder<MobEffect, T> {
		return register(name, zhCnText) {
			val apply = supplier.get()
			function.apply(apply, modRl(name))
			apply
		}
	}

	private fun <T : MobEffect> register(
		name: String,
		zhCnText: String,
		biFunction: BiFunction<MobEffectCategory, Int, T>,
		category: MobEffectCategory,
		color: Int,
		function: Function<T, MobEffect>
	): DeferredHolder<MobEffect, T> {
		return register(name, zhCnText) {
			val apply = biFunction.apply(category, color)
			function.apply(apply)
			apply
		}
	}

	private fun <T : MobEffect> register(
		name: String,
		zhCnText: String,
		biFunction: BiFunction<MobEffectCategory, Int, T>,
		category: MobEffectCategory,
		color: Int,
		function: BiFunction<T, ResourceLocation, MobEffect>
	): DeferredHolder<MobEffect, T> {
		return register(name, zhCnText) {
			val apply = biFunction.apply(category, color)
			function.apply(apply, modRl(name))
			apply
		}
	}
}
