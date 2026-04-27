package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class LibSoundEvents {
	public static final DeferredRegister<SoundEvent> REGISTRY = GoldenBoughsLib.modRegister(BuiltInRegistries.SOUND_EVENT);

	private static DeferredHolder<SoundEvent, SoundEvent> registerForHolder(String id, String zhName, String location) {
		DeferredHolder<SoundEvent, SoundEvent> register = REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(GoldenBoughsLib.modRl(location)));
		LibZhCn.addI18nSoundEventText(zhName, register);
		return register;
	}
}
