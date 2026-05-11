package architecture.goldenboughs_lib.client.particle.text

import architecture.goldenboughs_lib.api.virtue.network.codec.CompositeStreamCodecBuilder
import architecture.goldenboughs_lib.init.LibParticleTypes
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.function.Function

@JvmRecord
data class TextParticleOptions(
	@JvmField val textComponent: MutableList<Component>,
	@JvmField val fontColor: Int,
	@JvmField val strokeColor: Int,
	@JvmField val particleLifeTime: Int,
	@JvmField val size: Float,
	@JvmField val alignType: TextParticleAlignType,
	@JvmField val isShine: Boolean,
	@JvmField val strokeType: TextParticleStrokeType,
	@JvmField val xRot: Float,
	@JvmField val yRot: Float,
	@JvmField val isTargetingPlayers: Boolean,
	@JvmField val isThrough: Boolean
) : ParticleOptions {
	val build: TextParticleBuilder
		get() = TextParticleBuilder(
			this.textComponent,
			this.fontColor,
			this.strokeColor,
			this.particleLifeTime,
			this.size,
			this.alignType,
			this.isShine,
			this.strokeType,
			this.xRot,
			this.yRot,
			this.isTargetingPlayers,
			this.isThrough
		)

	fun buildParticle(level: ClientLevel, x: Double, y: Double, z: Double): TextParticle {
		return TextParticle(level, x, y, z, this)
	}

	override fun getType(): ParticleType<TextParticleOptions> {
		return LibParticleTypes.TEXT.get()
	}

	companion object {
		@JvmField
		val CODEC: MapCodec<TextParticleOptions> =
			RecordCodecBuilder.mapCodec(Function { thisOptionsInstance: RecordCodecBuilder.Instance<TextParticleOptions> ->
				thisOptionsInstance.group(
					Codec.list(ComponentSerialization.CODEC).fieldOf("textComponentList")
						.forGetter(
							TextParticleOptions::textComponent
						),
					Codec.INT.fieldOf("fontColor").forGetter(TextParticleOptions::fontColor),
					Codec.INT.fieldOf("strokeColor").forGetter(TextParticleOptions::strokeColor),
					Codec.INT.fieldOf("particleLifeTime")
						.forGetter(TextParticleOptions::particleLifeTime),
					Codec.FLOAT.fieldOf("size").forGetter(TextParticleOptions::size),
					TextParticleAlignType.CODEC.fieldOf("align")
						.forGetter(TextParticleOptions::alignType),
					Codec.BOOL.fieldOf("isShine").forGetter(TextParticleOptions::isShine),
					TextParticleStrokeType.CODEC.fieldOf("strokeType")
						.forGetter(TextParticleOptions::strokeType),
					Codec.FLOAT.fieldOf("xRot").forGetter(TextParticleOptions::xRot),
					Codec.FLOAT.fieldOf("yRot").forGetter(TextParticleOptions::yRot),
					Codec.BOOL.fieldOf("isTargetingPlayers")
						.forGetter(TextParticleOptions::isTargetingPlayers),
					Codec.BOOL.fieldOf("isSeeThrough").forGetter(TextParticleOptions::isThrough)
				).apply(
					thisOptionsInstance, ::TextParticleOptions
				)
			})

		@JvmField
		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TextParticleOptions> =
			CompositeStreamCodecBuilder.builder<RegistryFriendlyByteBuf, TextParticleOptions>()
				.withComponent(
					ComponentSerialization.STREAM_CODEC.apply(
						ByteBufCodecs.list()
					), TextParticleOptions::textComponent
				)
				.withComponent(ByteBufCodecs.INT, TextParticleOptions::fontColor)
				.withComponent(ByteBufCodecs.INT, TextParticleOptions::strokeColor)
				.withComponent(ByteBufCodecs.INT, TextParticleOptions::particleLifeTime)
				.withComponent(ByteBufCodecs.FLOAT, TextParticleOptions::size)
				.withComponent(
					TextParticleAlignType.STREAM_CODEC,
					TextParticleOptions::alignType
				)
				.withComponent(ByteBufCodecs.BOOL, TextParticleOptions::isShine)
				.withComponent(
					TextParticleStrokeType.STREAM_CODEC,
					TextParticleOptions::strokeType
				)
				.withComponent(ByteBufCodecs.FLOAT, TextParticleOptions::xRot)
				.withComponent(ByteBufCodecs.FLOAT, TextParticleOptions::yRot)
				.withComponent(ByteBufCodecs.BOOL, TextParticleOptions::isTargetingPlayers)
				.withComponent(ByteBufCodecs.BOOL, TextParticleOptions::isThrough)
				.decoderFactory { components: MutableIterator<Any> ->
					TextParticleOptions(
						components.next() as MutableList<Component>,
						components.next() as Int,
						components.next() as Int,
						components.next() as Int,
						components.next() as Float,
						components.next() as TextParticleAlignType,
						components.next() as Boolean,
						components.next() as TextParticleStrokeType,
						components.next() as Float,
						components.next() as Float,
						components.next() as Boolean,
						components.next() as Boolean
					)
				}.build()
	}
}
