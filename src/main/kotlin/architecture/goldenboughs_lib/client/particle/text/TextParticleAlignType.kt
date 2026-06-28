package architecture.goldenboughs_lib.client.particle.text

import architecture.goldenboughs_lib.util.EnumCodec
import architecture.goldenboughs_lib.util.EnumStreamCodec
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

enum class TextParticleAlignType(
	@JvmField val index: Int,
	val typeName: String
) {
	/**
	 * 左对齐
	 */
	LEFT(0, "left"),

	/**
	 * 居中对齐
	 */
	CENTER(1, "center"),

	/**
	 * 右对齐
	 */
	RIGHT(2, "right");

	companion object {
		@JvmField
		val CODEC: Codec<TextParticleAlignType> = EnumCodec.create(TextParticleAlignType::class)

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, TextParticleAlignType> = EnumStreamCodec.create(TextParticleAlignType::class)
	}
}
