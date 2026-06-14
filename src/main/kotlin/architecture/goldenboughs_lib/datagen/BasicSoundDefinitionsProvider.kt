package architecture.goldenboughs_lib.datagen

import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.SoundDefinition
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider

abstract class BasicSoundDefinitionsProvider(
	output: PackOutput,
	modId: String,
	existingFileHelper: ExistingFileHelper
) : SoundDefinitionsProvider(output, modId, existingFileHelper) {

	protected fun add(
		soundEvent: SoundEvent,
		volume: Float = 0.5f,
		pitch: Float = 0.5f,
		weight: Int = 1,
		attenuationDistance: Int = 8
	) {
		add(soundEvent, 1, volume, pitch, weight, attenuationDistance)
	}

	protected fun add(
		soundEvent: SoundEvent,
		amount: Int = 1,
		volume: Float = 0.5f,
		pitch: Float = 0.5f,
		weight: Int = 1,
		attenuationDistance: Int = 8
	) {
		val location = soundEvent.location
		assert(amount > 0) { "sund : $location amount must be greater than 0" }
		val definition = SoundDefinition.definition()
		for (i in 0 until amount) {
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
			soundEvent,
			definition
				.subtitle(getSubtitle(location))
				.replace(true)
		)
	}

	companion object {
		@JvmStatic
		fun getSubtitle(soundEvent: SoundEvent): String {
			return getSubtitle(soundEvent.location)
		}

		@JvmStatic
		fun getSubtitle(location: ResourceLocation): String {
			return "sound." + location.toLanguageKey()
		}
	}
}