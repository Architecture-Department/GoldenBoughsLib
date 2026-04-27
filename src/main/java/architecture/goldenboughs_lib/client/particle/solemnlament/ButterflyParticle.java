package architecture.goldenboughs_lib.client.particle.solemnlament;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ButterflyParticle extends TextureSheetParticle {
	private final SpriteSet spriteSet;

	protected ButterflyParticle(SpriteSet spriteSet, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.spriteSet = spriteSet;
		this.setSpriteFromAge(spriteSet);
		this.lifetime = 8 + this.random.nextInt(4);
		this.quadSize = 0.15F + (this.random.nextFloat() * 0.1f);
		this.setSize(quadSize, quadSize);
	}

	@Override
	public float getQuadSize(float scaleFactor) {
		float f = ((float) this.age + scaleFactor) / (float) this.lifetime;
		return this.quadSize * (1.0F - f * f * 0.5F);
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.spriteSet);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public ButterflyParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ButterflyParticle(sprites, level, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}
