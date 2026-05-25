package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.core.LibConstants.modRl
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DeathMessageType
import architecture.goldenboughs_lib.core.LibConstants

object LibDamageTypes {
	/**
	 * 物理
	 */
	@JvmField
	val PHYSICS: ResourceKey<DamageType> = register("physics")

	/**
	 * 精神
	 */
	@JvmField
	val SPIRIT: ResourceKey<DamageType> = register("spirit")

	/**
	 * 侵蚀
	 */
	@JvmField
	val EROSION: ResourceKey<DamageType> = register("erosion")

	/**
	 * 灵魂
	 */
	@JvmField
	val THE_SOUL: ResourceKey<DamageType> = register("the_soul")

	/**
	 * 异想体
	 */
	@JvmField
	val ABNORMALITIES: ResourceKey<DamageType> = register("abnormalities")

	/**
	 * E.G.O
	 *
	 *
	 * Extermination of Geometrical Organ 是的没错这玩意的全称就是这么长
	 */
	@JvmField
	val EGO: ResourceKey<DamageType> = register("ego")

	/**
	 * 近战伤害
	 */
	@JvmField
	val MELEE: ResourceKey<DamageType> = register("melee")

	/**
	 * 远程伤害
	 */
	@JvmField
	val REMOTE: ResourceKey<DamageType> = register("remote")

	fun bootstrap(context: BootstrapContext<DamageType>) {
		register(
			context,
			"physics",
			PHYSICS,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.1f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
		register(
			context,
			"spirit",
			SPIRIT,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.2f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
		register(
			context,
			"erosion",
			EROSION,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.3f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
		register(
			context,
			"theSoul",
			THE_SOUL,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.4f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
		register(
			context,
			"abnormalities",
			ABNORMALITIES,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.3f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
		register(
			context,
			"ego",
			EGO,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.3f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
		register(
			context,
			"melee",
			MELEE,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.0f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
		register(
			context,
			"remote",
			REMOTE,
			DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
			0.0f,
			DamageEffects.HURT,
			DeathMessageType.DEFAULT
		)
	}

	private fun register(
		context: BootstrapContext<DamageType>,
		name: String,
		damageType: ResourceKey<DamageType>,
		damageScaling: DamageScaling,
		exhaustion: Float,
		damageEffects: DamageEffects,
		deathMessageType: DeathMessageType
	): Holder.Reference<DamageType> {
		return register(
			context, damageType, DamageType(name, damageScaling, exhaustion, damageEffects, deathMessageType)
		)
	}

	private fun register(
		context: BootstrapContext<DamageType>, damageType: ResourceKey<DamageType>, damageType1: DamageType
	): Holder.Reference<DamageType> {
		return context.register(damageType, damageType1)
	}

	/**
	 * 创建伤害类型
	 */
	private fun register(name: String): ResourceKey<DamageType> {
		return ResourceKey.create(Registries.DAMAGE_TYPE, LibConstants.modRl(name))
	}

	private fun register(
		context: BootstrapContext<DamageType>, name: String, damageType: ResourceKey<DamageType>, exhaustion: Float
	): Holder.Reference<DamageType> {
		return register(
			context, name, damageType, DamageScaling.ALWAYS, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT
		)
	}
}
