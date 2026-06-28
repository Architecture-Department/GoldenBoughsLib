package architecture.goldenboughs_lib.module.virtue.api

import architecture.goldenboughs_lib.api.ColourText
import architecture.goldenboughs_lib.init.LibAttachmentTypes
import architecture.goldenboughs_lib.util.ColorUtil.rgbColor
import architecture.goldenboughs_lib.util.EnumCodec
import architecture.goldenboughs_lib.util.EnumStreamCodec
import architecture.goldenboughs_lib.util.LibUtil
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.attachment.AttachmentType

/**
 * 心核四德 简称 四德
 */
enum class VirtueType(
	val id: Int,
	override val colourName: String,
	private val attachmentTypeHolder: Holder<AttachmentType<*>>?,
	@JvmField val tooltipName: String,
	colour: String?
) : ColourText {
	/**
	 * 勇气 - 影响最大生命值
	 */
	FORTITUDE(
		0,
		"fortitude",
		LibAttachmentTypes.FORTITUDE,
		LibUtil.modRlText("tooltip.fortitude"),
		"#ff0000"
	),

	/**
	 * 谨慎 - 影响最大精神
	 */
	PRUDENCE(
		1,
		"prudence",
		LibAttachmentTypes.PRUDENCE,
		LibUtil.modRlText("tooltip.prudence"),
		"#ffffff"
	),

	/**
	 * 自律 - 影响挖掘速度 TODO 补充
	 */
	TEMPERANCE(
		2,
		"temperance",
		LibAttachmentTypes.TEMPERANCE,
		LibUtil.modRlText("tooltip.temperance"),
		"#8a2be2"
	),

	/**
	 * 正义 - 影响移动速度和攻击速度
	 */
	JUSTICE(
		3,
		"justice",
		LibAttachmentTypes.JUSTICE,
		LibUtil.modRlText("tooltip.justice"),
		"#00ffff"
	),

	/**
	 * 综合
	 */
	COMPOSITE(
		4,
		"composite",
		null,
		LibUtil.modRlText("tooltip.composite_rating"),
		null
	);

	override val colourText: String = colour ?: ""
	override val colourValue: Int = colour?.run { rgbColor(colour) } ?: 0

	fun <T : AbstractVirtue> getAttachmentTypeHolder(): Holder<AttachmentType<T>> {
		@Suppress("UNCHECKED_CAST")
		return attachmentTypeHolder as Holder<AttachmentType<T>>
	}

	companion object {
		@JvmField
		var CODEC: Codec<VirtueType> = EnumCodec.create(VirtueType::class)

		@JvmField
		var STREAM_CODEC: StreamCodec<ByteBuf, VirtueType> = EnumStreamCodec.create(VirtueType::class)
	}
}
