package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.datagen.i18n.LibZhCn
import architecture.goldenboughs_lib.module.lc_damage.attribute.BasicAttribute
import architecture.goldenboughs_lib.module.lc_damage.attribute.MaxAttribute
import architecture.goldenboughs_lib.module.lc_damage.attribute.MinAttribute
import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.neoforge.common.BooleanAttribute
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

object LibAttributes {
	@JvmField
	val REGISTRY: DeferredRegister<Attribute> = LibUtil.modRegister(BuiltInRegistries.ATTRIBUTE)

	const val PHYSICS_VULNERABLE_DEFAULT_VALUE: Double = 1.0
	const val SPIRIT_VULNERABLE_DEFAULT_VALUE: Double = 1.0
	const val EROSION_VULNERABLE_DEFAULT_VALUE: Double = 1.5
	const val THE_SOUL_VULNERABLE_DEFAULT_VALUE: Double = 2.0

	// TODO 采用史诗战斗的相关系统
	/**
	 * 攻击速度（主手）
	 * 
	 * 
	 * 用物品攻击时处理冷却速度。该数值代表每秒可施展的满强度攻击次数
	 */
	@JvmField
	val ATTACK_SPEED_MAIN_HAND: DeferredHolder<Attribute, RangedAttribute> = register(
		"attack_speed_main_hand", "主手攻击速度", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, PHYSICS_VULNERABLE_DEFAULT_VALUE, -1024.0, 1024.0
	)

	/**
	 * 攻击速度（副手）
	 * 
	 * 
	 * 用物品攻击时处理冷却速度。该数值代表每秒可施展的满强度攻击次数
	 */
	@JvmField
	val ATTACK_SPEED_OFF_HAND: DeferredHolder<Attribute, RangedAttribute> = register(
		"attack_speed_off_hand", "副手攻击速度", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, PHYSICS_VULNERABLE_DEFAULT_VALUE, -1024.0, 1024.0
	)

	/**
	 * 物理易伤
	 */
	@JvmField
	val PHYSICS_VULNERABLE: DeferredHolder<Attribute, RangedAttribute> = register(
		"physics_vulnerable", "物理易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)
		}, PHYSICS_VULNERABLE_DEFAULT_VALUE, -1024.0, 1024.0
	)

	/**
	 * 精神易伤
	 */
	@JvmField
	val SPIRIT_VULNERABLE: DeferredHolder<Attribute, RangedAttribute> = register(
		"spirit_vulnerable", "精神易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)
		}, SPIRIT_VULNERABLE_DEFAULT_VALUE, -1024.0, 1024.0
	)

	/**
	 * 侵蚀易伤
	 */
	@JvmField
	val EROSION_VULNERABLE: DeferredHolder<Attribute, RangedAttribute> = register(
		"erosion_vulnerable", "侵蚀易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)
		}, EROSION_VULNERABLE_DEFAULT_VALUE, -1024.0, 1024.0
	)

	/**
	 * 灵魂易伤
	 */
	@JvmField
	val THE_SOUL_VULNERABLE: DeferredHolder<Attribute, RangedAttribute> = register(
		"the_soul_vulnerable", "灵魂易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)
		}, THE_SOUL_VULNERABLE_DEFAULT_VALUE, -1024.0, 1024.0
	)

	/**
	 * 物理防御
	 */
	@JvmField
	val PHYSICS_DEFENSE: DeferredHolder<Attribute, RangedAttribute> = register(
		"physics_defense", "物理易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0, -1024.0, 1024.0
	)

	/**
	 * 精神防御
	 */
	@JvmField
	val SPIRIT_DEFENSE: DeferredHolder<Attribute, RangedAttribute> = register(
		"spirit_defense", "精神易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0, -1024.0, 1024.0
	)

	/**
	 * 侵蚀防御
	 */
	@JvmField
	val EROSION_DEFENSE: DeferredHolder<Attribute, RangedAttribute> = register(
		"erosion_defense", "侵蚀易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0, -1024.0, 1024.0
	)

	/**
	 * 灵魂防御
	 */
	@JvmField
	val THE_SOUL_DEFENSE: DeferredHolder<Attribute, RangedAttribute> = register(
		"the_soul_defense", "灵魂易伤", {
			it.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0, -1024.0, 1024.0
	)

	/** 理智 */
	/**
	 * 最大理智值
	 */
	@JvmField
	val MAX_RATIONALITY: DeferredHolder<Attribute, MinAttribute> = registerMin(
		"max_rationality",
		"最大理智值",
		{ function: MinAttribute -> function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE) },
		20.0,
		0.0
	)

	/**
	 * 理智值自然恢复等待时间
	 */
	@JvmField
	val RATIONALITY_NATURAL_RECOVERY_WAIT_TIME: DeferredHolder<Attribute, MinAttribute> = registerMin(
		"rationality_natural_recovery_wait_time",
		"理智值自然恢复等待时间",
		{ function: MinAttribute -> function.setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE) },
		0.5,
		0.0
	)

	/**
	 * 理智值自然恢复量
	 */
	@JvmField
	val RATIONALITY_RECOVERY_AMOUNT: DeferredHolder<Attribute, MinAttribute> = registerMin(
		"rationality_recovery_amount",
		"理智值自然恢复量",
		{ function: MinAttribute -> function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE) },
		1.0,
		0.0
	)

	/** 四徳 */
	/**
	 * 勇气
	 */
	@JvmField
	val FORTITUDE_POINTS: DeferredHolder<Attribute, BasicAttribute> = register(
		"fortitude_points", "勇气点数", { function: BasicAttribute ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0
	)

	/**
	 * 谨慎
	 */
	@JvmField
	val PRUDENCE_POINTS: DeferredHolder<Attribute, BasicAttribute> = register(
		"prudence_points", "谨慎点数", { function: BasicAttribute ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0
	)

	/**
	 * 自律
	 */
	@JvmField
	val TEMPERANCE_POINTS: DeferredHolder<Attribute, BasicAttribute> = register(
		"temperance_points", "自律点数", { function: BasicAttribute ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0
	)

	/**
	 * 正义
	 */
	@JvmField
	val JUSTICE_POINTS: DeferredHolder<Attribute, BasicAttribute> = register(
		"justice_points", "正义点数", { function: BasicAttribute ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, 0.0
	)

	// 部门属性
	// TODO 完善情报部的功能
	/**
	 * 情报部门激活
	 */
	@JvmField
	val INTELLIGENCE_DEPARTMENT_ACTIVATION: DeferredHolder<Attribute, BooleanAttribute> = register(
		"player.intelligence_department_activation", "情报部效果", { function: BooleanAttribute ->
			function.setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
		}, false
	)

	private fun register(
		name: String, zhName: String, function: Function<RangedAttribute, Attribute>, minValue: Double, maxValue: Double
	): DeferredHolder<Attribute, RangedAttribute> {
		return register(name, zhName, function, minValue, minValue, maxValue)
	}

	private fun register(
		name: String,
		zhName: String,
		function: Function<RangedAttribute, Attribute>,
		defaultValue: Double,
		minValue: Double,
		maxValue: Double
	): DeferredHolder<Attribute, RangedAttribute> {
		return register(
			name, zhName, function, RangedAttribute(descriptionId(name), defaultValue, minValue, maxValue)
		)
	}

	private fun <T : Attribute> register(
		name: String, zhName: String, function: Function<T, Attribute>, attribute: T
	): DeferredHolder<Attribute, T> {
		val register = REGISTRY.register(name, Supplier { function.apply(attribute) as T })
		LibZhCn.addI18nAttributeText(zhName, register)
		return register
	}

	private fun descriptionId(name: String): String {
		return LibUtil.ID + ".attribute.name." + name
	}

	private fun register(
		name: String, zhName: String, function: Function<BasicAttribute, Attribute>, value: Double
	): DeferredHolder<Attribute, BasicAttribute> {
		return register(name, zhName, function, BasicAttribute(descriptionId(name), value))
	}

	private fun registerMin(
		name: String, zhName: String, function: Function<MinAttribute, Attribute>, defaultValue: Double, minValue: Double
	): DeferredHolder<Attribute, MinAttribute> {
		return register(
			name, zhName, function, MinAttribute(descriptionId(name), defaultValue, minValue)
		)
	}

	private fun registerMax(
		name: String, zhName: String, function: Function<MaxAttribute, Attribute>, defaultValue: Double, maxValue: Double
	): DeferredHolder<Attribute, MaxAttribute> {
		return register(
			name, zhName, function, MaxAttribute(descriptionId(name), defaultValue, maxValue)
		)
	}

	private fun register(
		name: String, zhName: String, function: Function<BooleanAttribute, Attribute>, defaultValue: Boolean
	): DeferredHolder<Attribute, BooleanAttribute> {
		return register(name, zhName, function, BooleanAttribute(descriptionId(name), defaultValue))
	}
}
