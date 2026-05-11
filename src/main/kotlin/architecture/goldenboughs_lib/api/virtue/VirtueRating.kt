package architecture.goldenboughs_lib.api.virtue

import architecture.goldenboughs_lib.core.Lib
import net.minecraft.util.StringRepresentable
import org.jetbrains.annotations.Contract
import java.util.*

/**
 * 四德评级
 */
enum class VirtueRating(
	val virtueName: String,
	val rating: Int,
	val minValue: Int
) : StringRepresentable {
	I("I", 1, 1),
	II("II", 2, 30),
	III("III", 3, 45),
	IV("IV", 4, 65),
	V("V", 5, 85),
	EX("EX", 6, 101),
	;

	@Contract(pure = true)
	override fun getSerializedName(): String {
		return Lib.ID + "." + this.virtueName.lowercase(Locale.getDefault())
	}

	companion object {
		@JvmField
		val REVERSE_LIST: MutableList<VirtueRating> =
			Collections.unmodifiableList(listOf(*entries.toTypedArray()).reversed())

		/**
		 * 获取给定数值对应的评级值
		 *
		 * @param value 数值
		 * @return 对应的评级值
		 */
		@JvmStatic
		fun getRatingValue(value: Int): Int {
			return getRating(value).rating
		}

		/**
		 * 获取给定数值对应的评级
		 *
		 * @param value 数值
		 * @return 对应的评级
		 */
		@JvmStatic
		fun getRating(value: Int): VirtueRating {
			for (virtueRating in REVERSE_LIST) {
				if (value >= virtueRating.minValue) {
					return virtueRating
				}
			}
			return I
		}
	}
}
