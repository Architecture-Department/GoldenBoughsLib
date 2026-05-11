package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.core.Lib;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import org.jetbrains.annotations.NotNull;

public final class LibDamageTypes {
	/**
	 * 物理
	 */
	public static final ResourceKey<DamageType> PHYSICS = register("physics");
	/**
	 * 精神
	 */
	public static final ResourceKey<DamageType> SPIRIT = register("spirit");
	/**
	 * 侵蚀
	 */
	public static final ResourceKey<DamageType> EROSION = register("erosion");
	/**
	 * 灵魂
	 */
	public static final ResourceKey<DamageType> THE_SOUL = register("the_soul");
	/**
	 * 异想体
	 */
	public static final ResourceKey<DamageType> ABNORMALITIES = register("abnormalities");
	/**
	 * E.G.O
	 * <p>
	 * Extermination of Geometrical Organ 是的没错这玩意的全称就是这么长
	 */
	public static final ResourceKey<DamageType> EGO = register("ego");
	/**
	 * 近战伤害
	 */
	public static final ResourceKey<DamageType> MELEE = register("melee");
	/**
	 * 远程伤害
	 */
	public static final ResourceKey<DamageType> REMOTE = register("remote");

	public static void bootstrap(BootstrapContext<DamageType> context) {
		register(context, "physics", PHYSICS, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f, DamageEffects.HURT, DeathMessageType.DEFAULT);
		register(context, "spirit", SPIRIT, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.2f, DamageEffects.HURT, DeathMessageType.DEFAULT);
		register(context, "erosion", EROSION, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.3f, DamageEffects.HURT, DeathMessageType.DEFAULT);
		register(context, "theSoul", THE_SOUL, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.4f, DamageEffects.HURT, DeathMessageType.DEFAULT);
		register(context, "abnormalities", ABNORMALITIES, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.3f, DamageEffects.HURT, DeathMessageType.DEFAULT);
		register(context, "ego", EGO, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.3f, DamageEffects.HURT, DeathMessageType.DEFAULT);
		register(context, "melee", MELEE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f, DamageEffects.HURT, DeathMessageType.DEFAULT);
		register(context, "remote", REMOTE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f, DamageEffects.HURT, DeathMessageType.DEFAULT);
	}

	private static Holder.@NotNull Reference<DamageType> register(
		final BootstrapContext<DamageType> context,
		final String name,
		final ResourceKey<DamageType> damageType,
		final DamageScaling damageScaling,
		final float exhaustion,
		final DamageEffects damageEffects,
		final DeathMessageType deathMessageType
	) {
		return register(context, damageType, new DamageType(name, damageScaling, exhaustion, damageEffects, deathMessageType));
	}

	private static Holder.@NotNull Reference<DamageType> register(
		final @NotNull BootstrapContext<DamageType> context,
		final ResourceKey<DamageType> damageType,
		final DamageType damageType1
	) {
		return context.register(damageType, damageType1);
	}

	/**
	 * 创建伤害类型
	 */
	private static @NotNull ResourceKey<DamageType> register(final String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, Lib.modRl(name));
	}

	private static Holder.@NotNull Reference<DamageType> register(
		final BootstrapContext<DamageType> context,
		final String name,
		final ResourceKey<DamageType> damageType,
		float exhaustion
	) {
		return register(context, name, damageType, DamageScaling.ALWAYS, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT);
	}
}
