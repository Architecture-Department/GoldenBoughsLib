package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.core.Lib
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.SoundDefinition
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider

class LibDatagenSoundDefinitionsProvider(
	output: PackOutput,
	existingFileHelper: ExistingFileHelper
) : SoundDefinitionsProvider(output, Lib.ID, existingFileHelper) {
	override fun registerSounds() {
	}

	private fun add(soundEvent: SoundEvent, volume: Float, pitch: Float, weight: Int, attenuationDistance: Int) {
		add(soundEvent, 1, volume, pitch, weight, attenuationDistance)
	}

	private fun add(
		soundEvent: SoundEvent,
		amount: Int,
		volume: Float,
		pitch: Float,
		weight: Int,
		attenuationDistance: Int
	) {
		val location = soundEvent.location
		assert(amount > 0) { "sund : $location amount must be greater than 0" }
		val definition = SoundDefinition.definition()
		for (i in 0..<amount) {
			definition.with(
				SoundDefinition.Sound.sound(
					location.withSuffix(if (i > 0) i.toString() else ""),
					SoundDefinition.SoundType.SOUND
				)
					.volume(volume)
					.pitch(pitch)
					.weight(weight)
					.attenuationDistance(attenuationDistance)
			)
		}
		add(
			soundEvent, definition
				.subtitle(getSubtitle(location))
				.replace(true)
		)
	}

	companion object {
		fun getSubtitle(soundEvent: SoundEvent): String {
			return getSubtitle(soundEvent.location)
		}

		fun getSubtitle(location: ResourceLocation): String {
			return "sound." + location.toLanguageKey()
		}
	}
}
