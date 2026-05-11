package architecture.goldenboughs_lib.config

import net.neoforged.neoforge.common.ModConfigSpec

class LibClientConfig internal constructor(builder: ModConfigSpec.Builder) : LibConfigUtil() {
	/**
	 * 新玩家生命条
	 */
	@JvmField
	val enableNewHealthBar: ModConfigSpec.BooleanValue =
		builder.define(true, "enable_new_health_bar", "新玩家生命条")

	/**
	 * 玩家低理智滤镜
	 */
	@JvmField
	val enableLowRationalityFilter: ModConfigSpec.BooleanValue =
		builder.define(true, "enable_low_rationality_filter", "玩家低理智滤镜")

	/**
	 * 玩家遭受脑叶伤害滤镜
	 */
	@JvmField
	val enableLcColorDamageFilter: ModConfigSpec.BooleanValue =
		builder.define(true, "enable_lobotomy_corporation_damage_filter", "玩家遭受脑叶伤害滤镜")
}
