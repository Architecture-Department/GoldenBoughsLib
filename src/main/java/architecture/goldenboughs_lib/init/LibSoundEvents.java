package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.datagen.i18n.ZhCn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class LibSoundEvents {
	public static final DeferredRegister<ArmorMaterial> REGISTRY = GoldenBoughsLib.modRegister(BuiltInRegistries.ARMOR_MATERIAL);
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_ZAYIN = registerForHolder(
		"armor_equip_zayin", "", "item.armor.equip_zayin");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_TETH = registerForHolder(
		"armor_equip_teth", "", "item.armor.equip_teth");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_HE = registerForHolder(
		"armor_equip_he", "", "item.armor.equip_he");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_WAW = registerForHolder(
		"armor_equip_waw", "", "item.armor.equip_waw");
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_ALEPH = registerForHolder(
		"armor_equip_aleph", "", "item.armor.equip_aleph");


	private static DeferredHolder<SoundEvent, SoundEvent> registerForHolder(String id, String zhName, String location) {
		DeferredHolder<SoundEvent, SoundEvent> register = REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(GoldenBoughsLib.modRl(location)));
		ZhCn.addI18nSoundEventText(zhName, register);
		return register;
	}
}
