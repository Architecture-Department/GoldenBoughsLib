package architecture.goldenboughs_lib.client.particle.text;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TextParticleAlignType implements StringRepresentable {
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
	public static final Codec<TextParticleAlignType> CODEC = StringRepresentable
		.fromEnum(TextParticleAlignType::values).validate(DataResult::success);
	public static final StreamCodec<ByteBuf, TextParticleAlignType> STREAM_CODEC = ByteBufCodecs
		.idMapper(ByIdMap.continuous(TextParticleAlignType::getIndex, values(), ByIdMap.OutOfBoundsStrategy.WRAP), TextParticleAlignType::getIndex);
	private final int index;
	private final String name;

	TextParticleAlignType(int index, String name) {
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
