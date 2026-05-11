package architecture.goldenboughs_lib.client.particle.text

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable

enum class TextParticleAlignType(
	@JvmField val index: Int,
	val typeName: String
) : StringRepresentable {
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

	override fun getSerializedName(): String {
		return typeName
	}

	companion object {
		@JvmField
		val CODEC: Codec<TextParticleAlignType> = StringRepresentable
			.fromEnum { entries.toTypedArray() }
			.validate { result: TextParticleAlignType -> DataResult.success(result) }

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, TextParticleAlignType> = ByteBufCodecs
			.idMapper(
				ByIdMap.continuous(
					{ obj: TextParticleAlignType -> obj.index },
					entries.toTypedArray(),
					ByIdMap.OutOfBoundsStrategy.WRAP
				)
			) { obj: TextParticleAlignType -> obj.index }
	}
}
