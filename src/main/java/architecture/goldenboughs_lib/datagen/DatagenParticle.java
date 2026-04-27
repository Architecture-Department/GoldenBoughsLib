package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibParticleTypes;
import architecture.imaginarycraft.client.particle.LcDamageIconParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class DatagenParticle extends ParticleDescriptionProvider {
	public DatagenParticle(PackOutput output, ExistingFileHelper fileHelper) {
		super(output, fileHelper);
	}

	@Override
	protected void addDescriptions() {
		sprite(LibParticleTypes.LC_DAMAGE_ICON, Arrays.stream(LcDamageIconParticle.Type.values())
			.map(LcDamageIconParticle.Type::getTexturePl)
			.toArray(String[]::new));
		sprite(LibParticleTypes.DYEING_MAGIC_CIRCLE, "magic_circle/magic_circle16x", "magic_circle/magic_circle32x", "magic_circle/magic_circle128x");
		sprite(LibParticleTypes.MAGIC_BULLET_MAGIC_CIRCLE, "magic_bullet/magic_circle16x", "magic_bullet/magic_circle32x", "magic_bullet/magic_circle128x");
		sprite(LibParticleTypes.SOLEMN_LAMENT_BUTTERFLY_BLACK, "solemn_lament/butterfly_black");
		sprite(LibParticleTypes.SOLEMN_LAMENT_BUTTERFLY_WHITE, "solemn_lament/butterfly_white");
	}

	private <T extends ParticleType<?>> void sprite(Supplier<T> type, String name) {
		sprite(type.get(), ResourceLocation.fromNamespaceAndPath(GoldenBoughsLib.ID, name));
	}

	private <T extends ParticleType<?>> void sprite(Supplier<T> type, String... names) {
		spriteSet(type.get(), Arrays.stream(names)
			.map(DatagenParticle::getPath)
			.collect(Collectors.toList()));
	}

	private static @NotNull ResourceLocation getPath(String name) {
		return ResourceLocation.fromNamespaceAndPath(GoldenBoughsLib.ID, name);
	}
}
