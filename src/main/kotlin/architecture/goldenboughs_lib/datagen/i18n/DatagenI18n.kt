package architecture.goldenboughs_lib.datagen.i18n

import architecture.goldenboughs_lib.datagen.LibDatagenSoundDefinitionsProvider
import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.ModConfigSpec
import net.neoforged.neoforge.common.data.LanguageProvider
import java.util.function.Supplier

abstract class DatagenI18n(
	output: PackOutput,
	val modId: String,
	locale: String
) : LanguageProvider(output, modId, locale) {
	abstract override fun addTranslations()

	protected fun addPackDescription(a: String, description: String) {
		add("pack.$a.description", description)
	}

	protected fun addItemList(map: MutableMap<Supplier<out Item>, String>) {
		map.forEach { (holder: Supplier<out Item>, zhName: String) -> add(holder.get(), zhName) }
	}

	protected fun addEntityList(map: MutableMap<Supplier<out EntityType<*>>, String>) {
		map.forEach { (holder: Supplier<out EntityType<*>>, zhName: String) -> add(holder.get(), zhName) }
	}

	protected fun addMobEffectList(map: MutableMap<Supplier<out MobEffect>, String>) {
		map.forEach { (holder: Supplier<out MobEffect>, zhName: String) -> add(holder.get(), zhName) }
	}

	protected fun addAttributeList(map: MutableMap<Supplier<out Attribute>, String>) {
		map.forEach { (holder: Supplier<out Attribute>, zhName: String) -> add(holder.get(), zhName) }
	}

	/**
	 * 生物属性翻译
	 */
	protected fun add(attributeHolder: Attribute, name: String) {
		add(attributeHolder.descriptionId, name)
	}

	protected fun addSoundEventList(map: MutableMap<Supplier<out SoundEvent>, String>) {
		map.forEach { (holder: Supplier<out SoundEvent>, zhName: String) -> add(holder.get(), zhName) }
	}

	fun add(damageType: SoundEvent, name: String) {
		add(LibDatagenSoundDefinitionsProvider.getSubtitle(damageType), name)
	}

	protected fun addJadePlugin(pluginId: ResourceLocation, name: String) {
		add("config.jade.plugin_" + pluginId.toLanguageKey(), name)
	}

	protected fun addCurios(curiosIdName: String, name: String, modifiersName: String) {
		add("curios.identifier.$curiosIdName", name)
		add("curios.modifiers.$curiosIdName", modifiersName)
	}

	protected fun add(configValue: ModConfigSpec.ConfigValue<*>, value: String, tooltipValue: String) {
		add(configValue, value)
		add(
			getConfigTranslation(modId, *configValue.path.toTypedArray() + ".tooltip", value),
			tooltipValue
		)
	}

	protected fun add(configValue: ModConfigSpec.ConfigValue<*>, value: String) {
		add(getConfigTranslation(modId, *configValue.path.toTypedArray()), value)
	}

	protected fun <T> add(dataComponentType: DataComponentType<T>, name: String) {
		add(dataComponentType.toString(), name)
	}

	/**
	 * 死亡消息翻译
	 */
	protected fun addDeathMessage(damageType: ResourceKey<DamageType>, name: String) {
		add("death.attack." + damageType.location().path, name)
	}

	/**
	 * 声音字幕翻译
	 */
	protected fun addSoundEvent(damageType: Holder<SoundEvent>, name: String) {
		add(damageType.value(), name)
	}

	/**
	 * 玩家死亡消息翻译
	 */
	protected fun addPlayerDeathMessage(damageType: ResourceKey<DamageType>, name: String) {
		add("death.attack." + damageType.location().path + ".player", name)
	}

	companion object {
		@JvmStatic
		fun getFormattedKey(vararg key: String): String {
			val builder = StringBuilder(LibUtil.ID)
			builder.append(".commands")
			for (s in key) {
				builder.append(".").append(s)
			}
			return builder.toString()
		}

		@JvmStatic
		fun getConfigTranslation(modId: String, vararg keys: String): String {
			if (keys.isEmpty()) {
				return "$modId.config"
			}
			val builder = StringBuilder()
			for (key in keys) {
				builder.append(".")
				builder.append(key)
			}
			return "$modId.config$builder"
		}
	}
}
