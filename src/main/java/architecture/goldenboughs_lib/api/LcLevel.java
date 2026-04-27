package architecture.goldenboughs_lib.api;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.util.ColorUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 脑叶等级
 * <p>
 * 如果获取的LcLevel是null那无视任何等级效果，例如高等级减少来自低等级的伤害之类的，而是一视同仁
 */
public enum LcLevel implements ColourText, StringRepresentable {
	ZAYIN(0, 1, "zayin", "#00ff00"),
	TETH(1, 2, "teth", "#1e90ff"),
	HE(2, 3, "he", "#ffff00"),
	WAW(3, 4, "waw", "#8a2be2"),
	ALEPH(4, 5, "aleph", "#ff0000");

	public static final Codec<LcLevel> CODEC = StringRepresentable
		.fromEnum(LcLevel::values).validate(DataResult::success);
	public static final StreamCodec<ByteBuf, LcLevel> STREAM_CODEC = ByteBufCodecs
		.idMapper(ByIdMap.continuous(Enum::ordinal, values(),
			ByIdMap.OutOfBoundsStrategy.WRAP), Enum::ordinal);
	private static final Map<Integer, LcLevel> LC_LEVEL_MAP = new HashMap<>();

	static {
		Arrays.stream(LcLevel.values()).forEach(value -> LC_LEVEL_MAP.put(value.levelValue, value));
	}

	private final String name;
	private final int levelValue;
	private final String colour;
	private final int colourValue;
	private final int id;

	LcLevel(int id, int levelValue, String name, String colour) {
		this.id = id;
		this.name = name;
		this.levelValue = levelValue;
		this.colour = colour;
		this.colourValue = ColorUtil.rgbColor(colour);
	}

	public static @NotNull LcLevel byLevel(int level) {
		return LC_LEVEL_MAP.getOrDefault(level, ZAYIN);
	}

	public int getLevelValue() {
		return levelValue;
	}

	@Override
	public int getColourValue() {
		return colourValue;
	}

	@Override
	public String getColourText() {
		return colour;
	}

	@Override
	public String getColourName() {
		return name;
	}

	@Contract(pure = true)
	@Override
	public @NotNull String getSerializedName() {
		return GoldenBoughsLib.modRlText(getName());
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
