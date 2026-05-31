package architecture.goldenboughs_lib.datagen.i18n

import architecture.goldenboughs_lib.core.LibConfig
import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibDamageTypes
import architecture.goldenboughs_lib.init.tag.LibItemTags
import architecture.goldenboughs_lib.linkage.jade.LivingEntityVulnerable
import architecture.goldenboughs_lib.linkage.jade.ModJadePlugin
import architecture.goldenboughs_lib.module.rationality.command.RationalityDataCommands
import architecture.goldenboughs_lib.module.virtue.api.VirtueType
import architecture.goldenboughs_lib.module.virtue.data_component.ItemVirtueUsageReq
import net.minecraft.data.PackOutput
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.item.Item
import net.neoforged.fml.loading.FMLEnvironment
import org.jetbrains.annotations.ApiStatus
import java.util.function.Supplier

@ApiStatus.Internal
class LibZhCn(output: PackOutput) : DatagenI18n(output, LibConstants.ID, "zh_cn") {
	override fun addTranslations() {
		addPackDescription(LibConstants.ID, "金枝")
		addMobEffectList(MOB_EFFECT)
		addAttributeList(ATTRIBUTE)
		addSoundEventList(SOUND_EVENT)
		addItemList(ITEMS)
		addEntityList(ENTITY_TYPES)
		MAP.forEach { (key: String, value: String) -> this.add(key, value) }

		addJadePlugin(ModJadePlugin.ENTITY_LC_LEVEL, "生物等级")
		addJadePlugin(ModJadePlugin.BLOCK_LC_LEVEL, "方块等级")
		addJadePlugin(ModJadePlugin.ENTITY_LC_VULNERABLE, "生物易伤")
		add(LivingEntityVulnerable.ATTRIBUTE_DESCRIPTION_KEY, "易伤")
		add(LivingEntityVulnerable.PHYSICS_KEY, "物理易伤")
		add(LivingEntityVulnerable.SPIRIT_KEY, "精神易伤")
		add(LivingEntityVulnerable.EROSION_KEY, "侵蚀易伤")
		add(LivingEntityVulnerable.THE_SOUL_KEY, "灵魂易伤")

		//region 命令
		add(
			getFormattedKey(
				RationalityDataCommands.SET_KEY,
				RationalityDataCommands.ProcessType.VALUE.typeName
			), "已设置%s的理智值为：%d"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.SET_KEY,
				RationalityDataCommands.ProcessType.MAX_VALUE.typeName
			), "已设置%s的最大理智基础值为：%d"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.SET_KEY,
				RationalityDataCommands.ProcessType.NATURAL_RECOVERY_RATE.typeName
			), "已设置%s的基础理智值理智值自然恢复等待时间为：20*%d Tick"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.SET_KEY,
				RationalityDataCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.typeName
			), "已设置%s的基础理智恢复为：每次%d"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.GET_KEY,
				RationalityDataCommands.ProcessType.VALUE.typeName
			), "%s的理智值为：%d"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.GET_KEY,
				RationalityDataCommands.ProcessType.MAX_VALUE.typeName
			), "%s的最大理智值为：%d"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.GET_KEY,
				RationalityDataCommands.ProcessType.NATURAL_RECOVERY_RATE.typeName
			), "%s的理智值理智值自然恢复等待时间为：20*%d Tick"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.GET_KEY,
				RationalityDataCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.typeName
			), "%s的理智值自然恢复量为：每次%d点"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.RESET_KEY,
				RationalityDataCommands.ProcessType.VALUE.typeName
			), "已重置%s的理智值为：%d"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.RESET_KEY,
				RationalityDataCommands.ProcessType.MAX_VALUE.typeName
			), "已重置%s的最大理智基础值为：%d"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.RESET_KEY,
				RationalityDataCommands.ProcessType.NATURAL_RECOVERY_RATE.typeName
			), "已重置%s的基础理智值理智值自然恢复等待时间为：20*%dTick"
		)
		add(
			getFormattedKey(
				RationalityDataCommands.RESET_KEY,
				RationalityDataCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.typeName
			), "已重置%s的基础理智值自然恢复量为：每次%d点"
		)
		add(getFormattedKey(RationalityDataCommands.RESET_KEY), "已重置%s的理智")

		//endregion

		//region 配置
		add(LibConfig.CLIENT.enableNewHealthBar, "玩家的新生命条")
		add(LibConfig.CLIENT.enableLcColorDamageFilter, "玩家遭受四色伤害滤镜")
		add(LibConfig.CLIENT.enableLowRationalityFilter, "玩家低理智滤镜")
		add(LibConfig.SERVER.enableNaturalRationalityRationality, "自然恢复理智值")
		add(LibConfig.SERVER.enableMultiShield, "四色护盾共存")
		add(LibConfig.SERVER.enableShieldDamageImmunity, "护盾破盾免伤")
		add(LibConfig.SERVER.shieldAdditionalValuePerLevel, "护盾每级增加的护盾值")

		//endregion

		//region 伤害类型
		addPlayerDeathMessage(LibDamageTypes.PHYSICS, "%s死于%s的造成的物理伤害")
		addDeathMessage(LibDamageTypes.PHYSICS, "%s被剁成肉沫了")
		addPlayerDeathMessage(LibDamageTypes.SPIRIT, "%s死于%s的造成的精神污染")
		addDeathMessage(LibDamageTypes.SPIRIT, "%s因精神崩溃而死")
		addPlayerDeathMessage(LibDamageTypes.EROSION, "%s死于%s的造成的侵蚀伤害")
		addDeathMessage(LibDamageTypes.EROSION, "%s因腐蚀而亡")
		addPlayerDeathMessage(LibDamageTypes.THE_SOUL, "%s死于%s的造成的灵魂伤害")
		addDeathMessage(LibDamageTypes.THE_SOUL, "%s的灵魂被超度了")
		addPlayerDeathMessage(LibDamageTypes.EGO, "%s死于%s的E.G.O")
		addDeathMessage(LibDamageTypes.EGO, "%s死于E.G.O")
		addPlayerDeathMessage(LibDamageTypes.MELEE, "%s死于%s的造成的近战伤害")
		addDeathMessage(LibDamageTypes.MELEE, "%s死于近战伤害")
		addPlayerDeathMessage(LibDamageTypes.REMOTE, "%s死于%s的造成的远程伤害")
		addDeathMessage(LibDamageTypes.REMOTE, "%s死于远程伤害")

		//endregion

		//region tag标签
		add(LibItemTags.EGO, "E.G.O.")

		//endregion

		//region tooltip
		add(ItemVirtueUsageReq.USE_CONDITION, "使用条件")
		add(ItemVirtueUsageReq.REQUIREMENT, "只能为：")
		add(ItemVirtueUsageReq.INTERVAL, "至少：%s，至多：%s")
		add(ItemVirtueUsageReq.NOT_TO_EXCEED, "至多：%s")
		add(ItemVirtueUsageReq.NOT_LOWER_THAN, "至少：%s")
		add(VirtueType.FORTITUDE.tooltipName, "勇气")
		add(VirtueType.PRUDENCE.tooltipName, "谨慎")
		add(VirtueType.TEMPERANCE.tooltipName, "自律")
		add(VirtueType.JUSTICE.tooltipName, "正义")
		add(VirtueType.COMPOSITE.tooltipName, "综合")
		//endregion
	}

	companion object {
		@JvmStatic
		private val ITEMS: MutableMap<Supplier<out Item>, String> = HashMap()

		@JvmStatic
		private val MOB_EFFECT: MutableMap<Supplier<out MobEffect>, String> = HashMap()

		@JvmStatic
		private val ATTRIBUTE: MutableMap<Supplier<out Attribute>, String> = HashMap()

		@JvmStatic
		private val SOUND_EVENT: MutableMap<Supplier<out SoundEvent>, String> = HashMap()

		@JvmStatic
		private val ENTITY_TYPES: MutableMap<Supplier<out EntityType<*>>, String> = HashMap()

		@JvmStatic
		private val MAP: MutableMap<String, String> = HashMap()

		@JvmStatic
		fun addI18nText(zhCn: String, key: String) {
			if (!FMLEnvironment.production) {
				MAP[key] = zhCn
			}
		}

		@JvmStatic
		fun addI18nItemText(zhName: String, deferredItem: Supplier<out Item>) {
			if (!FMLEnvironment.production) {
				ITEMS[deferredItem] = zhName
			}
		}

		@JvmStatic
		fun addI18nMobEffectText(zhName: String, supplier: Supplier<out MobEffect>) {
			if (!FMLEnvironment.production) {
				MOB_EFFECT[supplier] = zhName
			}
		}

		@JvmStatic
		fun addI18nAttributeText(zhName: String, supplier: Supplier<out Attribute>) {
			if (!FMLEnvironment.production) {
				ATTRIBUTE[supplier] = zhName
			}
		}

		@JvmStatic
		fun addI18nSoundEventText(zhName: String, supplier: Supplier<out SoundEvent>) {
			if (!FMLEnvironment.production) {
				SOUND_EVENT[supplier] = zhName
			}
		}

		@JvmStatic
		fun addI18nEntityTypeText(zhName: String, supplier: Supplier<out EntityType<*>>) {
			if (!FMLEnvironment.production) {
				ENTITY_TYPES[supplier] = zhName
			}
		}
	}
}
