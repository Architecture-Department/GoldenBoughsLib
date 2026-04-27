package architecture.goldenboughs_lib.datagen.i18n;

import architecture.goldenboughs_lib.api.virtue.VirtueType;
import architecture.goldenboughs_lib.common.components.ItemVirtueUsageReq;
import architecture.goldenboughs_lib.datagen.DatagenCuriosTest;
import architecture.goldenboughs_lib.init.LibDamageTypes;
import architecture.goldenboughs_lib.init.tag.LibItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ZhCn extends DatagenI18n {
	private static final Map<Supplier<? extends MobEffect>, String> MOB_EFFECT = new HashMap<>();
	private static final Map<Supplier<? extends Attribute>, String> ATTRIBUTE = new HashMap<>();
	private static final Map<Supplier<? extends SoundEvent>, String> SOUND_EVENT = new HashMap<>();
	private static final Map<String, String> MAP = new HashMap<>();

	public ZhCn(final PackOutput output) {
		super(output, "zh_cn");
	}

	public static void addI18nText(String zhCn, String key) {
		if (!FMLEnvironment.production) {
			ZhCn.MAP.put(key, zhCn);
		}
	}

	public static void addI18nMobEffectText(String zhName, Supplier<? extends MobEffect> supplier) {
		if (!FMLEnvironment.production) {
			ZhCn.MOB_EFFECT.put(supplier, zhName);
		}
	}

	public static void addI18nAttributeText(String zhName, Supplier<? extends Attribute> supplier) {
		if (!FMLEnvironment.production) {
			ZhCn.ATTRIBUTE.put(supplier, zhName);
		}
	}

	public static void addI18nSoundEventText(String zhName, Supplier<? extends SoundEvent> supplier) {
		if (!FMLEnvironment.production) {
			ZhCn.SOUND_EVENT.put(supplier, zhName);
		}
	}

	@Override
	protected void addTranslations() {
		add("pack.imaginarycraft.description", "异想工艺");
		addMobEffectList(MOB_EFFECT);
		addAttributeList(ATTRIBUTE);
		addSoundEventList(SOUND_EVENT);
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
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.VALUE.getName()), "已设置%s的理智值为：%d");
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.MAX_VALUE.getName()), "已设置%s的最大理智基础值为：%d");
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.NATURAL_RECOVERY_RATE.getName()), "已设置%s的基础理智值理智值自然恢复等待时间为：20*%d Tick");
		add(getFormattedKey(RationalityCommands.SET_KEY, RationalityCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.getName()), "已设置%s的基础理智恢复为：每次%d");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.VALUE.getName()), "%s的理智值为：%d");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.MAX_VALUE.getName()), "%s的最大理智值为：%d");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.NATURAL_RECOVERY_RATE.getName()), "%s的理智值理智值自然恢复等待时间为：20*%d Tick");
		add(getFormattedKey(RationalityCommands.GET_KEY, RationalityCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.getName()), "%s的理智值自然恢复量为：每次%d点");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.VALUE.getName()), "已重置%s的理智值为：%d");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.MAX_VALUE.getName()), "已重置%s的最大理智基础值为：%d");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.NATURAL_RECOVERY_RATE.getName()), "已重置%s的基础理智值理智值自然恢复等待时间为：20*%dTick");
		add(getFormattedKey(RationalityCommands.RESET_KEY, RationalityCommands.ProcessType.RATIONALITY_RECOVERY_AMOUNT.getName()), "已重置%s的基础理智值自然恢复量为：每次%d点");
		add(getFormattedKey(RationalityCommands.RESET_KEY), "已重置%s的理智");
		//endregion

		//region 配置
		add(architecture.goldenboughs_lib.config.LibConfig.CLIENT.enableNewHealthBar, "是否开启新玩家生命条");
		add(architecture.goldenboughs_lib.config.LibConfig.CLIENT.enableLcColorDamageFilter, "是否开启玩家遭受四色伤害滤镜");
		add(architecture.goldenboughs_lib.config.LibConfig.CLIENT.enableLowRationalityFilter, "是否开启玩家低理智滤镜");
		add(architecture.goldenboughs_lib.config.LibConfig.SERVER.enableNaturalRationalityRationality, "是否开启自然恢复理智值");
		//endregion

		//region 饰品
		addCurios(DatagenCuriosTest.EGO_CURIOS_HEADWEAR, "头饰", "E.G.O饰品-头饰");
		addCurios(DatagenCuriosTest.EGO_CURIOS_HEAD, "头部", "E.G.O饰品-头部");
		addCurios(DatagenCuriosTest.EGO_CURIOS_HINDBRAIN, "后脑", "E.G.O饰品-后脑");
		addCurios(DatagenCuriosTest.EGO_CURIOS_EYE, "眼部", "E.G.O饰品-眼部");
		addCurios(DatagenCuriosTest.EGO_CURIOS_FACE, "面部", "E.G.O饰品-面部");
		addCurios(DatagenCuriosTest.EGO_CURIOS_CHEEK, "脸颊", "E.G.O饰品-脸颊");
		addCurios(DatagenCuriosTest.EGO_CURIOS_MASK, "口罩", "E.G.O饰品-口罩");
		addCurios(DatagenCuriosTest.EGO_CURIOS_MOUTH, "口部", "E.G.O饰品-口部");
		addCurios(DatagenCuriosTest.EGO_CURIOS_NECK, "颈部", "E.G.O饰品-颈部");
		addCurios(DatagenCuriosTest.EGO_CURIOS_BROOCH, "胸针", "E.G.O饰品-胸针");
		addCurios(DatagenCuriosTest.EGO_CURIOS_HAND, "手部", "E.G.O饰品-手部");
		addCurios(DatagenCuriosTest.EGO_CURIOS_GLOVE, "手套", "E.G.O饰品-手套");
		addCurios(DatagenCuriosTest.EGO_CURIOS_LEFT_BACK, "左背", "E.G.O饰品-左背");
		addCurios(DatagenCuriosTest.EGO_CURIOS_RIGHT_BACK, "右背", "E.G.O饰品-右背");
		EgoCurioItems.REGISTRY.getEntries().stream()
			.map(DeferredHolder::get)
			.filter(EgoCurioItem.class::isInstance)
			.map(EgoCurioItem.class::cast)
			.map(EgoCurioItem::getAndClearTooltipsI18nMap)
			.forEach(map -> map.forEach(this::add));
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
		add(LibItemTags.EGO, "E.G.O");
		add(LibItemTags.EGO_ARMOUR, "E.G.O盔甲");
		add(LibItemTags.EGO_WEAPON, "E.G.O武器");
		add(LibItemTags.EGO_TOOL, "E.G.O工具");
		add(LibItemTags.EGO_CURIOS, "E.G.O饰品");
		add(LibItemTags.EGO_CURIOS_HEADWEAR, "E.G.O饰品-头饰");
		add(LibItemTags.EGO_CURIOS_HEAD, "E.G.O饰品-头部");
		add(LibItemTags.EGO_CURIOS_HINDBRAIN, "E.G.O饰品-后脑");
		add(LibItemTags.EGO_CURIOS_EYE, "E.G.O饰品-眼部");
		add(LibItemTags.EGO_CURIOS_FACE, "E.G.O饰品-面部");
		add(LibItemTags.EGO_CURIOS_CHEEK, "E.G.O饰品-脸颊");
		add(LibItemTags.EGO_CURIOS_MASK, "E.G.O饰品-口罩");
		add(LibItemTags.EGO_CURIOS_MOUTH, "E.G.O饰品-口部");
		add(LibItemTags.EGO_CURIOS_NECK, "E.G.O饰品-颈部");
		add(LibItemTags.EGO_CURIOS_BROOCH, "E.G.O饰品-胸针");
		add(LibItemTags.EGO_CURIOS_HAND, "E.G.O饰品-手部");
		add(LibItemTags.EGO_CURIOS_GLOVE, "E.G.O饰品-手套");
		add(LibItemTags.EGO_CURIOS_BACK, "E.G.O饰品-背后");
		//endregion

		//region tooltip
		add(ItemVirtueUsageReq.USE_CONDITION, "使用条件");
		add(ItemVirtueUsageReq.REQUIREMENT, "只能为：");
		add(ItemVirtueUsageReq.INTERVAL, "至少：%s，至多：%s");
		add(ItemVirtueUsageReq.NOT_TO_EXCEED, "至多：%s");
		add(ItemVirtueUsageReq.NOT_LOWER_THAN, "至少：%s");
		add(VirtueType.FORTITUDE.getTooltipName(), "勇气");
		add(VirtueType.PRUDENCE.getTooltipName(), "谨慎");
		add(VirtueType.TEMPERANCE.getTooltipName(), "自律");
		add(VirtueType.JUSTICE.getTooltipName(), "正义");
		add(VirtueType.COMPOSITE.getTooltipName(), "综合");
		//endregion
	}
}
