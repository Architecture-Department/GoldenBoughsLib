package architecture.goldenboughs_lib.client.particle.text

import architecture.goldenboughs_lib.init.LibParticleTypes
import architecture.goldenboughs_lib.util.LibUtil.COMPONENT_SERIALIZATION_LIST_STREAM_CODEC
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
	@JvmField val textComponent: List<Component>,
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
			this.textComponent.toMutableList(),
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
			StreamCodec.of(
				{ buf, p ->
					COMPONENT_SERIALIZATION_LIST_STREAM_CODEC.encode(buf, p.textComponent)
					ByteBufCodecs.INT.encode(buf, p.fontColor)
					ByteBufCodecs.INT.encode(buf, p.strokeColor)
					ByteBufCodecs.INT.encode(buf, p.particleLifeTime)
					ByteBufCodecs.FLOAT.encode(buf, p.size)
					TextParticleAlignType.STREAM_CODEC.encode(buf, p.alignType)
					ByteBufCodecs.BOOL.encode(buf, p.isShine)
					TextParticleStrokeType.STREAM_CODEC.encode(buf, p.strokeType)
					ByteBufCodecs.FLOAT.encode(buf, p.xRot)
					ByteBufCodecs.FLOAT.encode(buf, p.yRot)
					ByteBufCodecs.BOOL.encode(buf, p.isTargetingPlayers)
					ByteBufCodecs.BOOL.encode(buf, p.isThrough)
				},
				{ buf ->
					TextParticleOptions(
						COMPONENT_SERIALIZATION_LIST_STREAM_CODEC.decode(buf),
						ByteBufCodecs.INT.decode(buf),
						ByteBufCodecs.INT.decode(buf),
						ByteBufCodecs.INT.decode(buf),
						ByteBufCodecs.FLOAT.decode(buf),
						TextParticleAlignType.STREAM_CODEC.decode(buf),
						ByteBufCodecs.BOOL.decode(buf),
						TextParticleStrokeType.STREAM_CODEC.decode(buf),
						ByteBufCodecs.FLOAT.decode(buf),
						ByteBufCodecs.FLOAT.decode(buf),
						ByteBufCodecs.BOOL.decode(buf),
						ByteBufCodecs.BOOL.decode(buf),
					)
				}
			)
	}
}
