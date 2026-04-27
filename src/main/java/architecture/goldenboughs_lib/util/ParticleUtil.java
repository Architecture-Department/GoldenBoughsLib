package architecture.goldenboughs_lib.util;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.client.LibFontIcon;
import architecture.goldenboughs_lib.client.particle.text.*;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static architecture.goldenboughs_lib.api.LcDamageType.PHYSICS;

public final class ParticleUtil {

	public static void randomDamageTextParticles(
		ServerLevel world,
		Component component,
		boolean isRationality,
		boolean isHeal,
		double x,
		double y,
		double z
	) {
		randomDamageTextParticles(world, component, null, null, isRationality, isHeal, x, y, z);
	}

	public static void randomDamageTextParticles(
		ServerLevel world,
		Component component,
		@Nullable Holder<DamageType> damageType,
		@Nullable LcDamageType lcDamageType,
		boolean isRationality,
		boolean isHeal,
		double x,
		double y,
		double z
	) {
		createDamageTextParticles(world, component, damageType, lcDamageType, isRationality, isHeal, x, y, z, 0.1, 0.1, 0.1);
	}

	//region 伤害文本粒子
	public static void createDamageTextParticles(
		ServerLevel world,
		Component component,
		@Nullable Holder<DamageType> damageTypeHolder,
		@Nullable LcDamageType lcDamageType,
		boolean isRationality,
		boolean isHeal,
		double x,
		double y,
		double z,
		double xOffset,
		double yOffset,
		double zOffset
	) {
		TextParticleOptions built = getBuild(component, damageTypeHolder, lcDamageType, isRationality, isHeal)
			.align(TextParticleAlignType.CENTER)
			.targetingPlayers(true)
			.particleLifeTime(isHeal ? 20 : 20 * 3)
			.strokeType(TextParticleStrokeType.SHADOW)
			.shine(true)
			.seeThrough(true)
			.buildOptions();
		world.sendParticles(new DamageTextParticle.Options(built, isHeal), x, y, z, 1, xOffset, yOffset, zOffset, 0);
	}

	private static TextParticleBuilder getBuild(
		final Component component,
		final @Nullable Holder<DamageType> damageTypeHolder,
		final @Nullable LcDamageType lcDamageType,
		final boolean isRationality,
		final boolean isHeal
	) {
		TextParticleBuilder builder = new TextParticleBuilder();
		Component iconComponent;
		int fontColor;
		int strokeColor;
		if (isRationality) {
			if (isHeal) {
				iconComponent = LibFontIcon.RATIONALITY_ADD.getComponent();
				fontColor = 0x78f5ff;
				strokeColor = 0x2c80d0;
			} else {
				iconComponent = LibFontIcon.RATIONALITY_REDUCE.getComponent();
				fontColor = 0xA81919;
				strokeColor = 0x4d0000;
			}
			return builder.addTextComponent(Component.empty()
					.append(iconComponent)
					.append(component))
				.fontColor(fontColor)
				.strokeColor(strokeColor);
		}
		if (isHeal) {
			return builder.addTextComponent(component)
				.strokeColor(0x1c501f)
				.fontColor(0x89ff6a);
		}

		final LcDamageType type;
		if (lcDamageType != null) {
			type = lcDamageType;
		} else {
			if (damageTypeHolder != null) {
				type = LcDamageType.byDamageType(damageTypeHolder);
			} else {
				type = null;
			}
		}

		if (type != null) {
			iconComponent = type.getChar().getComponent();
			fontColor = type.getColourValue();
			strokeColor = switch (type) {
				case SPIRIT -> 0x9c4e80;
				case EROSION -> 0x28054a;
				case THE_SOUL -> 0x074161;
				case PHYSICS -> 0x4d0000;
			};
		} else {
			if (damageTypeHolder != null && damageTypeHolder.is(Tags.DamageTypes.IS_MAGIC)) {
				iconComponent = LibFontIcon.MAGIC.getComponent();
				fontColor = 0x8a2be2;
				strokeColor = 0x28054a;
			} else {
				iconComponent = PHYSICS.getChar().getComponent();
				fontColor = PHYSICS.getColourValue();
				strokeColor = 0x4d0000;
			}
		}

		return builder.addTextComponent(Component.empty().append(iconComponent)
				.append(component))
			.fontColor(fontColor)
			.strokeColor(strokeColor);
	}

	public static void createDamageTextParticles(LivingEntity entity, Component component, boolean isRationality, boolean isHeal) {
		createDamageTextParticles(entity, component, null, null, isRationality, isHeal);
	}

	public static void createDamageTextParticles(
		LivingEntity entity,
		Component component,
		@Nullable Holder<DamageType> damageType,
		@Nullable LcDamageType lcDamageType,
		boolean isRationality,
		boolean isHeal
	) {
		if (!(entity.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		Vec3 pos = entity.position();
		AABB aabb = entity.getHitbox();
		double xOffset = (aabb.maxX - aabb.minX) / 2;
		double yOffset = (aabb.maxY - aabb.minY) / 2;
		double zOffset = (aabb.maxZ - aabb.minZ) / 2;
		double x = pos.x;
		double y = pos.y + yOffset;
		double z = pos.z;
		createDamageTextParticles(serverLevel, component, damageType, lcDamageType, isRationality, isHeal,
			x, y, z, xOffset / 2, yOffset / 2, zOffset / 2);
	}

	public static void createDamageTextParticles(LivingEntity entity, float value, boolean isRationality, boolean isHeal) {
		createDamageTextParticles(entity, getText(value, isHeal), null, null, isRationality, isHeal);
	}

	// TODO 新增免疫字段
	// TODO 新增吸收字段
	// TODO 新增无效字段
	public static @NotNull MutableComponent getText(float value, boolean isHeal) {
		return Component.literal((isHeal ? "+%s" : "-%s").formatted(TextUtil.formatNumberPlaces(Math.abs(value), 2)));
	}

	public static void createDamageTextParticles(
		LivingEntity entity,
		@Nullable Holder<DamageType> damageType,
		@Nullable LcDamageType lcDamageType,
		float value,
		boolean isRationality,
		boolean isHeal
	) {
		createDamageTextParticles(entity, getText(value, isHeal), damageType, lcDamageType, isRationality, isHeal);
	}

	public static @NotNull MutableComponent getText(float value) {
		return getText(value, value > 0);
	}
	//endregion
}
