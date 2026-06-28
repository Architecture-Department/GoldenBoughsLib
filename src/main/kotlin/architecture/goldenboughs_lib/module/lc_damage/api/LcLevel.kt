package architecture.goldenboughs_lib.module.lc_damage.api

import architecture.goldenboughs_lib.api.ColourText
import architecture.goldenboughs_lib.util.ColorUtil
import architecture.goldenboughs_lib.util.EnumCodec
import architecture.goldenboughs_lib.util.EnumStreamCodec
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
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
) : ColourText {
	ZAYIN(0, 1, "zayin", "#00ff00"),
	TETH(1, 2, "teth", "#1e90ff"),
	HE(2, 3, "he", "#ffff00"),
	WAW(3, 4, "waw", "#8a2be2"),
	ALEPH(4, 5, "aleph", "#ff0000");

	companion object {
		@JvmField
		val CODEC: Codec<LcLevel> = EnumCodec.create(LcLevel::class)

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, LcLevel> = EnumStreamCodec.create(LcLevel::class)

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

	val levelName: String = name

	override val colourValue: Int = ColorUtil.rgbColor(colour)
	override val colourName = levelName

	override val colourText = colour
}
