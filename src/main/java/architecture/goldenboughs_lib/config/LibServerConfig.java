package architecture.goldenboughs_lib.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import org.jetbrains.annotations.NotNull;

public final class LibServerConfig extends ConfigUtil {

	//理智/精神伤害配置
	/**
	 * 自然恢复理智值
	 */
	public final BooleanValue enableNaturalRationalityRationality;
	//护盾/四色护盾共存
	public final BooleanValue enableMultiShield;
	//护盾/护盾破盾免伤
	public final BooleanValue enableShieldDamageImmunity;
	//护盾/护盾每级增加的护盾值
	public final ModConfigSpec.@NotNull DoubleValue shieldAdditionalValuePerLevel;

	LibServerConfig(Builder builder) {
		enableNaturalRationalityRationality = define(
			builder, true, "enable_natural_rationality_rationality", "自然恢复理智值");
		enableMultiShield = define(
			builder, false, "enable_multi_shield", "四色护盾共存");
		enableShieldDamageImmunity = define(
			builder, false, "enable_shield_damage_immunity", "护盾破盾免伤");
		shieldAdditionalValuePerLevel = define(
			builder, 10.0D, 0D, 1000D, "shield_additional_value_per_level", "护盾每级增加的护盾值");
	}
}
