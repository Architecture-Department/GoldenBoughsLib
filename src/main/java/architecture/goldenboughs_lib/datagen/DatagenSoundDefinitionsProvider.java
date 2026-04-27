package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.imaginarycraft.init.MobSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.jetbrains.annotations.NotNull;

public class DatagenSoundDefinitionsProvider extends SoundDefinitionsProvider {
	public DatagenSoundDefinitionsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, GoldenBoughsLib.ID, existingFileHelper);
	}

	public static @NotNull String getSubtitle(SoundEvent soundEvent) {
		return getSubtitle(soundEvent.getLocation());
	}

	public static @NotNull String getSubtitle(ResourceLocation location) {
		return "sound." + location.toLanguageKey();
	}

	@Override
	public void registerSounds() {
		add(MobSoundEvents.SOLEMN_LAMENT_WEAPON_ATTACK_BLACK.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.SOLEMN_LAMENT_WEAPON_ATTACK_WHITE.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.SOLEMN_LAMENT_WEAPON_STONGATTACK_BLACK.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.SOLEMN_LAMENT_WEAPON_STONGATTACK_WHITE.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.VIOLET_NOON_DOWN.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.VIOLET_NOON_ATK.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.VIOLET_NOON_idle.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.VIOLET_NOON_DEATH.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.VIOLET_DAWN_WALK.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.VIOLET_DAWN_SUICIDE.value(), 0.5f, 0.5f, 1, 8);
		add(MobSoundEvents.VIOLET_DAWN_DEAD.value(), 0.5f, 0.5f, 1, 8);
	}

	private void add(SoundEvent soundEvent, float volume, float pitch, int weight, int attenuationDistance) {
		add(soundEvent, 1, volume, pitch, weight, attenuationDistance);
	}

	private void add(SoundEvent soundEvent, int amount, float volume, float pitch, int weight, int attenuationDistance) {
		ResourceLocation location = soundEvent.getLocation();
		assert amount > 0 : "sund : " + location + " amount must be greater than 0";
		SoundDefinition definition = SoundDefinition.definition();
		for (int i = 0; i < amount; i++) {
			definition.with(SoundDefinition.Sound.sound(location.withSuffix(i > 0 ? String.valueOf(i) : ""), SoundDefinition.SoundType.SOUND)
				.volume(volume)
				.pitch(pitch)
				.weight(weight)
				.attenuationDistance(attenuationDistance)
			);
		}
		add(soundEvent, definition
			.subtitle(getSubtitle(location))
			.replace(true)
		);
	}
}
