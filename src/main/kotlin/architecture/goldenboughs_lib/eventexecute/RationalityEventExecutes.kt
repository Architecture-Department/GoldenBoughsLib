package architecture.goldenboughs_lib.eventexecute

import architecture.goldenboughs_lib.config.LibConfig
import architecture.goldenboughs_lib.util.RationalityUtil.getMaxRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.getNaturalRecoveryRate
import architecture.goldenboughs_lib.util.RationalityUtil.getPauseRecoveryTick
import architecture.goldenboughs_lib.util.RationalityUtil.getRationalityRecoveryAmount
import architecture.goldenboughs_lib.util.RationalityUtil.getRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.modifyRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.setRecoveryTick
import net.minecraft.server.level.ServerPlayer

object RationalityEventExecutes {
	/**
	 * 自然恢复理智值
	 */
	fun refreshRationalityValue(player: ServerPlayer) {
		if (!LibConfig.SERVER.enableNaturalRationalityRationality.get()) {
			return
		}

		val recoveryTick = player.getPauseRecoveryTick()
		val isRecovery = recoveryTick > 0

		if (isRecovery) {
			player.setRecoveryTick(recoveryTick - 1)
			return
		}

		val value = player.getRationalityValue()
		if (value < 0 || value >= player.getMaxRationalityValue()) {
			return
		}

		val recoveryAmount = player.getRationalityRecoveryAmount()
		if (recoveryAmount > 0) {
			player.modifyRationalityValue(recoveryAmount, true)
		}

		val naturalRecoveryRate = getNaturalRecoveryRate(player)
		if (naturalRecoveryRate > 0) {
			player.setRecoveryTick((20 * naturalRecoveryRate).toInt())
		}
	}
}
