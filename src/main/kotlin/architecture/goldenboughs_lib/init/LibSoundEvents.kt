package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.core.Lib.modRegister
import architecture.goldenboughs_lib.core.Lib.modRl
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object LibSoundEvents {

	@JvmField
	val REGISTRY: DeferredRegister<SoundEvent> = modRegister<SoundEvent>(BuiltInRegistries.SOUND_EVENT)

	private fun registerForHolder(
		id: String, zhName: String, location: String
	): DeferredHolder<SoundEvent, SoundEvent> {
		val register = REGISTRY.register(id) { -> SoundEvent.createVariableRangeEvent(modRl(location)) }
		LibZhCn.addI18nSoundEventText(zhName, register)
		return register
	}
}
