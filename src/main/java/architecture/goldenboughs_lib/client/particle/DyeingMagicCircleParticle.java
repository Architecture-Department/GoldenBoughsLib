package architecture.goldenboughs_lib.client.particle;

import architecture.goldenboughs_lib.client.LibParticleRenderTypes;
import architecture.goldenboughs_lib.init.LibParticleTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.List;

public class DyeingMagicCircleParticle extends TextureSheetParticle {
	private float xRot;
	private float yRot;
	private int color;

	protected DyeingMagicCircleParticle(
		TextureAtlasSprite sprite,
		ClientLevel level,
		double x,
		double y,
		double z,
		float xRot,
		float yRot,
		int color,
		float radius,
		int particleLifeTime
	) {
		super(level, x, y, z);
		setLifetime(particleLifeTime);
		setSprite(sprite);
		this.quadSize = radius;
		setSize(radius, radius);
		this.xRot = xRot;
		this.yRot = yRot;
		this.color = color;
	}

	public float getXRot() {
		return xRot;
	}

	public void setXRot(float xRot) {
		this.xRot = xRot;
	}

	public float getYRot() {
		return yRot;
	}

	public void setYRot(float yRot) {
		this.yRot = yRot;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		setColor(color << 24, color << 16, color << 8);
	}

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		Quaternionf quaternionf = new Quaternionf();
		quaternionf.rotateX(xRot * Mth.DEG_TO_RAD);
		quaternionf.rotateY(yRot * Mth.DEG_TO_RAD);
		this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
	}

	@Override
	protected int getLightColor(float partialTick) {
		return LightTexture.FULL_BRIGHT;
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return LibParticleRenderTypes.MAGIC_CIRCLE_PARTICLE;
	}

	public static class Builder {
		protected final float xRot;
		protected final float yRot;
		protected int color = 0xFFFFFF;
		protected float radius;
		protected int particleLifeTime = 20;

		public Builder(float xRot, float yRot) {
			this.xRot = xRot;
			this.yRot = yRot;
		}

		public Builder color(int color) {
			this.color = color;
			return this;
		}

		public Builder radius(float radius) {
			this.radius = radius;
			return this;
		}

		public Builder particleLifeTime(int particleLifeTime) {
			this.particleLifeTime = particleLifeTime;
			return this;
		}

		public Options buildOptions(int index) {
			return new Options(this.xRot, this.yRot, this.color, this.radius, this.particleLifeTime, index);
		}
	}

	public record Options(float xRot, float yRot, int color,
	                      float radius, int particleLifeTime, int index) implements ParticleOptions {
		public static final MapCodec<Options> CODEC = RecordCodecBuilder.mapCodec((thisOptionsInstance) -> thisOptionsInstance.group(
			Codec.FLOAT.fieldOf("xRot").forGetter(Options::xRot),
			Codec.FLOAT.fieldOf("yRot").forGetter(Options::yRot),
			Codec.INT.fieldOf("color").forGetter(Options::color),
			Codec.FLOAT.fieldOf("radius").forGetter(Options::radius),
			Codec.INT.fieldOf("particleLifeTime").forGetter(Options::particleLifeTime),
			Codec.INT.fieldOf("index").forGetter(Options::index)
		).apply(thisOptionsInstance, Options::new));

		public static final StreamCodec<ByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, Options::xRot,
			ByteBufCodecs.FLOAT, Options::yRot,
			ByteBufCodecs.INT, Options::color,
			ByteBufCodecs.FLOAT, Options::radius,
			ByteBufCodecs.INT, Options::particleLifeTime,
			ByteBufCodecs.INT, Options::index,
			Options::new);

		@Override
		public @NotNull ParticleType<Options> getType() {
			return LibParticleTypes.DYEING_MAGIC_CIRCLE.get();
		}
	}

	public static class Provider implements ParticleProvider<Options> {
		private final SpriteSet sprite;

		public Provider(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		@NotNull
		public Particle createParticle(@NotNull Options options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new DyeingMagicCircleParticle(getSpriteLis().get(options.index()), level, x, y, z, options.xRot(), options.yRot(), options.color(), options.radius(), options.particleLifeTime());
		}

		protected List<TextureAtlasSprite> getSpriteLis() {
			return ((ParticleEngine.MutableSpriteSet) sprite).sprites;
		}
	}
}
