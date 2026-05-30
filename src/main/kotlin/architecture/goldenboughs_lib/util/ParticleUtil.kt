package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.client.LibFontIcon
import architecture.goldenboughs_lib.client.particle.text.TextParticleAlignType
import architecture.goldenboughs_lib.client.particle.text.TextParticleBuilder
import architecture.goldenboughs_lib.client.particle.text.TextParticleStrokeType
import architecture.goldenboughs_lib.module.lc_damage.api.LcDamageType
import architecture.goldenboughs_lib.module.lc_damage.particle.DamageTextParticle
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.common.Tags
import kotlin.math.abs

object ParticleUtil {
	@JvmStatic
	fun randomDamageTextParticles(
		world: ServerLevel,
		component: Component,
		isRationality: Boolean,
		isHeal: Boolean,
		x: Double,
		y: Double,
		z: Double
	) {
		randomDamageTextParticles(world, component, null, null, isRationality, isHeal, x, y, z)
	}

	@JvmStatic
	fun randomDamageTextParticles(
		world: ServerLevel,
		component: Component,
		damageType: Holder<DamageType>?,
		lcDamageType: LcDamageType?,
		isRationality: Boolean,
		isHeal: Boolean,
		x: Double,
		y: Double,
		z: Double
	) {
		createDamageTextParticles(
			world,
			component,
			damageType,
			lcDamageType,
			isRationality,
			isHeal,
			x,
			y,
			z,
			0.1,
			0.1,
			0.1
		)
	}

	//region 伤害文本粒子
	@JvmStatic
	fun createDamageTextParticles(
		world: ServerLevel,
		component: Component,
		damageTypeHolder: Holder<DamageType>?,
		lcDamageType: LcDamageType?,
		isRationality: Boolean,
		isHeal: Boolean,
		x: Double,
		y: Double,
		z: Double,
		xOffset: Double,
		yOffset: Double,
		zOffset: Double
	) {
		val built = getBuild(component, damageTypeHolder, lcDamageType, isRationality, isHeal)
			.align(TextParticleAlignType.CENTER)
			.targetingPlayers(true)
			.particleLifeTime(if (isHeal) 20 else 20 * 3)
			.strokeType(TextParticleStrokeType.SHADOW)
			.shine(true)
			.seeThrough(true)
			.buildOptions()
		world.sendParticles(
			DamageTextParticle.Options(built, isHeal),
			x,
			y,
			z,
			1,
			xOffset,
			yOffset,
			zOffset,
			0.0
		)
	}

	@JvmStatic
	private fun getBuild(
		component: Component,
		damageTypeHolder: Holder<DamageType>?,
		lcDamageType: LcDamageType?,
		isRationality: Boolean,
		isHeal: Boolean
	): TextParticleBuilder {
		val builder = TextParticleBuilder()
		val iconComponent: Component
		val fontColor: Int
		val strokeColor: Int
		if (isRationality) {
			if (isHeal) {
				iconComponent = LibFontIcon.RATIONALITY_ADD.component
				fontColor = 0x78f5ff
				strokeColor = 0x2c80d0
			} else {
				iconComponent = LibFontIcon.RATIONALITY_REDUCE.component
				fontColor = 0xA81919
				strokeColor = 0x4d0000
			}
			return builder.addTextComponent(
				Component.empty()
					.append(iconComponent)
					.append(component)
			)
				.fontColor(fontColor)
				.strokeColor(strokeColor)
		}
		if (isHeal) {
			return builder.addTextComponent(component)
				.strokeColor(0x1c501f)
				.fontColor(0x89ff6a)
		}

		val type = lcDamageType ?: if (damageTypeHolder != null) {
			LcDamageType.byDamageType(damageTypeHolder)
		} else {
			null
		}

		if (type != null) {
			iconComponent = type.charIcon.component
			fontColor = type.colourValue
			strokeColor = when (type) {
				LcDamageType.SPIRIT -> 0x9c4e80
				LcDamageType.EROSION -> 0x28054a
				LcDamageType.THE_SOUL -> 0x074161
				LcDamageType.PHYSICS -> 0x4d0000
			}
		} else {
			if (damageTypeHolder != null && damageTypeHolder.`is`(Tags.DamageTypes.IS_MAGIC)) {
				iconComponent = LibFontIcon.MAGIC.component
				fontColor = 0x8a2be2
				strokeColor = 0x28054a
			} else {
				iconComponent = LcDamageType.PHYSICS.charIcon.component
				fontColor = LcDamageType.PHYSICS.colourValue
				strokeColor = 0x4d0000
			}
		}

		return builder.addTextComponent(
			Component.empty().append(iconComponent)
				.append(component)
		)
			.fontColor(fontColor)
			.strokeColor(strokeColor)
	}

	@JvmStatic
	fun createDamageTextParticles(entity: LivingEntity, component: Component, isRationality: Boolean, isHeal: Boolean) {
		createDamageTextParticles(entity, component, null, null, isRationality, isHeal)
	}

	@JvmStatic
	fun createDamageTextParticles(
		entity: LivingEntity,
		component: Component,
		damageType: Holder<DamageType>?,
		lcDamageType: LcDamageType?,
		isRationality: Boolean,
		isHeal: Boolean
	) {
		(entity.level() as? ServerLevel)?.apply {
			val pos = entity.position()
			val aabb = entity.hitbox
			val xOffset = (aabb.maxX - aabb.minX) / 2
			val yOffset = (aabb.maxY - aabb.minY) / 2
			val zOffset = (aabb.maxZ - aabb.minZ) / 2
			val x = pos.x
			val y = pos.y + yOffset
			val z = pos.z
			createDamageTextParticles(
				this, component, damageType, lcDamageType, isRationality, isHeal,
				x, y, z, xOffset / 2, yOffset / 2, zOffset / 2
			)
		}
	}

	@JvmStatic
	fun createDamageTextParticles(entity: LivingEntity, value: Float, isRationality: Boolean, isHeal: Boolean) {
		createDamageTextParticles(entity, getText(value, isHeal), null, null, isRationality, isHeal)
	}

	// TODO 新增免疫字段
	// TODO 新增吸收字段
	// TODO 新增无效字段
	@JvmStatic
	fun getText(value: Float, isHeal: Boolean): MutableComponent {
		val string = if (isHeal) "+%s" else "-%s"
		val args = TextUtil.formatNumberPlaces(abs(value).toDouble(), 2)
		return Component.literal(string.format(args))
	}

	@JvmStatic
	fun createDamageTextParticles(
		entity: LivingEntity,
		damageType: Holder<DamageType>?,
		lcDamageType: LcDamageType?,
		value: Float,
		isRationality: Boolean,
		isHeal: Boolean
	) {
		createDamageTextParticles(entity, getText(value, isHeal), damageType, lcDamageType, isRationality, isHeal)
	}

	@JvmStatic
	fun getText(value: Float): MutableComponent {
		return getText(value, value > 0)
	}
	//endregion
}
