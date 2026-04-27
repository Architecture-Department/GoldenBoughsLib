package architecture.goldenboughs_lib.config;

import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public final class LibClientConfig extends LibConfigUtil {
	/**
	 * 新玩家生命条
	 */
	public final BooleanValue enableNewHealthBar;
	/**
	 * 玩家低理智滤镜
	 */
	public final BooleanValue enableLowRationalityFilter;
	/**
	 * 玩家遭受脑叶伤害滤镜
	 */
	public final BooleanValue enableLcColorDamageFilter;

	LibClientConfig(Builder builder) {
		enableNewHealthBar = define(
			builder, false, "enable_new_health_bar", "新玩家生命条");
		enableLowRationalityFilter = define(
			builder, true, "enable_low_rationality_filter", "玩家低理智滤镜");
		enableLcColorDamageFilter = define(
			builder, true, "enable_lobotomy_corporation_damage_filter", "玩家遭受脑叶伤害滤镜");
	}
}
