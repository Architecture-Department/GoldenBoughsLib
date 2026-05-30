package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.core.LibConstants.modRl
import architecture.goldenboughs_lib.init.LibParticleTypes
import architecture.goldenboughs_lib.module.lc_damage.particle.LcDamageIconParticle
import net.minecraft.core.particles.ParticleType
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider
import java.util.function.Supplier

class LibDatagenParticle(output: PackOutput, fileHelper: ExistingFileHelper) :
	ParticleDescriptionProvider(output, fileHelper) {
	override fun addDescriptions() {
		sprite(
			LibParticleTypes.LC_DAMAGE_ICON,
			*LcDamageIconParticle.Type.entries.map { it.texturePl }.toTypedArray()
		)
	}

	private fun <T : ParticleType<*>> sprite(type: Supplier<T>, name: String) {
		sprite(type.get(), modRl(name))
	}

	private fun <T : ParticleType<*>> sprite(type: Supplier<T>, vararg names: String) {
		spriteSet(type.get(), names.map { modRl(it) })
	}
}
