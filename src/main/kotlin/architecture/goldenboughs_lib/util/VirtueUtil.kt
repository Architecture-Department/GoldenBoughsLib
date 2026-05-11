package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.api.virtue.VirtueFortitude
import architecture.goldenboughs_lib.api.virtue.VirtueJustice
import architecture.goldenboughs_lib.api.virtue.VirtuePrudence
import architecture.goldenboughs_lib.api.virtue.VirtueTemperance
import architecture.goldenboughs_lib.init.LibAttachmentTypes
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player

/**
 * 四德属性系统
 * 
 * 
 * 属性机制，包括勇气（Fortitude）、谨慎（Prudence）、自律（Temperance）和正义（Justice），
 * 每种属性都会影响玩家的不同能力。
 */
object VirtueUtil {
	/**
	 * 获取综合评级
	 *
	 *
	 * 计算并返回四个基本属性评级之和的综合评价。
	 *
	 * @return 综合评级
	 */
	@JvmStatic
	fun Player.getCompositeRatting(): Int {
		val rating = getFortitudeRating() + getPrudenceRating() + getTemperanceRating() + getJusticeRating()
		if (rating >= 16) {
			return 5
		}
		return Mth.clamp(rating / 3, 1, 4)
	}

	@JvmStatic
	fun Player.getJusticeRating(): Int {
		return getJustice().ratingPoints
	}

	@JvmStatic
	fun Player.getJustice(): VirtueJustice {
		return getData(LibAttachmentTypes.JUSTICE)
	}

	@JvmStatic
	fun Player.getTemperanceRating(): Int {
		return getTemperance().ratingPoints
	}

	@JvmStatic
	fun Player.getTemperance(): VirtueTemperance {
		return getData(LibAttachmentTypes.TEMPERANCE)
	}

	@JvmStatic
	fun Player.getPrudenceRating(): Int {
		return getPrudence().ratingPoints
	}

	@JvmStatic
	fun Player.getPrudence(): VirtuePrudence {
		return getData(LibAttachmentTypes.PRUDENCE)
	}

	@JvmStatic
	fun Player.getFortitudeRating(): Int {
		return getFortitude().ratingPoints
	}

	@JvmStatic
	fun Player.getFortitude(): VirtueFortitude {
		return getData(LibAttachmentTypes.FORTITUDE)
	}
}
