package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.common.attribute.BasicAttribute;
import architecture.goldenboughs_lib.common.attribute.MaxAttribute;
import architecture.goldenboughs_lib.common.attribute.MinAttribute;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


public final class LibAttributes {
	public static final DeferredRegister<Attribute> REGISTRY = GoldenBoughsLib.modRegister(BuiltInRegistries.ATTRIBUTE);

	public static final double PHYSICS_VULNERABLE_DEFAULT_VALUE = 1.0;
	public static final double SPIRIT_VULNERABLE_DEFAULT_VALUE = 1.0;
	public static final double EROSION_VULNERABLE_DEFAULT_VALUE = 1.5;
	public static final double THE_SOUL_VULNERABLE_DEFAULT_VALUE = 2.0;

	// TODO 采用史诗战斗的相关系统
	/**
	 * 攻击速度（主手）
	 * <p>
	 * 用物品攻击时处理冷却速度。该数值代表每秒可施展的满强度攻击次数
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> ATTACK_SPEED_MAIN_HAND = register(
		"attack_speed_main_hand", "主手攻击速度", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), PHYSICS_VULNERABLE_DEFAULT_VALUE, -1024, 1024);
	/**
	 * 攻击速度（副手）
	 * <p>
	 * 用物品攻击时处理冷却速度。该数值代表每秒可施展的满强度攻击次数
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> ATTACK_SPEED_OFF_HAND = register(
		"attack_speed_off_hand", "副手攻击速度", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), PHYSICS_VULNERABLE_DEFAULT_VALUE, -1024, 1024);

	/**
	 * 物理易伤
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> PHYSICS_VULNERABLE = register(
		"physics_vulnerable", "物理易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE), PHYSICS_VULNERABLE_DEFAULT_VALUE, -1024, 1024);
	/**
	 * 精神易伤
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> SPIRIT_VULNERABLE = register(
		"spirit_vulnerable", "精神易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE), SPIRIT_VULNERABLE_DEFAULT_VALUE, -1024, 1024);
	/**
	 * 侵蚀易伤
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> EROSION_VULNERABLE = register(
		"erosion_vulnerable", "侵蚀易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE), EROSION_VULNERABLE_DEFAULT_VALUE, -1024, 1024);
	/**
	 * 灵魂易伤
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> THE_SOUL_VULNERABLE = register(
		"the_soul_vulnerable", "灵魂易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE), THE_SOUL_VULNERABLE_DEFAULT_VALUE, -1024, 1024);
	/**
	 * 物理防御
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> PHYSICS_DEFENSE = register(
		"physics_defense", "物理易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0.0, -1024, 1024);
	/**
	 * 精神防御
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> SPIRIT_DEFENSE = register(
		"spirit_defense", "精神易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0.0, -1024, 1024);
	/**
	 * 侵蚀防御
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> EROSION_DEFENSE = register(
		"erosion_defense", "侵蚀易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0.0, -1024, 1024);
	/**
	 * 灵魂防御
	 */
	public static final DeferredHolder<Attribute, RangedAttribute> THE_SOUL_DEFENSE = register(
		"the_soul_defense", "灵魂易伤", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0.0, -1024, 1024);

	/// 理智
	/**
	 * 最大理智值
	 */
	public static final DeferredHolder<Attribute, MinAttribute> MAX_RATIONALITY = registerMin(
		"max_rationality", "最大理智值", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 20, 0);
	/**
	 * 理智值自然恢复等待时间
	 */
	public static final DeferredHolder<Attribute, MinAttribute> RATIONALITY_NATURAL_RECOVERY_WAIT_TIME = registerMin(
		"rationality_natural_recovery_wait_time", "理智值自然恢复等待时间", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE), 0.5, 0);
	/**
	 * 理智值自然恢复量
	 */
	public static final DeferredHolder<Attribute, MinAttribute> RATIONALITY_RECOVERY_AMOUNT = registerMin(
		"rationality_recovery_amount", "理智值自然恢复量", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 1, 0);

	/// 四徳
	/**
	 * 勇气
	 */
	public static final DeferredHolder<Attribute, BasicAttribute> FORTITUDE_POINTS = register(
		"fortitude_points", "勇气点数", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0);
	/**
	 * 谨慎
	 */
	public static final DeferredHolder<Attribute, BasicAttribute> PRUDENCE_POINTS = register(
		"prudence_points", "谨慎点数", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0);
	/**
	 * 自律
	 */
	public static final DeferredHolder<Attribute, BasicAttribute> TEMPERANCE_POINTS = register(
		"temperance_points", "自律点数", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0);
	/**
	 * 正义
	 */
	public static final DeferredHolder<Attribute, BasicAttribute> JUSTICE_POINTS = register(
		"justice_points", "正义点数", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), 0);

	// 部门属性
	// TODO 完善情报部的功能
	/**
	 * 情报部门激活
	 */
	public static final DeferredHolder<Attribute, BooleanAttribute> INTELLIGENCE_DEPARTMENT_ACTIVATION = register(
		"player.intelligence_department_activation", "情报部效果", function ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE), false);

	private static @NotNull DeferredHolder<Attribute, RangedAttribute> register(
		final String name,
		final String zhName,
		final Function<RangedAttribute, Attribute> function,
		final double minValue,
		final double maxValue
	) {
		return register(name, zhName, function, minValue, minValue, maxValue);
	}

	private static @NotNull DeferredHolder<Attribute, RangedAttribute> register(
		final String name,
		final String zhName,
		final Function<RangedAttribute, Attribute> function,
		final double defaultValue,
		final double minValue,
		final double maxValue
	) {
		return register(name, zhName, function, new RangedAttribute(descriptionId(name), defaultValue, minValue, maxValue));
	}

	private static @NotNull <T extends Attribute> DeferredHolder<Attribute, T> register(
		final String name,
		final String zhName,
		final Function<T, Attribute> function,
		final T attribute
	) {
		DeferredHolder<Attribute, T> register = LibAttributes.REGISTRY.register(name, () -> (T) function.apply(attribute));
		LibZhCn.addI18nAttributeText(zhName, register);
		return register;
	}

	private static @NotNull String descriptionId(String name) {
		return GoldenBoughsLib.ID + ".attribute.name." + name;
	}

	private static @NotNull DeferredHolder<Attribute, BasicAttribute> register(
		final String name,
		final String zhName,
		final Function<BasicAttribute, Attribute> function,
		final double value
	) {
		return register(name, zhName, function, new BasicAttribute(descriptionId(name), value));
	}

	private static @NotNull DeferredHolder<Attribute, MinAttribute> registerMin(
		final String name,
		final String zhName,
		final Function<MinAttribute, Attribute> function,
		final double defaultValue,
		final double minValue
	) {
		return register(name, zhName, function, new MinAttribute(descriptionId(name), defaultValue, minValue));
	}

	private static @NotNull DeferredHolder<Attribute, MaxAttribute> registerMax(
		final String name,
		final String zhName,
		final Function<MaxAttribute, Attribute> function,
		final double defaultValue,
		final double maxValue
	) {
		return register(name, zhName, function, new MaxAttribute(descriptionId(name), defaultValue, maxValue));
	}

	private static @NotNull DeferredHolder<Attribute, BooleanAttribute> register(
		final String name,
		final String zhName,
		final Function<BooleanAttribute, Attribute> function,
		final boolean defaultValue
	) {
		return register(name, zhName, function, new BooleanAttribute(descriptionId(name), defaultValue));
	}
}
