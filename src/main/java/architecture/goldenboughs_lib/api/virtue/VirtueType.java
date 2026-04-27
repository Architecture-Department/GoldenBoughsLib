package architecture.goldenboughs_lib.api.virtue;

import architecture.goldenboughs_lib.api.ColourText;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibAttachments;
import architecture.goldenboughs_lib.util.ColorUtil;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

/**
 * 心核四德 简称 四德
 */
public enum VirtueType implements ColourText, StringRepresentable {
	/**
	 * 勇气 - 影响最大生命值
	 */
	FORTITUDE(0, "fortitude", LibAttachments.FORTITUDE, GoldenBoughsLib.modRlText("tooltip.fortitude"), "#ff0000"),
	/**
	 * 谨慎 - 影响最大精神
	 */
	PRUDENCE(1, "prudence", LibAttachments.PRUDENCE, GoldenBoughsLib.modRlText("tooltip.prudence"), "#ffffff"),
	/**
	 * 自律 - 影响挖掘速度 TODO 补充
	 */
	TEMPERANCE(2, "temperance", LibAttachments.TEMPERANCE, GoldenBoughsLib.modRlText("tooltip.temperance"), "#8a2be2"),
	/**
	 * 正义 - 影响移动速度和攻击速度
	 */
	JUSTICE(3, "justice", LibAttachments.JUSTICE, GoldenBoughsLib.modRlText("tooltip.justice"), "#00ffff"),
	/**
	 * 综合
	 */
	COMPOSITE(4, "composite", null, GoldenBoughsLib.modRlText("tooltip.composite_rating"), null);

	public static final Codec<VirtueType> CODEC = StringRepresentable
		.fromEnum(VirtueType::values);
	private static final IntFunction<VirtueType> BY_ID = ByIdMap
		.continuous(type -> type.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
	public static final StreamCodec<ByteBuf, VirtueType> STREAM_CODEC = ByteBufCodecs
		.idMapper(BY_ID, type -> type.id);

	private final int id;
	private final String name;
	private final String tooltipName;
	@Nullable
	private final Holder<AttachmentType<?>> attachmentTypeHolder;
	@Nullable
	private final String colour;
	private final int colourValue;

	VirtueType(int id, String name, @Nullable Holder<AttachmentType<?>> attachmentTypeHolder, String tooltipName, @Nullable String colour) {
		this.id = id;
		this.name = name;
		this.attachmentTypeHolder = attachmentTypeHolder;
		this.tooltipName = tooltipName;
		this.colour = colour;
		this.colourValue = colour == null ? 0 : ColorUtil.rgbColor(colour);
	}

	@Override
	public @NotNull String getSerializedName() {
		return GoldenBoughsLib.ID + "." + getName();
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	@Nullable
	public <T extends AbstractVirtue> Holder<AttachmentType<T>> getAttachmentTypeHolder() {
		return (Holder<AttachmentType<T>>) (Object) attachmentTypeHolder;
	}

	@Override
	public int getColourValue() {
		return colourValue;
	}

	@Override
	@Nullable
	public String getColourText() {
		return colour;
	}

	@Override
	@Nullable
	public String getColourName() {
		return name;
	}

	public String getTooltipName() {
		return tooltipName;
	}
}
