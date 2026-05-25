package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.client.particle.LcDamageIconParticle
import architecture.goldenboughs_lib.client.particle.text.DamageTextParticle
import architecture.goldenboughs_lib.client.particle.text.TextParticleOptions
import architecture.goldenboughs_lib.common.particle.SpecialParticleType
import architecture.goldenboughs_lib.core.LibConstants.modRegister
import com.mojang.serialization.MapCodec
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

/**
 * 粒子类型
 */
object LibParticleTypes {
	@JvmField
	val REGISTRY: DeferredRegister<ParticleType<*>> = modRegister(BuiltInRegistries.PARTICLE_TYPE)

	@JvmField
	val TEXT: Supplier<ParticleType<TextParticleOptions>> = register(
		"text", true, TextParticleOptions.CODEC, TextParticleOptions.STREAM_CODEC
	)

	@JvmField
	val DAMAGE_TEXT: Supplier<ParticleType<DamageTextParticle.Options>> = register(
		"damage_text", true, DamageTextParticle.Options.CODEC, DamageTextParticle.Options.STREAM_CODEC
	)

	@JvmField
	val LC_DAMAGE_ICON: Supplier<ParticleType<LcDamageIconParticle.Options>> = register(
		"lobotomycorporation_damage_icon",
		true,
		LcDamageIconParticle.Options.CODEC,
		LcDamageIconParticle.Options.STREAM_CODEC
	)

	private fun <T : ParticleOptions> register(
		id: String, overrideLimiter: Boolean, mapCodec: MapCodec<T>, streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
	): DeferredHolder<ParticleType<*>, ParticleType<T>> {
		return register(id) { SpecialParticleType(overrideLimiter, mapCodec, streamCodec) }
	}

	private fun <O : ParticleType<*>> register(
		id: String, particleType: Supplier<O>
	): DeferredHolder<ParticleType<*>, O> {
		return REGISTRY.register(id, particleType)
	}
}
