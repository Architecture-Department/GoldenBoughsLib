package architecture.goldenboughs_lib.config

import net.neoforged.neoforge.common.ModConfigSpec
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue

class LibServerConfig internal constructor(builder: ModConfigSpec.Builder) : LibConfigUtil() {
	//理智/精神伤害配置
	/**
	 * 自然恢复理智值
	 */
	@JvmField
	val enableNaturalRationalityRationality: BooleanValue =
		builder.define(true, "enable_natural_rationality_rationality", "自然恢复理智值")

	//护盾/四色护盾共存
	@JvmField
	val enableMultiShield: BooleanValue =
		builder.define(false, "enable_multi_shield", "四色护盾共存")

	//护盾/护盾破盾免伤
	@JvmField
	val enableShieldDamageImmunity: BooleanValue =
		builder.define(false, "enable_shield_damage_immunity", "护盾破盾免伤")

	//护盾/护盾每级增加的护盾值
	@JvmField
	val shieldAdditionalValuePerLevel: ModConfigSpec.DoubleValue =
		builder.define(10.0, 0.0, 1000.0, "shield_additional_value_per_level", "护盾每级增加的护盾值")
}
