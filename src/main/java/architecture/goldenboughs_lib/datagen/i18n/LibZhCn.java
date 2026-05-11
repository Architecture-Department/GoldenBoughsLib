package architecture.goldenboughs_lib.datagen.i18n;

import architecture.goldenboughs_lib.api.virtue.VirtueType;
import architecture.goldenboughs_lib.common.command.RationalityCommands;
import architecture.goldenboughs_lib.common.components.ItemVirtueUsageReq;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibDamageTypes;
import architecture.goldenboughs_lib.init.tag.LibItemTags;
import architecture.goldenboughs_lib.linkage.jade.LivingEntityVulnerable;
import architecture.goldenboughs_lib.linkage.jade.ModJadePlugin;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class LibZhCn extends DatagenI18n {
	private static final Map<Supplier<? extends Item>, String> ITEMS = new HashMap<>();
	private static final Map<Supplier<? extends MobEffect>, String> MOB_EFFECT = new HashMap<>();
	private static final Map<Supplier<? extends Attribute>, String> ATTRIBUTE = new HashMap<>();
	private static final Map<Supplier<? extends SoundEvent>, String> SOUND_EVENT = new HashMap<>();
	private static final Map<Supplier<? extends EntityType<?>>, String> ENTITY_TYPES = new HashMap<>();
	private static final Map<String, String> MAP = new HashMap<>();

	public LibZhCn(final PackOutput output) {
		super(output, GoldenBoughsLib.ID, "zh_cn");
	}

	public static void addI18nText(String zhCn, String key) {
		if (!FMLEnvironment.production) {
			MAP.put(key, zhCn);
		}
	}

	public static void addI18nItemText(String zhName, Supplier<? extends Item> deferredItem) {
		if (!FMLEnvironment.production) {
			ITEMS.put(deferredItem, zhName);
		}
	}

	public static void addI18nMobEffectText(String zhName, Supplier<? extends MobEffect> supplier) {
		if (!FMLEnvironment.production) {
			MOB_EFFECT.put(supplier, zhName);
		}
	}

	public static void addI18nAttributeText(String zhName, Supplier<? extends Attribute> supplier) {
		if (!FMLEnvironment.production) {
			ATTRIBUTE.put(supplier, zhName);
		}
	}

	public static void addI18nSoundEventText(String zhName, Supplier<? extends SoundEvent> supplier) {
		if (!FMLEnvironment.production) {
			SOUND_EVENT.put(supplier, zhName);
		}
	}

	public static void addI18nEntityTypeText(String zhName, Supplier<? extends EntityType<?>> supplier) {
		if (!FMLEnvironment.production) {
			ENTITY_TYPES.put(supplier, zhName);
		}
	}

	@Override
	protected void addTranslations() {
		addPackDescription(GoldenBoughsLib.ID, "金枝");
		addMobEffectList(MOB_EFFECT);
		addAttributeList(ATTRIBUTE);
		addSoundEventList(SOUND_EVENT);
		addItemList(ITEMS);
		addEntityList(ENTITY_TYPES);
		MAP.forEach(this::add);

		addJadePlugin(ModJadePlugin.ENTITY_LC_LEVEL, "生物等级");
		addJadePlugin(ModJadePlugin.BLOCK_LC_LEVEL, "方块等级");
		addJadePlugin(ModJadePlugin.ENTITY_LC_VULNERABLE, "生物易伤");
		add(LivingEntityVulnerable.ATTRIBUTE_DESCRIPTION_KEY, "易伤");
		add(LivingEntityVulnerable.PHYSICS_KEY, "物理易伤");
		add(LivingEntityVulnerable.SPIRIT_KEY, "精神易伤");
		add(LivingEntityVulnerable.EROSION_KEY, "侵蚀易伤");
		add(LivingEntityVulnerable.THE_SOUL_KEY, "灵魂易伤");

		//region 命令
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.VALUE.typeName), "已设置%s的理智值为：%d");
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.MAX_VALUE.typeName), "已设置%s的最大理智基础值为：%d");
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.NATURAL_RECOVERY_RATE.typeName), "已设置%s的基础理智值理智值自然恢复等待时间为：20*%d Tick");
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.typeName), "已设置%s的基础理智恢复为：每次%d");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.VALUE.typeName), "%s的理智值为：%d");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.MAX_VALUE.typeName), "%s的最大理智值为：%d");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.NATURAL_RECOVERY_RATE.typeName), "%s的理智值理智值自然恢复等待时间为：20*%d Tick");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.typeName), "%s的理智值自然恢复量为：每次%d点");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.VALUE.typeName), "已重置%s的理智值为：%d");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.MAX_VALUE.typeName), "已重置%s的最大理智基础值为：%d");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.NATURAL_RECOVERY_RATE.typeName), "已重置%s的基础理智值理智值自然恢复等待时间为：20*%dTick");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.typeName), "已重置%s的基础理智值自然恢复量为：每次%d点");
		add(getFormattedKey(RationalityCommands.RESET_KEY), "已重置%s的理智");
		//endregion

		//region 配置
		add(architecture.goldenboughs_lib.config.LibConfig.CLIENT.enableNewHealthBar, "是否开启新玩家生命条");
		add(architecture.goldenboughs_lib.config.LibConfig.CLIENT.enableLcColorDamageFilter, "是否开启玩家遭受四色伤害滤镜");
		add(architecture.goldenboughs_lib.config.LibConfig.CLIENT.enableLowRationalityFilter, "是否开启玩家低理智滤镜");
		add(architecture.goldenboughs_lib.config.LibConfig.SERVER.enableNaturalRationalityRationality, "是否开启自然恢复理智值");
		//endregion

		//region 伤害类型
		addPlayerDeathMessage(LibDamageTypes.PHYSICS, "%s死于%s的造成的物理伤害");
		addDeathMessage(LibDamageTypes.PHYSICS, "%s被剁成肉沫了");
		addPlayerDeathMessage(LibDamageTypes.SPIRIT, "%s死于%s的造成的精神污染");
		addDeathMessage(LibDamageTypes.SPIRIT, "%s因精神崩溃而死");
		addPlayerDeathMessage(LibDamageTypes.EROSION, "%s死于%s的造成的侵蚀伤害");
		addDeathMessage(LibDamageTypes.EROSION, "%s因腐蚀而亡");
		addPlayerDeathMessage(LibDamageTypes.THE_SOUL, "%s死于%s的造成的灵魂伤害");
		addDeathMessage(LibDamageTypes.THE_SOUL, "%s的灵魂被超度了");
		addPlayerDeathMessage(LibDamageTypes.EGO, "%s死于%s的E.G.O");
		addDeathMessage(LibDamageTypes.EGO, "%s死于E.G.O");
		addPlayerDeathMessage(LibDamageTypes.MELEE, "%s死于%s的造成的近战伤害");
		addDeathMessage(LibDamageTypes.MELEE, "%s死于近战伤害");
		addPlayerDeathMessage(LibDamageTypes.REMOTE, "%s死于%s的造成的远程伤害");
		addDeathMessage(LibDamageTypes.REMOTE, "%s死于远程伤害");
		//endregion

		//region tag标签
		add(LibItemTags.EGO, "E.G.O.");
		//endregion

		//region tooltip
		add(ItemVirtueUsageReq.USE_CONDITION, "使用条件");
		add(ItemVirtueUsageReq.REQUIREMENT, "只能为：");
		add(ItemVirtueUsageReq.INTERVAL, "至少：%s，至多：%s");
		add(ItemVirtueUsageReq.NOT_TO_EXCEED, "至多：%s");
		add(ItemVirtueUsageReq.NOT_LOWER_THAN, "至少：%s");
		add(VirtueType.FORTITUDE.tooltipName, "勇气");
		add(VirtueType.PRUDENCE.tooltipName, "谨慎");
		add(VirtueType.TEMPERANCE.tooltipName, "自律");
		add(VirtueType.JUSTICE.tooltipName, "正义");
		add(VirtueType.COMPOSITE.tooltipName, "综合");
		//endregion
	}
}
