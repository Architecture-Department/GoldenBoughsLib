package architecture.goldenboughs_lib.init;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * 伤害来源
 * <p>
 * 关于四色伤害的使用推荐直接使用{@link architecture.imaginarycraft.mixed.IDamageSource} 中的方法
 */
public class LibDamageSources extends DamageSources {
	private final DamageSource physics;
	private final DamageSource rationality;
	private final DamageSource erosion;
	private final DamageSource theSoul;
	private final DamageSource abnormalities;
	private final DamageSource ego;

	private final DamageSource melee;
	private final DamageSource remote;

	public LibDamageSources(RegistryAccess registry) {
		super(registry);
		physics = source(LibDamageTypes.PHYSICS);
		rationality = source(LibDamageTypes.SPIRIT);
		erosion = source(LibDamageTypes.EROSION);
		theSoul = source(LibDamageTypes.THE_SOUL);
		abnormalities = source(LibDamageTypes.ABNORMALITIES);
		ego = source(LibDamageTypes.EGO);
		melee = source(LibDamageTypes.MELEE);
		remote = source(LibDamageTypes.REMOTE);
	}

	public static @NotNull DamageSource meleeDamage(Entity causer) {
		return createDamage(LibDamageTypes.MELEE, causer);
	}

	public static @NotNull DamageSource createDamage(ResourceKey<DamageType> damageTypes, Entity causingEntity) {
		return createDamage(damageTypes, causingEntity, causingEntity, null);
	}

	public static @NotNull DamageSource createDamage(ResourceKey<DamageType> damageTypes, @Nullable Entity directEntity, @NotNull Entity causingEntity, Vec3 damageSourcePosition) {
		return createDamage(causingEntity.level().registryAccess(), damageTypes, directEntity, causingEntity, damageSourcePosition);
	}

	public static @NotNull DamageSource createDamage(RegistryAccess registryAccess, ResourceKey<DamageType> damageTypes, @Nullable Entity directEntity, @Nullable Entity causingEntity, Vec3 damageSourcePosition) {
		return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypes), directEntity, causingEntity, damageSourcePosition);
	}

	public static @NotNull DamageSource remoteDamage(Entity causer) {
		return createDamage(LibDamageTypes.REMOTE, causer);
	}

	@Contract("_ -> new")
	public static @NotNull DamageSource spiritDamage(Entity causer) {
		return createDamage(LibDamageTypes.SPIRIT, causer);
	}

	@Contract("_ -> new")
	public static @NotNull DamageSource erosionDamage(Entity causer) {
		return createDamage(LibDamageTypes.EROSION, causer);
	}

	@Contract("_ -> new")
	public static @NotNull DamageSource theSoulDamage(Entity causer) {
		return createDamage(LibDamageTypes.THE_SOUL, causer);
	}

	@Contract("_ -> new")
	public static @NotNull DamageSource physicsDamage(Entity causer) {
		return createDamage(LibDamageTypes.PHYSICS, causer);
	}

	@Contract("_ -> new")
	public static @NotNull DamageSource abnormalitiesDamage(Entity causer) {
		return createDamage(LibDamageTypes.ABNORMALITIES, causer);
	}

	@Contract("_ -> new")
	public static @NotNull DamageSource egoDamage(Entity causer) {
		return createDamage(LibDamageTypes.EGO, causer);
	}

	public static @NotNull DamageSource createDamage(ResourceKey<DamageType> damageTypes, @Nullable Entity directEntity, @NotNull Entity causingEntity) {
		return createDamage(damageTypes, directEntity, causingEntity, null);
	}

	public DamageSource physics() {
		return physics;
	}

	public DamageSource rationality() {
		return rationality;
	}

	public DamageSource erosion() {
		return erosion;
	}

	public DamageSource theSoul() {
		return theSoul;
	}

	public DamageSource abnormalities() {
		return abnormalities;
	}

	public DamageSource melee() {
		return melee;
	}

	public DamageSource remote() {
		return remote;
	}

	public DamageSource ego() {
		return ego;
	}
}
