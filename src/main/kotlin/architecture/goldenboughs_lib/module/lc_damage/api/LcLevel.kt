package architecture.goldenboughs_lib.module.lc_damage.api

import architecture.goldenboughs_lib.api.ColourText

import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.util.ColorUtil
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import org.jetbrains.annotations.Contract
import java.util.*

/**
 * 脑叶等级
 * 
 * 
 * 如果获取的LcLevel是null那无视任何等级效果，例如高等级减少来自低等级的伤害之类的，而是一视同仁
 */
enum class LcLevel(
	val id: Int,
	val levelValue: Int,
	name: String,
	colour: String
) : ColourText, StringRepresentable {
	ZAYIN(0, 1, "zayin", "#00ff00"),
	TETH(1, 2, "teth", "#1e90ff"),
	HE(2, 3, "he", "#ffff00"),
	WAW(3, 4, "waw", "#8a2be2"),
	ALEPH(4, 5, "aleph", "#ff0000");

	val levelName: String = name
	override val colourValue: Int = ColorUtil.rgbColor(colour)

	override val colourName = levelName
	override val colourText = colour

	@Contract(pure = true)
	override fun getSerializedName(): String {
		return LibConstants.modRlText(this.levelName)
	}

	companion object {
		@JvmField
		val CODEC: Codec<LcLevel> = StringRepresentable
			.fromEnum { entries.toTypedArray() }
			.validate { DataResult.success(it) }

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, LcLevel> = ByteBufCodecs
			.idMapper(
				ByIdMap.continuous(
					LcLevel::ordinal, entries.toTypedArray(),
					ByIdMap.OutOfBoundsStrategy.WRAP
				), LcLevel::ordinal
			)

		private val LC_LEVEL_MAP = HashMap<Int, LcLevel>()

		init {
			Arrays.stream(entries.toTypedArray())
				.forEach { value -> LC_LEVEL_MAP[value.levelValue] = value }
		}

		@JvmStatic
		fun byLevel(level: Int): LcLevel {
			return LC_LEVEL_MAP.getOrDefault(level, ZAYIN)
		}
	}
}
