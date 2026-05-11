package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.client.particle.LcDamageIconParticle;
import architecture.goldenboughs_lib.client.particle.text.DamageTextParticle;
import architecture.goldenboughs_lib.client.particle.text.TextParticleOptions;
import architecture.goldenboughs_lib.core.Lib;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * 粒子类型
 */
public final class LibParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = Lib.modRegister(BuiltInRegistries.PARTICLE_TYPE);

	public static final Supplier<ParticleType<TextParticleOptions>> TEXT = register(
		"text", true, TextParticleOptions.CODEC, TextParticleOptions.STREAM_CODEC);
	public static final Supplier<ParticleType<DamageTextParticle.Options>> DAMAGE_TEXT = register(
		"damage_text", true, DamageTextParticle.Options.CODEC, DamageTextParticle.Options.STREAM_CODEC);

	public static final Supplier<ParticleType<LcDamageIconParticle.Options>> LC_DAMAGE_ICON = register(
		"lobotomycorporation_damage_icon", true, LcDamageIconParticle.Options.CODEC, LcDamageIconParticle.Options.STREAM_CODEC);

	private static <T extends ParticleOptions> @NotNull DeferredHolder<ParticleType<?>, ParticleType<T>> register(
		String id,
		boolean overrideLimiter,
		MapCodec<T> mapCodec,
		StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec
	) {
		return register(
			id, () -> new SpecialParticleType<>(overrideLimiter, mapCodec, streamCodec));
	}

	private static <O extends ParticleType<?>> DeferredHolder<ParticleType<?>, O> register(String id, Supplier<O> particleType) {
		return LibParticleTypes.REGISTRY.register(id, particleType);
	}

	private static DeferredHolder<ParticleType<?>, SimpleParticleType> registerSimpleParticle(String id, boolean overrideLimiter) {
		return register(id, () -> new SimpleParticleType(overrideLimiter));
	}

	private static class SpecialParticleType<T extends ParticleOptions> extends ParticleType<T> {
		private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
		private final MapCodec<T> codec;

		private SpecialParticleType(
			final boolean overrideLimitter,
			MapCodec<T> codec,
			StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec
		) {
			super(overrideLimitter);
			this.codec = codec;
			this.streamCodec = streamCodec;
		}

		@Override
		public MapCodec<T> codec() {
			return codec;
		}

		@Override
		public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
			return streamCodec;
		}
	}
}
