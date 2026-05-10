package architecture.goldenboughs_lib.eventexecute;

import architecture.goldenboughs_lib.util.RationalityUtil;
import net.minecraft.server.level.ServerPlayer;

public final class RationalityEventExecutes {
	/**
	 * 自然恢复理智值
	 */
	public static void refreshRationalityValue(ServerPlayer player) {
		if (!architecture.goldenboughs_lib.config.LibConfig.SERVER.enableNaturalRationalityRationality.get()) {
			return;
		}

		int recoveryTick = RationalityUtil.getPauseRecoveryTick(player);
		boolean isRecovery = recoveryTick > 0;

		if (isRecovery) {
			RationalityUtil.setRecoveryTick(player, recoveryTick - 1);
			return;
		}

		float value = RationalityUtil.getRationalityValue(player);
		if (value < 0 || value >= RationalityUtil.getMaxRationalityValue(player)) {
			return;
		}

		float recoveryAmount = RationalityUtil.getRationalityRecoveryAmount(player);
		if (recoveryAmount > 0) {
			RationalityUtil.modifyRationalityValue(player, recoveryAmount, true);
		}

		float naturalRecoveryRate = RationalityUtil.getNaturalRecoveryRate(player);
		if (naturalRecoveryRate > 0) {
			RationalityUtil.setRecoveryTick(player, (int) (20 * naturalRecoveryRate));
		}
	}
}
