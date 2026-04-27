package architecture.goldenboughs_lib.client.particle;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.init.LibParticleTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LcDamageIconParticle extends TextureSheetParticle {

	private final int durationTick;

	protected LcDamageIconParticle(
		final ClientLevel level,
		final TextureAtlasSprite sprite,
		final double x,
		final double y,
		final double z,
		final int durationTick
	) {
		super(level, x, y, z);
		this.durationTick = durationTick;
		setSprite(sprite);
	}

	@Override
	protected int getLightColor(final float partialTick) {
		return LightTexture.FULL_BRIGHT;
	}

	@Override
	public void tick() {
		this.age++;
		if (this.age > this.durationTick) {
			remove();
		}
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ModParticleRenderTypes.LOBOTOMY_CORPORATION_DAMAGE_ICON_PARTICLE;
	}

	public enum Type {
		PHYSICS(0, "damage_type/physics"),
		SPIRIT(1, "damage_type/spirit"),
		EROSION(2, "damage_type/erosion"),
		THE_SOUL(3, "damage_type/the_soul"),
		RATIONALITY_ADD(4, "damage_type/rationality_add"),
		RATIONALITY_REDUCE(5, "damage_type/rationality_reduce"),
		MAGIC(6, "damage_type/magic");

		private final int index;
		private final @javax.annotation.Nullable String texturePl;

		Type(final int index, @javax.annotation.Nullable final String texturePl) {
			this.index = index;
			this.texturePl = texturePl;
		}

		public @javax.annotation.Nullable String getTexturePl() {
			return texturePl;
		}

		public int getIndex() {
			return index;
		}
	}

	public record Provider(
		SpriteSet spriteSet) implements ParticleProvider<Options> {
		@Override
		@NotNull
		public Particle createParticle(@NotNull LcDamageIconParticle.Options type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new LcDamageIconParticle(level, getTextureAtlasSprite(type), x, y, z, type.isHeal() ? 20 : 20 * 3);
		}

		private TextureAtlasSprite getTextureAtlasSprite(@NotNull LcDamageIconParticle.Options options) {
			boolean isRationality = options.isRationality();
			boolean isHeal = options.isHeal();

			if (isRationality) {
				return getSprite(isHeal ? Type.RATIONALITY_ADD : Type.RATIONALITY_REDUCE);
			}

			if (isHeal) {
				return getSprite(Type.PHYSICS);
			}

			@Nullable Holder<DamageType> damageTypeResourceKey = options.damageType().orElse(null);
			@Nullable LcDamageType lcDamageTypeResourceKey = options.lcDamageType().orElse(null);

			if (lcDamageTypeResourceKey == null) {
				if (damageTypeResourceKey == null) {
					return getSprite(Type.PHYSICS);
				}
				if (damageTypeResourceKey.is(Tags.DamageTypes.IS_MAGIC)) {
					return getSprite(Type.MAGIC);
				}
			}
			return getSprite(switch (lcDamageTypeResourceKey != null ?
				lcDamageTypeResourceKey : LcDamageType.byDamageType(damageTypeResourceKey)) {
				case SPIRIT -> Type.SPIRIT;
				case EROSION -> Type.EROSION;
				case THE_SOUL -> Type.THE_SOUL;
				case null, default -> Type.PHYSICS;
			});
		}

		private TextureAtlasSprite getSprite(Type type) {
			return ((ParticleEngine.MutableSpriteSet) this.spriteSet).sprites.get(type.getIndex());
		}
	}

	public record Options(Optional<@Nullable Holder<DamageType>> damageType,
	                      Optional<@Nullable LcDamageType> lcDamageType,
	                      boolean isRationality,
	                      boolean isHeal) implements ParticleOptions {
		public static final MapCodec<Options> CODEC = RecordCodecBuilder.mapCodec((thisOptionsInstance) -> thisOptionsInstance.group(
				DamageType.CODEC.optionalFieldOf("damageType").forGetter(Options::damageType),
				LcDamageType.CODEC.optionalFieldOf("lcDamageType").forGetter(Options::lcDamageType),
				Codec.BOOL.fieldOf("isRationality").forGetter(Options::isRationality),
				Codec.BOOL.fieldOf("isHeal").forGetter(Options::isHeal))
			.apply(thisOptionsInstance, Options::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(DamageType.STREAM_CODEC), Options::damageType,
			ByteBufCodecs.optional(LcDamageType.STREAM_CODEC), Options::lcDamageType,
			ByteBufCodecs.BOOL, Options::isRationality,
			ByteBufCodecs.BOOL, Options::isHeal,
			Options::new);

		@Override
		public @NotNull ParticleType<Options> getType() {
			return LibParticleTypes.LC_DAMAGE_ICON.get();
		}
	}
}
