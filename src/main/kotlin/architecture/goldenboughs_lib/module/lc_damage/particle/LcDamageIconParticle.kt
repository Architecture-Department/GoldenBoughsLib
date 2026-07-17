package architecture.goldenboughs_lib.module.lc_damage.particle

import architecture.goldenboughs_lib.api.AllOpen
import architecture.goldenboughs_lib.client.LibParticleRenderTypes
import architecture.goldenboughs_lib.init.LibParticleTypes
import architecture.goldenboughs_lib.module.lc_damage.api.LcDamageType
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.damagesource.DamageType
import net.neoforged.neoforge.common.Tags
import java.util.*

@AllOpen
class LcDamageIconParticle(
	level: ClientLevel,
	sprite: TextureAtlasSprite,
	x: Double,
	y: Double,
	z: Double,
	private val durationTick: Int
) : TextureSheetParticle(level, x, y, z) {
	init {
		setSprite(sprite)
	}

	override fun getLightColor(partialTick: Float): Int {
		return LightTexture.FULL_BRIGHT
	}

	override fun tick() {
		this.age++
		if (this.age > this.durationTick) {
			remove()
		}
	}

	override fun getRenderType(): ParticleRenderType {
		return LibParticleRenderTypes.LOBOTOMY_CORPORATION_DAMAGE_ICON_PARTICLE
	}

	enum class Type(@JvmField val index: Int, val texturePl: String) {
		PHYSICS(0, "damage_type/physics"),
		SPIRIT(1, "damage_type/spirit"),
		EROSION(2, "damage_type/erosion"),
		THE_SOUL(3, "damage_type/the_soul"),
		RATIONALITY_ADD(4, "damage_type/rationality_add"),
		RATIONALITY_REDUCE(5, "damage_type/rationality_reduce"),
		MAGIC(6, "damage_type/magic")
	}

	@JvmRecord
	data class Provider(val spriteSet: SpriteSet) : ParticleProvider<Options> {
		override fun createParticle(
			type: Options,
			level: ClientLevel,
			x: Double,
			y: Double,
			z: Double,
			xSpeed: Double,
			ySpeed: Double,
			zSpeed: Double
		): Particle {
			return LcDamageIconParticle(level, getTextureAtlasSprite(type), x, y, z, if (type.isHeal) 20 else 20 * 3)
		}

		private fun getTextureAtlasSprite(options: Options): TextureAtlasSprite {
			val isRationality = options.isRationality
			val isHeal = options.isHeal

			if (isRationality) {
				return getSprite(if (isHeal) Type.RATIONALITY_ADD else Type.RATIONALITY_REDUCE)
			}

			if (isHeal) {
				return getSprite(Type.PHYSICS)
			}

			val damageTypeResourceKey = options.damageType.orElse(null)
			val lcDamageTypeResourceKey = options.lcDamageType.orElse(null)

			if (lcDamageTypeResourceKey == null) {
				if (damageTypeResourceKey == null) {
					return getSprite(Type.PHYSICS)
				}
				if (damageTypeResourceKey.`is`(Tags.DamageTypes.IS_MAGIC)) {
					return getSprite(Type.MAGIC)
				}
			}

			return getSprite(
				when (lcDamageTypeResourceKey ?: damageTypeResourceKey?.run {
					LcDamageType.byDamageType(damageTypeResourceKey)
				}) {
					LcDamageType.SPIRIT -> Type.SPIRIT
					LcDamageType.EROSION -> Type.EROSION
					LcDamageType.THE_SOUL -> Type.THE_SOUL
					LcDamageType.PHYSICS, null -> Type.PHYSICS
				}
			)
		}

		private fun getSprite(type: Type): TextureAtlasSprite {
			return (this.spriteSet as ParticleEngine.MutableSpriteSet).sprites[type.index]
		}
	}

	@JvmRecord
	data class Options(
		val damageType: Optional<Holder<DamageType>>,
		val lcDamageType: Optional<LcDamageType>,
		val isRationality: Boolean,
		val isHeal: Boolean
	) : ParticleOptions {
		override fun getType(): ParticleType<Options> {
			return LibParticleTypes.LC_DAMAGE_ICON.get()
		}

		companion object {
			@JvmField
			val CODEC: MapCodec<Options> =
				RecordCodecBuilder.mapCodec {
					it.group(
						DamageType.CODEC.optionalFieldOf("damageType").forGetter(Options::damageType),
						LcDamageType.CODEC.optionalFieldOf("lcDamageType").forGetter(Options::lcDamageType),
						Codec.BOOL.fieldOf("isRationality").forGetter(Options::isRationality),
						Codec.BOOL.fieldOf("isHeal").forGetter(Options::isHeal)
					).apply(it, ::Options)
				}

			@JvmField
			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Options> =
				StreamCodec.composite(
					ByteBufCodecs.optional(DamageType.STREAM_CODEC),
					Options::damageType,
					ByteBufCodecs.optional(LcDamageType.STREAM_CODEC),
					Options::lcDamageType,
					ByteBufCodecs.BOOL,
					Options::isRationality,
					ByteBufCodecs.BOOL,
					Options::isHeal,
					::Options
				)
		}
	}
}
