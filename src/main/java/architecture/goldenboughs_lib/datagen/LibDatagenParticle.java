package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.client.particle.LcDamageIconParticle;
import architecture.goldenboughs_lib.core.Lib;
import architecture.goldenboughs_lib.init.LibParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class LibDatagenParticle extends ParticleDescriptionProvider {
	public LibDatagenParticle(PackOutput output, ExistingFileHelper fileHelper) {
		super(output, fileHelper);
	}

	@Override
	protected void addDescriptions() {
		sprite(LibParticleTypes.LC_DAMAGE_ICON, Arrays.stream(LcDamageIconParticle.Type.values())
			.map(LcDamageIconParticle.Type::getTexturePl)
			.toArray(String[]::new));
	}

	private <T extends ParticleType<?>> void sprite(Supplier<T> type, String name) {
		sprite(type.get(), Lib.modRl(name));
	}

	private <T extends ParticleType<?>> void sprite(Supplier<T> type, String... names) {
		spriteSet(type.get(), Arrays.stream(names)
			.map(Lib::modRl)
			.collect(Collectors.toList()));
	}
}
