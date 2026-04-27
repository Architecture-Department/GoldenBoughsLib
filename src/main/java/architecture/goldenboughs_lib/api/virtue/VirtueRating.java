package architecture.goldenboughs_lib.api.virtue;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * 四德评级
 */
public enum VirtueRating implements StringRepresentable {
	I("I", 1, 1),
	II("II", 2, 30),
	III("III", 3, 45),
	IV("IV", 4, 65),
	V("V", 5, 85),
	EX("EX", 6, 101),
	;
	public static final List<VirtueRating> REVERSE_LIST = Collections.unmodifiableList(List.of(VirtueRating.values()).reversed());

	private final String name;
	private final int rating;
	private final int minValue;

	VirtueRating(String name, int rating, int minValue) {
		this.name = name;
		this.rating = rating;
		this.minValue = minValue;
	}

	/**
	 * 获取给定数值对应的评级值
	 *
	 * @param value 数值
	 * @return 对应的评级值
	 */
	public static int getRatingValue(int value) {
		return getRating(value).getRating();
	}

	/**
	 * 获取给定数值对应的评级
	 *
	 * @param value 数值
	 * @return 对应的评级
	 */
	public static VirtueRating getRating(int value) {
		for (VirtueRating virtueRating : REVERSE_LIST) {
			if (value < virtueRating.minValue) {
				continue;
			}
			return virtueRating;
		}
		return I;
	}

	public int getRating() {
		return rating;
	}

	public int getMinValue() {
		return minValue;
	}

	@Contract(pure = true)
	@Override
	public @NotNull String getSerializedName() {
		return GoldenBoughsLib.ID + "." + getName().toLowerCase();
	}

	public String getName() {
		return name;
	}
}
