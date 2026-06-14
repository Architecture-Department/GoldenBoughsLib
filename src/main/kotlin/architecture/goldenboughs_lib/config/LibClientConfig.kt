package architecture.goldenboughs_lib.config

import architecture.goldenboughs_lib.api.BasicConfig
import architecture.goldenboughs_lib.util.LibUtil
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue
import net.neoforged.neoforge.common.ModConfigSpec.Builder

class LibClientConfig internal constructor(builder: Builder) : BasicConfig(LibUtil.ID, builder) {
	/**
	 * 新玩家生命条
	 */
	@JvmField
	val enableNewHealthBar: BooleanValue =
		define(false, "enable_new_health_bar", "新玩家生命条")

	/**
	 * 玩家低理智滤镜
	 */
	@JvmField
	val enableLowRationalityFilter: BooleanValue =
		define(true, "enable_low_rationality_filter", "玩家低理智滤镜")

	/**
	 * 玩家遭受脑叶伤害滤镜
	 */
	@JvmField
	val enableLcColorDamageFilter: BooleanValue =
		define(true, "enable_lobotomy_corporation_damage_filter", "玩家遭受脑叶伤害滤镜")
}
