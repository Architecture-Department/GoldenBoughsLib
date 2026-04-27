package architecture.goldenboughs_lib.util;

import architecture.goldenboughs_lib.api.virtue.VirtueFortitude;
import architecture.goldenboughs_lib.api.virtue.VirtueJustice;
import architecture.goldenboughs_lib.api.virtue.VirtuePrudence;
import architecture.goldenboughs_lib.api.virtue.VirtueTemperance;
import architecture.goldenboughs_lib.init.LibAttachments;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * 四德属性系统
 * <p>
 * 属性机制，包括勇气（Fortitude）、谨慎（Prudence）、自律（Temperance）和正义（Justice），
 * 每种属性都会影响玩家的不同能力。
 */
public final class VirtueUtil {
	/**
	 * 获取综合评级
	 * <p>
	 * 计算并返回四个基本属性评级之和的综合评价。
	 *
	 * @return 综合评级
	 */
	public static int getCompositeRatting(Player player) {
		int rating = getFortitudeRating(player) + getPrudenceRating(player) + getTemperanceRating(player) + getJusticeRating(player);
		if (rating >= 16) {
			return 5;
		}
		return Mth.clamp(rating / 3, 1, 4);
	}

	public static int getJusticeRating(final Player player) {
		return getJustice(player).getRatingPoints();
	}

	public static VirtueJustice getJustice(final Player player) {
		return player.getData(LibAttachments.JUSTICE);
	}

	public static int getTemperanceRating(final Player player) {
		return getTemperance(player).getRatingPoints();
	}

	public static VirtueTemperance getTemperance(final Player player) {
		return player.getData(LibAttachments.TEMPERANCE);
	}

	public static int getPrudenceRating(final Player player) {
		return getPrudence(player).getRatingPoints();
	}

	public static VirtuePrudence getPrudence(final Player player) {
		return player.getData(LibAttachments.PRUDENCE);
	}

	public static int getFortitudeRating(final Player player) {
		return getFortitude(player).getRatingPoints();
	}

	public static VirtueFortitude getFortitude(final Player player) {
		return player.getData(LibAttachments.FORTITUDE);
	}
}
