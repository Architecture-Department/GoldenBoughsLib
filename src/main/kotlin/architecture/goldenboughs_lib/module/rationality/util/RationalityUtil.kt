package architecture.goldenboughs_lib.module.rationality.util

import architecture.goldenboughs_lib.core.LibEventHooks
import architecture.goldenboughs_lib.init.LibAttachmentTypes
import architecture.goldenboughs_lib.init.LibAttributes
import architecture.goldenboughs_lib.util.ParticleUtil
import net.minecraft.world.entity.player.Player

/**
 * 理智工具
 */
object RationalityUtil {
	/**
	 * 修改理智值
	 */
	@JvmStatic
	fun Player.modifyRationalityValue(value: Float, isEvent: Boolean, isParticle: Boolean) {
		setRationalityValue(getRationalityValue() + value, isEvent, isParticle)
	}


	/**
	 * 修改理智值
	 */
	@JvmStatic
	fun Player.modifyRationalityValue(value: Float, isEvent: Boolean) {
		setRationalityValue(getRationalityValue() + value, isEvent, true)
	}

	/**
	 * 限制理智
	 */
	@JvmStatic
	fun Player.restrictRationalityValue() {
		restrictRationalityValue(getRationalityValue())
	}

	/**
	 * 限制理智
	 */
	@JvmStatic
	private fun Player.restrictRationalityValue(value: Float) {
		val maxRationalityValue = getMaxRationalityValue()
		setRationalityValue(
			Math.clamp(value, -maxRationalityValue, maxRationalityValue),
			isEvent = false,
			isParticle = false
		)
	}

	@JvmStatic
	fun Player.setRationalityValue(value: Float, isEvent: Boolean) {
		setRationalityValue(value, isEvent, true)
	}

	/**
	 * 设置理智值
	 */
	@JvmStatic
	fun Player.setRationalityValue(value: Float, isEvent: Boolean = true, isParticle: Boolean = true) {
		val oldValue = getRationalityValue()
		var newValue = value
		if (isEvent) {
			val pre = LibEventHooks.sourceRationalityPre(this, oldValue, value)
			if (pre.isCanceled) {
				return
			}
			newValue = pre.newValue
		}

		val maxRationalityValue = getMaxRationalityValue()

		if (oldValue != newValue) {
			setData(
				LibAttachmentTypes.RATIONALITY,
				Math.clamp(newValue, -maxRationalityValue, maxRationalityValue)
			)
		}

		if (isEvent) {
			LibEventHooks.sourceRationalityPost(this, oldValue, newValue)
		}

		if (isParticle) {
			val particles = oldValue - newValue
			ParticleUtil.createDamageTextParticles(this, particles, true, particles < 0)
		}
	}

	/**
	 * 获取理智值
	 */
	@JvmStatic
	fun Player.getRationalityValue(): Float {
		val maxRationalityValue = getMaxRationalityValue()
		return Math.clamp(getData(LibAttachmentTypes.RATIONALITY), -maxRationalityValue, maxRationalityValue)
	}

	/**
	 * 获取最大理智值
	 */
	@JvmStatic
	fun Player.getMaxRationalityValue(): Float {
		return getAttributeValue(LibAttributes.MAX_RATIONALITY).toFloat()
	}

	/**
	 * 设置基本最大理智值
	 */
	@JvmStatic
	fun setBaseMaxValue(player: Player, value: Float) {
		player.attributes.getInstance(LibAttributes.MAX_RATIONALITY)?.baseValue = value.toDouble()
	}

	/**
	 * 获取理智值自然恢复效率
	 */
	@JvmStatic
	fun getNaturalRecoveryRate(player: Player): Float {
		return player.getAttributeValue(LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME).toFloat()
	}

	/**
	 * 设置基本理智值自然恢复效率
	 */
	@JvmStatic
	fun setBaseNaturalRecoveryRate(player: Player, value: Float) {
		player.attributes
			.getInstance(LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME)?.baseValue =
			value.toDouble()
	}

	/**
	 * 获取理智值自然恢复量
	 */
	@JvmStatic
	fun Player.getRationalityRecoveryAmount(): Float {
		restrictRationalityValue()
		return getAttributeValue(LibAttributes.RATIONALITY_RECOVERY_AMOUNT).toFloat()
	}

	/**
	 * 设置基本理智值自然恢复量
	 */
	@JvmStatic
	fun Player.setBaseRationalityRecoveryAmount(value: Float) {
		attributes.getInstance(LibAttributes.RATIONALITY_RECOVERY_AMOUNT)?.baseValue = value.toDouble()
	}

	/**
	 * 获取暂停恢复理智tick
	 */
	@JvmStatic
	fun Player.getPauseRecoveryTick(): Int {
		return getData(LibAttachmentTypes.RATIONALITY_PAUSE_RECOVERY_TICK)
	}

	/**
	 * 设置恢复暂停理智tick
	 */
	@JvmStatic
	fun Player.setRecoveryTick(value: Int) {
		setData(LibAttachmentTypes.RATIONALITY_PAUSE_RECOVERY_TICK, value)
	}
}
