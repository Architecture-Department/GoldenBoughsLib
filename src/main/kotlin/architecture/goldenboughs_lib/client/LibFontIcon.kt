package architecture.goldenboughs_lib.client

import architecture.goldenboughs_lib.core.GoldenBoughsLib
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable

enum class LibFontIcon(
	val index: Int,
	val fontIconName: String,
	val charName: String,
	val fileRl: ResourceLocation,
	val width: Int,
	val height: Int
) : StringRepresentable {
	PHYSICS(
		0,
		"physics",
		"\uf000",
		GoldenBoughsLib.modRl("particle/damage_type/physics"),
		16, 16
	),
	PHYSICS_8X(
		1,
		"physics8x",
		"\uf001",
		GoldenBoughsLib.modRl("gui/sprites/physics8x"),
		8, 8
	),
	SPIRIT(
		2,
		"spirit",
		"\uf002",
		GoldenBoughsLib.modRl("particle/damage_type/spirit"),
		16, 16
	),
	SPIRIT_8X(
		3,
		"spirit8x",
		"\uf003",
		GoldenBoughsLib.modRl("gui/sprites/spirit8x"),
		8, 8
	),
	RATIONALITY_ADD(
		4,
		"rationality_add",
		"\uf004",
		GoldenBoughsLib.modRl("particle/damage_type/rationality_add"),
		16, 16
	),
	RATIONALITY_REDUCE(
		5,
		"rationality_reduce",
		"\uf005",
		GoldenBoughsLib.modRl("particle/damage_type/rationality_reduce"),
		16, 16
	),
	EROSION(
		6,
		"erosion",
		"\uf006",
		GoldenBoughsLib.modRl("particle/damage_type/erosion"),
		16, 16
	),
	EROSION_8X(
		7,
		"erosion8x",
		"\uf007",
		GoldenBoughsLib.modRl("gui/sprites/erosion8x"),
		8, 8
	),
	THE_SOUL(
		8,
		"the_soul",
		"\uf008",
		GoldenBoughsLib.modRl("particle/damage_type/the_soul"),
		16, 16
	),
	THE_SOUL_8X(
		9,
		"the_soul8x",
		"\uf009",
		GoldenBoughsLib.modRl("gui/sprites/the_soul8x"),
		8, 8
	),
	MAGIC(
		10,
		"magic",
		"\uf010",
		GoldenBoughsLib.modRl("gui/sprites/the_soul8x"),
		8, 8
	),
	;

	val component: MutableComponent
		get() = CACHE.computeIfAbsent(this.charName) { chars: String ->
			Component.literal(chars)
				.setStyle(Style.EMPTY.withFont(GoldenBoughsLib.modRl("goldenboughs_lib_icon")))
		}

	override fun getSerializedName(): String {
		return fontIconName
	}

	companion object {
		@JvmStatic
		private val CACHE: MutableMap<String, MutableComponent> = HashMap()
	}
}
