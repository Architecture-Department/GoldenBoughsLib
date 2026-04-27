package architecture.goldenboughs_lib.datagen.i18n;

import architecture.goldenboughs_lib.config.LibConfigUtil;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.datagen.LibDatagenSoundDefinitionsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public abstract class DatagenI18n extends LanguageProvider {
	public DatagenI18n(PackOutput output, String locale) {
		super(output, GoldenBoughsLib.ID, locale);
	}

	public static @NotNull String getFormattedKey(String... key) {
		StringBuilder builder = new StringBuilder(GoldenBoughsLib.ID);
		builder.append(".commands");
		for (String s : key) {
			builder.append(".").append(s);
		}
		return builder.toString();
	}

	@Override
	protected abstract void addTranslations();

	protected void addPackDescription(String a, String description) {
		add("pack." + a + "description", description);
	}

	protected void addItemList(Map<Supplier<? extends Item>, String> map) {
		map.forEach((holder, zhName) -> add(holder.get(), zhName));
	}

	protected void addEntityList(Map<Supplier<? extends EntityType<?>>, String> map) {
		map.forEach((holder, zhName) -> add(holder.get(), zhName));
	}

	protected void addMobEffectList(Map<Supplier<? extends MobEffect>, String> map) {
		map.forEach((holder, zhName) -> add(holder.get(), zhName));
	}

	protected void addAttributeList(Map<Supplier<? extends Attribute>, String> map) {
		map.forEach((holder, zhName) -> add(holder.get(), zhName));
	}

	/**
	 * 生物属性翻译
	 */
	protected void add(Attribute attributeHolder, String name) {
		add(attributeHolder.getDescriptionId(), name);
	}

	protected void addSoundEventList(Map<Supplier<? extends SoundEvent>, String> map) {
		map.forEach((holder, zhName) -> add(holder.get(), zhName));
	}

	public void add(SoundEvent damageType, String name) {
		add(LibDatagenSoundDefinitionsProvider.getSubtitle(damageType), name);
	}

	protected void addJadePlugin(ResourceLocation pluginId, String name) {
		add("config.jade.plugin_" + pluginId.toLanguageKey(), name);
	}

	protected void addCurios(String curiosIdName, String name, String modifiersName) {
		add("curios.identifier." + curiosIdName, name);
		add("curios.modifiers." + curiosIdName, modifiersName);
	}

	protected void add(ModConfigSpec.ConfigValue<?> configValue, String value, String tooltipValue) {
		add(configValue, value);
		add(LibConfigUtil.getTranslation(configValue.getPath().toArray(String[]::new)) + ".tooltip", value);
	}

	protected void add(ModConfigSpec.ConfigValue<?> configValue, String value) {
		add(LibConfigUtil.getTranslation(configValue.getPath().toArray(String[]::new)), value);
	}

	protected <T> void add(DataComponentType<T> dataComponentType, String name) {
		add(dataComponentType.toString(), name);
	}

	/**
	 * 死亡消息翻译
	 */
	protected void addDeathMessage(ResourceKey<DamageType> damageType, String name) {
		add("death.attack." + damageType.location().getPath(), name);
	}

	/**
	 * 声音字幕翻译
	 */
	protected void addSoundEvent(Holder<SoundEvent> damageType, String name) {
		add(damageType.value(), name);
	}

	/**
	 * 玩家死亡消息翻译
	 */
	protected void addPlayerDeathMessage(ResourceKey<DamageType> damageType, String name) {
		add("death.attack." + damageType.location().getPath() + ".player", name);
	}
}
