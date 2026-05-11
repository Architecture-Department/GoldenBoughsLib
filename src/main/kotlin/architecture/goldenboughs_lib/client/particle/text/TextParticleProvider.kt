package architecture.goldenboughs_lib.client.particle.text

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider

class TextParticleProvider : ParticleProvider<TextParticleOptions> {
	override fun createParticle(
		type: TextParticleOptions,
		level: ClientLevel,
		x: Double,
		y: Double,
		z: Double,
		xSpeed: Double,
		ySpeed: Double,
		zSpeed: Double
	): Particle {
		return type.buildParticle(level, x, y, z)
	}
}
