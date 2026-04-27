package architecture.goldenboughs_lib.client.particle.text;

import architecture.goldenboughs_lib.init.LibParticleTypes;
import architecture.imaginarycraft.network.codec.CompositeStreamCodecBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TextParticleOptions(
	List<Component> textComponent,
	int fontColor,
	int strokeColor,
	int particleLifeTime,
	float size,
	TextParticleAlignType alignType,
	boolean isShine,
	TextParticleStrokeType strokeType,
	float xRot,
	float yRot,
	boolean isTargetingPlayers,
	boolean isThrough
) implements ParticleOptions {
	public static final MapCodec<TextParticleOptions> CODEC = RecordCodecBuilder.mapCodec((thisOptionsInstance) -> thisOptionsInstance.group(
		Codec.list(ComponentSerialization.CODEC).fieldOf("textComponentList").forGetter(TextParticleOptions::textComponent),
		Codec.INT.fieldOf("fontColor").forGetter(TextParticleOptions::fontColor),
		Codec.INT.fieldOf("strokeColor").forGetter(TextParticleOptions::strokeColor),
		Codec.INT.fieldOf("particleLifeTime").forGetter(TextParticleOptions::particleLifeTime),
		Codec.FLOAT.fieldOf("size").forGetter(TextParticleOptions::size),
		TextParticleAlignType.CODEC.fieldOf("align").forGetter(TextParticleOptions::alignType),
		Codec.BOOL.fieldOf("isShine").forGetter(TextParticleOptions::isShine),
		TextParticleStrokeType.CODEC.fieldOf("strokeType").forGetter(TextParticleOptions::strokeType),
		Codec.FLOAT.fieldOf("xRot").forGetter(TextParticleOptions::xRot),
		Codec.FLOAT.fieldOf("yRot").forGetter(TextParticleOptions::yRot),
		Codec.BOOL.fieldOf("isTargetingPlayers").forGetter(TextParticleOptions::isTargetingPlayers),
		Codec.BOOL.fieldOf("isSeeThrough").forGetter(TextParticleOptions::isThrough)
	).apply(thisOptionsInstance, TextParticleOptions::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, TextParticleOptions> STREAM_CODEC = CompositeStreamCodecBuilder.<RegistryFriendlyByteBuf, TextParticleOptions>builder()
		.withComponent(ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list()), TextParticleOptions::textComponent)
		.withComponent(ByteBufCodecs.INT, TextParticleOptions::fontColor)
		.withComponent(ByteBufCodecs.INT, TextParticleOptions::strokeColor)
		.withComponent(ByteBufCodecs.INT, TextParticleOptions::particleLifeTime)
		.withComponent(ByteBufCodecs.FLOAT, TextParticleOptions::size)
		.withComponent(TextParticleAlignType.STREAM_CODEC, TextParticleOptions::alignType)
		.withComponent(ByteBufCodecs.BOOL, TextParticleOptions::isShine)
		.withComponent(TextParticleStrokeType.STREAM_CODEC, TextParticleOptions::strokeType)
		.withComponent(ByteBufCodecs.FLOAT, TextParticleOptions::xRot)
		.withComponent(ByteBufCodecs.FLOAT, TextParticleOptions::yRot)
		.withComponent(ByteBufCodecs.BOOL, TextParticleOptions::isTargetingPlayers)
		.withComponent(ByteBufCodecs.BOOL, TextParticleOptions::isThrough)
		.decoderFactory(components -> new TextParticleOptions(
			(List<Component>) components.next(),
			(int) components.next(),
			(int) components.next(),
			(int) components.next(),
			(float) components.next(),
			(TextParticleAlignType) components.next(),
			(boolean) components.next(),
			(TextParticleStrokeType) components.next(),
			(float) components.next(),
			(float) components.next(),
			(boolean) components.next(),
			(boolean) components.next()
		)).build();

	public TextParticleBuilder getBuild() {
		return new TextParticleBuilder(
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
		);
	}

	public TextParticle buildParticle(ClientLevel level, double x, double y, double z) {
		return new TextParticle(level, x, y, z, this);
	}

	@Override
	public @NotNull ParticleType<TextParticleOptions> getType() {
		return LibParticleTypes.TEXT.get();
	}
}
