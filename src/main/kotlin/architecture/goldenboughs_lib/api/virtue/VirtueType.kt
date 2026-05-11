package architecture.goldenboughs_lib.api.virtue

import architecture.goldenboughs_lib.api.ColourText
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibAttachmentTypes
import architecture.goldenboughs_lib.util.ColorUtil.rgbColor
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import net.neoforged.neoforge.attachment.AttachmentType
import java.util.function.IntFunction
import java.util.function.Supplier

/**
 * 心核四德 简称 四德
 */
enum class VirtueType(
	val id: Int,
	name: String,
	private val attachmentTypeHolder: Holder<AttachmentType<*>>?,
	@JvmField val tooltipName: String,
	colour: String?
) : ColourText, StringRepresentable {
	/**
	 * 勇气 - 影响最大生命值
	 */
	FORTITUDE(
		0,
		"fortitude",
		LibAttachmentTypes.FORTITUDE,
		Lib.modRlText("tooltip.fortitude"),
		"#ff0000"
	),

	/**
	 * 谨慎 - 影响最大精神
	 */
	PRUDENCE(
		1,
		"prudence",
		LibAttachmentTypes.PRUDENCE,
		Lib.modRlText("tooltip.prudence"),
		"#ffffff"
	),

	/**
	 * 自律 - 影响挖掘速度 TODO 补充
	 */
	TEMPERANCE(
		2,
		"temperance",
		LibAttachmentTypes.TEMPERANCE,
		Lib.modRlText("tooltip.temperance"),
		"#8a2be2"
	),

	/**
	 * 正义 - 影响移动速度和攻击速度
	 */
	JUSTICE(
		3,
		"justice",
		LibAttachmentTypes.JUSTICE,
		Lib.modRlText("tooltip.justice"),
		"#00ffff"
	),

	/**
	 * 综合
	 */
	COMPOSITE(
		4,
		"composite",
		null,
		Lib.modRlText("tooltip.composite_rating"),
		null
	);

	override val colourName: String = name

	override val colourText: String = colour ?: ""

	override val colourValue: Int = colour?.run { rgbColor(colour) } ?: 0

	override fun getSerializedName(): String {
		return Lib.ID + "." + this.colourName
	}

	fun <T : AbstractVirtue> getAttachmentTypeHolder(): Holder<AttachmentType<T>> {
		@Suppress("UNCHECKED_CAST")
		return attachmentTypeHolder as Holder<AttachmentType<T>>
	}

	companion object {
		@JvmField
		val CODEC: Codec<VirtueType> = StringRepresentable
			.fromEnum<VirtueType>(Supplier { entries.toTypedArray() })

		private val BY_ID: IntFunction<VirtueType> = ByIdMap
			.continuous(
				VirtueType::id,
				entries.toTypedArray(),
				ByIdMap.OutOfBoundsStrategy.ZERO
			)

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, VirtueType> = ByteBufCodecs
			.idMapper(BY_ID, VirtueType::id)
	}
}
