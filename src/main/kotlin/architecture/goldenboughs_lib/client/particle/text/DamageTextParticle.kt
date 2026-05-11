package architecture.goldenboughs_lib.client.particle.text

import architecture.goldenboughs_lib.init.LibParticleTypes
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Mth
import kotlin.math.min
import kotlin.math.sqrt

class DamageTextParticle(
	level: ClientLevel,
	x: Double,
	y: Double,
	z: Double,
	options: Options,
	@JvmField protected final val isHeal: Boolean
) : TextParticle(level, x, y, z, options.options) {
	private val isCrit = false
	private val numberLifetime = 30
	private var phase = 0
	private var phaseTimer = 0
	private var startX: Float
	private var startY: Float
	private var startZ: Float
	private var targetX = 0f
	private var targetY = 0f
	private var targetZ = 0f
	private var currentSizeScale = 0.5f
	private var targetSizeScale = 0.5f

	init {
		this.startX = x.toFloat()
		this.startY = y.toFloat()
		this.startZ = z.toFloat()
		calculateRandomTarget()
		this.x = this.startX.toDouble()
		this.y = this.startY.toDouble()
		this.z = this.startZ.toDouble()
	}

	private fun calculateRandomTarget() {
		val verticalDistance = this.random.nextFloat()
		val horizontalRadius = 0.5f
		val randomAngle = this.random.nextFloat() * 360.0f
		val angleRad = randomAngle * Mth.DEG_TO_RAD
		val randomRadius = this.random.nextFloat() * horizontalRadius
		val horizontalX = Mth.cos(angleRad) * randomRadius
		val horizontalZ = Mth.sin(angleRad) * randomRadius
		this.targetX = this.startX + horizontalX
		this.targetZ = this.startZ + horizontalZ
		this.targetY = this.startY + verticalDistance
		if (this.isCrit) {
			this.targetY += 0.5f
			this.targetX += (this.random.nextFloat() - 0.5f) * 0.2f
			this.targetZ += (this.random.nextFloat() - 0.5f) * 0.2f
		}
	}

	override fun render(vertexConsumer: VertexConsumer, camera: Camera, partialTicks: Float) {
		val cameraPosition = camera.position
		val dx = getX(partialTicks) - cameraPosition.x
		val dy = getY(partialTicks) - cameraPosition.y
		val dz = getZ(partialTicks) - cameraPosition.z
		val distance = sqrt(dx * dx + dy * dy + dz * dz)

		val distanceScale = 0.1f + distance.toFloat() * 0.5f
		targetSizeScale = 0.5f * this.baseSize * distanceScale

		val minScale = 0.1f
		if (targetSizeScale < this.baseSize * minScale) {
			targetSizeScale = this.baseSize * minScale
		}

		val maxScale = 5.5f
		if (targetSizeScale > this.baseSize * maxScale) {
			targetSizeScale = this.baseSize * maxScale
		}

		when (phase) {
			0 -> {
				var smoothProgress = min(1.0f, phaseTimer / 10.0f)
				smoothProgress = smoothStep(smoothProgress)
				currentSizeScale = Mth.lerp(smoothProgress, 0.0f, targetSizeScale)
			}

			1 -> {
				currentSizeScale = Mth.lerp(0.1f, currentSizeScale, targetSizeScale)
			}

			2 -> {
				currentSizeScale = Mth.lerp(0.15f, currentSizeScale, targetSizeScale)
			}
		}
		this.size = currentSizeScale

		super.render(vertexConsumer, camera, partialTicks)
	}

	private fun smoothStep(t: Float): Float {
		return t * t * (3.0f - 2.0f * t)
	}

	override fun tick() {
		super.tick()
	}

	override fun tickAge() {
		if (this.age < this.lifetime) {
			phaseTimer++
			when (phase) {
				0 -> handleRisePhase()
				1 -> handleHoverPhase()
				2 -> handleFallPhase()
			}
		}
		super.tickAge()
	}

	private fun handleRisePhase() {
		val riseTime = if (this.isCrit) numberLifetime * 2 / 3 + 5 else numberLifetime / 3 + 5
		var progress = min(1.0f, phaseTimer / riseTime.toFloat())
		progress = smoothStep(progress)
		this.x = Mth.lerp(progress, this.startX, this.targetX).toDouble()
		this.y = Mth.lerp(progress, this.startY, this.targetY).toDouble()
		this.z = Mth.lerp(progress, this.startZ, this.targetZ).toDouble()
		if (progress >= 1.0f) {
			phase = 1
			phaseTimer = 0
			this.startX = this.x.toFloat()
			this.startY = this.y.toFloat()
			this.startZ = this.z.toFloat()
		}
	}

	private fun handleHoverPhase() {
		val hoverTime = if (this.isCrit) numberLifetime * 2 else numberLifetime / 3

		if (phaseTimer >= hoverTime) {
			phase = 2
			phaseTimer = 0
		} else {
			val floatOffset = Mth.sin(this.age * 0.2f) * 0.05f
			this.y = (this.targetY + floatOffset).toDouble()
			val swingOffset = Mth.sin(this.age * 0.15f) * 0.02f
			this.x = (this.targetX + swingOffset).toDouble()
			this.z = (this.targetZ + swingOffset).toDouble()
		}
	}

	private fun handleFallPhase() {
		val fallTime = if (this.isCrit) numberLifetime * 2 else numberLifetime / 3
		var fallProgress = min(1.0f, phaseTimer / fallTime.toFloat())
		fallProgress = easeOutCubic(fallProgress)
		val fallDistance = 0.5f + (if (this.isCrit) 0.3f else 0f)
		this.y = (this.targetY - fallProgress * fallDistance).toDouble()
		this.alpha = 1.0f - fallProgress
		if (phaseTimer < fallTime) {
			val drift = Mth.sin(this.age * 0.1f) * 0.01f
			this.x += drift.toDouble()
			this.z += drift.toDouble()
		}
	}

	private fun easeOutCubic(t: Float): Float {
		val f = t - 1.0f
		return f * f * f + 1.0f
	}

	@JvmRecord
	data class Options(@JvmField val options: TextParticleOptions, @JvmField val isHeal: Boolean) : ParticleOptions {
		override fun getType(): ParticleType<*> {
			return LibParticleTypes.DAMAGE_TEXT.get()
		}

		companion object {
			@JvmField
			val CODEC: MapCodec<Options> =
				RecordCodecBuilder.mapCodec { instance: RecordCodecBuilder.Instance<Options> ->
					instance.group(
						TextParticleOptions.CODEC.fieldOf("options").forGetter(Options::options),
						Codec.BOOL.fieldOf("isHeal").forGetter(Options::isHeal)
					).apply(instance, ::Options)
				}

			@JvmField
			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Options> =
				StreamCodec.composite(
					TextParticleOptions.STREAM_CODEC, Options::options,
					ByteBufCodecs.BOOL, Options::isHeal,
					::Options
				)
		}
	}

	class Provider : ParticleProvider<Options> {
		override fun createParticle(
			options: Options,
			level: ClientLevel,
			x: Double,
			y: Double,
			z: Double,
			xSpeed: Double,
			ySpeed: Double,
			zSpeed: Double
		): Particle {
			return DamageTextParticle(level, x, y, z, options, options.isHeal)
		}
	}
}
