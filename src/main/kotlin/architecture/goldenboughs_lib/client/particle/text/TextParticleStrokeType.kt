package architecture.goldenboughs_lib.client.particle.text

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable

enum class TextParticleStrokeType(@JvmField val index: Int, val typeName: String) : StringRepresentable {
	/**
	 * 无描边
	 */
	NONE(0, "none"),

	/**
	 * 阴影
	 */
	SHADOW(1, "shadow"),

	/**
	 * 描边
	 */
	STROKE(2, "stroke");

	override fun getSerializedName(): String {
		return typeName
	}

	companion object {
		@JvmField
		var CODEC: Codec<TextParticleStrokeType> = StringRepresentable
			.fromEnum { entries.toTypedArray() }
			.validate { result: TextParticleStrokeType -> DataResult.success(result) }

		@JvmField
		var STREAM_CODEC: StreamCodec<ByteBuf, TextParticleStrokeType> = ByteBufCodecs
			.idMapper(
				ByIdMap.continuous(
					TextParticleStrokeType::index,
					entries.toTypedArray(),
					ByIdMap.OutOfBoundsStrategy.WRAP
				), TextParticleStrokeType::index
			)
	}
}

