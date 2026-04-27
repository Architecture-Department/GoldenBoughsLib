package architecture.goldenboughs_lib.client.particle.text;

import architecture.goldenboughs_lib.init.LibParticleTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DamageTextParticle extends TextParticle {
	protected final boolean isHeal;
	private final boolean isCrit;
	private final int numberLifetime = 30;
	private int phase = 0;
	private int phaseTimer = 0;
	private float startX, startY, startZ;
	private float targetX, targetY, targetZ;
	private float currentSizeScale = 0.5f;
	private float targetSizeScale = 0.5f;

	protected DamageTextParticle(ClientLevel level, double x, double y, double z, Options options, boolean isHeal) {
		super(level, x, y, z, options.options);
		this.isHeal = isHeal;
		this.isCrit = false;
		this.startX = (float) x;
		this.startY = (float) y;
		this.startZ = (float) z;
		calculateRandomTarget();
		this.x = this.startX;
		this.y = this.startY;
		this.z = this.startZ;
	}

	private void calculateRandomTarget() {
		float verticalDistance = this.random.nextFloat();
		float horizontalRadius = 0.5f;
		float randomAngle = this.random.nextFloat() * 360.0f;
		float angleRad = randomAngle * Mth.DEG_TO_RAD;
		float randomRadius = this.random.nextFloat() * horizontalRadius;
		float horizontalX = Mth.cos(angleRad) * randomRadius;
		float horizontalZ = Mth.sin(angleRad) * randomRadius;
		this.targetX = this.startX + horizontalX;
		this.targetZ = this.startZ + horizontalZ;
		this.targetY = this.startY + verticalDistance;
		if (this.isCrit) {
			this.targetY += 0.5f;
			this.targetX += (this.random.nextFloat() - 0.5f) * 0.2f;
			this.targetZ += (this.random.nextFloat() - 0.5f) * 0.2f;
		}
	}

	@Override
	public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
		Vec3 cameraPosition = camera.getPosition();
		double dx = getX(partialTicks) - cameraPosition.x;
		double dy = getY(partialTicks) - cameraPosition.y;
		double dz = getZ(partialTicks) - cameraPosition.z;
		double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

		float distanceScale = 0.1f + (float) distance * 0.5f;
		targetSizeScale = 0.5f * this.baseSize * distanceScale;

		float minScale = 0.1f;
		if (targetSizeScale < this.baseSize * minScale) {
			targetSizeScale = this.baseSize * minScale;
		}

		float maxScale = 5.5f;
		if (targetSizeScale > this.baseSize * maxScale) {
			targetSizeScale = this.baseSize * maxScale;
		}

		switch (phase) {
			case 0 -> {
				float smoothProgress = Math.min(1.0f, phaseTimer / 10.0f);
				smoothProgress = smoothStep(smoothProgress);
				currentSizeScale = Mth.lerp(smoothProgress, 0.0f, targetSizeScale);
			}
			case 1 -> {
				currentSizeScale = Mth.lerp(0.1f, currentSizeScale, targetSizeScale);
			}
			case 2 -> {
				currentSizeScale = Mth.lerp(0.15f, currentSizeScale, targetSizeScale);
			}
		}
		this.size = currentSizeScale;

		super.render(vertexConsumer, camera, partialTicks);
	}

	private float smoothStep(float t) {
		return t * t * (3.0f - 2.0f * t);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void tickAge() {
		if (this.age < this.lifetime) {
			phaseTimer++;
			switch (phase) {
				case 0 -> handleRisePhase();
				case 1 -> handleHoverPhase();
				case 2 -> handleFallPhase();
			}
		}
		super.tickAge();
	}

	private void handleRisePhase() {
		int riseTime = this.isCrit ? numberLifetime * 2 / 3 + 5 : numberLifetime / 3 + 5;
		float progress = Math.min(1.0f, phaseTimer / (float) riseTime);
		progress = smoothStep(progress);
		this.x = Mth.lerp(progress, this.startX, this.targetX);
		this.y = Mth.lerp(progress, this.startY, this.targetY);
		this.z = Mth.lerp(progress, this.startZ, this.targetZ);
		if (progress >= 1.0f) {
			phase = 1;
			phaseTimer = 0;
			this.startX = (float) this.x;
			this.startY = (float) this.y;
			this.startZ = (float) this.z;
		}
	}

	private void handleHoverPhase() {
		int hoverTime = this.isCrit ? numberLifetime * 2 : numberLifetime / 3;

		if (phaseTimer >= hoverTime) {
			phase = 2;
			phaseTimer = 0;
		} else {
			float floatOffset = Mth.sin(this.age * 0.2f) * 0.05f;
			this.y = this.targetY + floatOffset;
			float swingOffset = Mth.sin(this.age * 0.15f) * 0.02f;
			this.x = this.targetX + swingOffset;
			this.z = this.targetZ + swingOffset;
		}
	}

	private void handleFallPhase() {
		int fallTime = this.isCrit ? numberLifetime * 2 : numberLifetime / 3;
		float fallProgress = Math.min(1.0f, phaseTimer / (float) fallTime);
		fallProgress = easeOutCubic(fallProgress);
		float fallDistance = 0.5f + (this.isCrit ? 0.3f : 0f);
		this.y = this.targetY - fallProgress * fallDistance;
		this.alpha = 1.0f - fallProgress;
		if (phaseTimer < fallTime) {
			float drift = Mth.sin(this.age * 0.1f) * 0.01f;
			this.x += drift;
			this.z += drift;
		}
	}

	private float easeOutCubic(float t) {
		float f = t - 1.0f;
		return f * f * f + 1.0f;
	}

	public record Options(TextParticleOptions options, boolean isHeal) implements ParticleOptions {
		public static final MapCodec<Options> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			TextParticleOptions.CODEC.fieldOf("options").forGetter(Options::options),
			Codec.BOOL.fieldOf("isHeal").forGetter(Options::isHeal)
		).apply(instance, Options::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
			TextParticleOptions.STREAM_CODEC, Options::options,
			ByteBufCodecs.BOOL, Options::isHeal,
			Options::new);

		@Override
		public @NotNull ParticleType<?> getType() {
			return LibParticleTypes.DAMAGE_TEXT.get();
		}
	}

	public static class Provider implements ParticleProvider<Options> {
		@Override
		@NotNull
		public Particle createParticle(@NotNull DamageTextParticle.Options options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new DamageTextParticle(level, x, y, z, options, options.isHeal);
		}
	}
}
