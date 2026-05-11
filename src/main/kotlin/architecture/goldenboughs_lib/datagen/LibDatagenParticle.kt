package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.client.particle.LcDamageIconParticle
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.core.Lib.modRl
import architecture.goldenboughs_lib.init.LibParticleTypes
import net.minecraft.core.particles.ParticleType
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors

class LibDatagenParticle(output: PackOutput, fileHelper: ExistingFileHelper) :
	ParticleDescriptionProvider(output, fileHelper) {
	override fun addDescriptions() {
		sprite(
			LibParticleTypes.LC_DAMAGE_ICON,
			*Arrays.stream(LcDamageIconParticle.Type.values())
				.map(LcDamageIconParticle.Type::texturePl)
				.toArray() as Array<String>
		)
	}

	private fun <T : ParticleType<*>> sprite(type: Supplier<T>, name: String) {
		sprite(type.get(), modRl(name))
	}

	private fun <T : ParticleType<*>> sprite(type: Supplier<T>, vararg names: String) {
		spriteSet(
			type.get(), Arrays.stream(names)
				.map(Lib::modRl)
				.collect(Collectors.toList())
		)
	}
}
