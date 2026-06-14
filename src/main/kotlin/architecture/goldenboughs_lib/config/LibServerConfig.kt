package architecture.goldenboughs_lib.config

import architecture.goldenboughs_lib.api.BasicConfig
import architecture.goldenboughs_lib.util.LibUtil
import net.neoforged.neoforge.common.ModConfigSpec.*

class LibServerConfig internal constructor(builder: Builder) : BasicConfig(LibUtil.ID, builder) {
	//理智

	/**
	 * 自然恢复理智值
	 */
	@JvmField
	val enableNaturalRationalityRationality: BooleanValue =
		define(true, "enable_natural_rationality_rationality", "自然恢复理智值")

	// 护盾

	/** 四色护盾共存*/
	@JvmField
	val enableMultiShield: BooleanValue =
		define(false, "enable_multi_shield", "四色护盾共存")

	/**护盾破盾免伤*/
	@JvmField
	val enableShieldDamageImmunity: BooleanValue =
		define(false, "enable_shield_damage_immunity", "护盾破盾免伤")

	/** 护盾每级增加的护盾值*/
	@JvmField
	val shieldAdditionalValuePerLevel: DoubleValue =
		define(10.0, 0.0, 1000.0, "shield_additional_value_per_level", "护盾每级增加的护盾值")
}
