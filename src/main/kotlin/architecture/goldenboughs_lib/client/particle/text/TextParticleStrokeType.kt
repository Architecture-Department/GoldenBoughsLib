package architecture.goldenboughs_lib.client.particle.text

import architecture.goldenboughs_lib.util.EnumCodec
import architecture.goldenboughs_lib.util.EnumStreamCodec
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

enum class TextParticleStrokeType(@JvmField val index: Int, val typeName: String) {
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
		var CODEC: Codec<TextParticleStrokeType> = EnumCodec.create(TextParticleStrokeType::class)

		@JvmField
		var STREAM_CODEC: StreamCodec<ByteBuf, TextParticleStrokeType> =
			EnumStreamCodec.create(TextParticleStrokeType::class)
	}
}

