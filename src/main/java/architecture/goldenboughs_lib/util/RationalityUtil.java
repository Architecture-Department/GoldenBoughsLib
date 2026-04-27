package architecture.goldenboughs_lib.util;

import architecture.goldenboughs_lib.init.LibAttachments;
import architecture.goldenboughs_lib.init.LibAttributes;
import architecture.imaginarycraft.core.ModEventHooks;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

/**
 * 理智工具
 */
public class RationalityUtil {
	/**
	 * 修改理智值
	 */
	public static void modifyValue(Player player, float value, boolean isEvent, boolean isParticle) {
		setValue(player, getValue(player) + value, isEvent, isParticle);
	}


	/**
	 * 修改理智值
	 */
	public static void modifyValue(Player player, float value, boolean isEvent) {
		setValue(player, getValue(player) + value, isEvent, true);
	}

	/**
	 * 限制理智
	 */
	public static void restrictValue(Player player) {
		restrictValue(player, getValue(player));
	}

	/**
	 * 限制理智
	 */
	private static void restrictValue(Player player, float value) {
		float maxRationalityValue = getMaxValue(player);
		setValue(player, Math.clamp(value, -maxRationalityValue, maxRationalityValue), false, false);
	}

	public static void setValue(Player player, float value, boolean isEvent) {
		setValue(player, value, isEvent, true);
	}

	/**
	 * 设置理智值
	 */
	public static void setValue(Player player, float value, boolean isEvent, boolean isParticle) {
		float oldValue = getValue(player);
		float newValue = value;
		if (isEvent) {
			Map.Entry<Boolean, Float> pre = ModEventHooks.sourceRationalityPre(player, oldValue, value);
			newValue = pre.getValue();
			if (pre.getKey()) {
				return;
			}
		}

		float maxRationalityValue = getMaxValue(player);

		if (oldValue != newValue) {
			player.setData(LibAttachments.RATIONALITY, Math.clamp(newValue, -maxRationalityValue, maxRationalityValue));
		}

		if (isEvent) {
			ModEventHooks.sourceRationalityPost(player, oldValue, newValue);
		}

		if (isParticle) {
			float particles = oldValue - newValue;
			ParticleUtil.createDamageTextParticles(player, particles, true, particles < 0);
		}
	}

	/**
	 * 获取理智值
	 */
	public static float getValue(Player player) {
		float maxRationalityValue = getMaxValue(player);
		return Math.clamp(player.getData(LibAttachments.RATIONALITY), -maxRationalityValue, maxRationalityValue);
	}

	/**
	 * 获取最大理智值
	 */
	public static float getMaxValue(Player player) {
		return (float) player.getAttributeValue(LibAttributes.MAX_RATIONALITY);
	}

	/**
	 * 设置基本最大理智值
	 */
	public static void setBaseMaxValue(Player player, float value) {
		player.getAttributes().getInstance(LibAttributes.MAX_RATIONALITY).setBaseValue(value);
	}

	/**
	 * 获取理智值自然恢复效率
	 */
	public static float getNaturalRecoveryRate(Player player) {
		return (float) player.getAttributeValue(LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME);
	}

	/**
	 * 设置基本理智值自然恢复效率
	 */
	public static void setBaseNaturalRecoveryRate(Player player, float value) {
		player.getAttributes().getInstance(LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME).setBaseValue(value);
	}

	/**
	 * 获取理智值自然恢复量
	 */
	public static float getRationalityRecoveryAmount(Player player) {
		restrictValue(player);
		return (float) player.getAttributeValue(LibAttributes.RATIONALITY_RECOVERY_AMOUNT);
	}

	/**
	 * 设置基本理智值自然恢复量
	 */
	public static void setBaseRationalityRecoveryAmount(Player player, float value) {
		player.getAttributes().getInstance(LibAttributes.RATIONALITY_RECOVERY_AMOUNT).setBaseValue(value);
	}

	/**
	 * 获取暂停恢复理智tick
	 */
	public static int getPauseRecoveryTick(Player player) {
		return player.getData(LibAttachments.RATIONALITY_PAUSE_RECOVERY_TICK);
	}

	/**
	 * 设置恢复暂停理智tick
	 */
	public static void setRecoveryTick(Player player, int value) {
		player.setData(LibAttachments.RATIONALITY_PAUSE_RECOVERY_TICK, value);
	}
}
