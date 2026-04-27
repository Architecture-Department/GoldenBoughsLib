package architecture.goldenboughs_lib.client.particle.text;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import org.jetbrains.annotations.NotNull;

public class TextParticleProvider implements ParticleProvider<TextParticleOptions> {
	@Override
	@NotNull
	public Particle createParticle(@NotNull TextParticleOptions type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		return type.buildParticle(level, x, y, z);
	}
}
