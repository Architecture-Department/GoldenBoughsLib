package architecture.goldenboughs_lib.common.particle

import com.mojang.serialization.MapCodec
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

class SpecialParticleType<T : ParticleOptions>(
	overrideLimiter: Boolean,
	private val codec: MapCodec<T>,
	private val streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
) : ParticleType<T>(overrideLimiter) {
	override fun codec(): MapCodec<T> {
		return codec
	}

	override fun streamCodec(): StreamCodec<in RegistryFriendlyByteBuf, T> {
		return streamCodec
	}
}