package architecture.goldenboughs_lib.client.particle.text;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TextParticleStrokeType implements StringRepresentable {
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

	public static Codec<TextParticleStrokeType> CODEC = StringRepresentable
		.fromEnum(TextParticleStrokeType::values).validate(DataResult::success);
	public static StreamCodec<ByteBuf, TextParticleStrokeType> STREAM_CODEC = ByteBufCodecs
		.idMapper(ByIdMap.continuous(TextParticleStrokeType::getIndex, values(), ByIdMap.OutOfBoundsStrategy.WRAP), TextParticleStrokeType::getIndex);
	private final int index;
	private final String name;

	TextParticleStrokeType(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	@Override
	public @NotNull String getSerializedName() {
		return name;
	}
}

