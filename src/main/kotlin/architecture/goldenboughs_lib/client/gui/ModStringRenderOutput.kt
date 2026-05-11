package architecture.goldenboughs_lib.client.gui

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.font.glyphs.BakedGlyph
import net.minecraft.client.gui.font.glyphs.EmptyGlyph
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSink
import org.joml.Matrix4f

class ModStringRenderOutput(
	private val font: Font,
	private val vertexConsumer: VertexConsumer,
	@JvmField var x: Float,
	@JvmField var y: Float,
	color: Int,
	private val dropShadow: Boolean,
	private val pose: Matrix4f,
	private val packedLightCoords: Int
) : FormattedCharSink {
	private val dimFactor: Float = if (dropShadow) 0.25f else 1.0f
	private val r: Float = (color shr 16 and 0xFF).toFloat() / 255.0f * this.dimFactor
	private val g: Float = (color shr 8 and 0xFF).toFloat() / 255.0f * this.dimFactor
	private val b: Float = (color and 0xFF).toFloat() / 255.0f * this.dimFactor
	private val a: Float = (color shr 24 and 0xFF).toFloat() / 255.0f
	private var effects: MutableList<BakedGlyph.Effect>? = null

	override fun accept(positionInCurrentSequence: Int, style: Style, codePoint: Int): Boolean {
		val fontSet = font.getFontSet(style.getFont())
		val glyphInfo = fontSet.getGlyphInfo(codePoint, font.filterFishyGlyphs)
		val bakedGlyph =
			if (style.isObfuscated && codePoint != 32) fontSet.getRandomGlyph(glyphInfo) else fontSet.getGlyph(
				codePoint
			)
		val flag = style.isBold
		val f3 = this.a
		val textcolor = style.getColor()
		val f: Float
		val f1: Float
		val f2: Float
		if (textcolor != null) {
			val i = textcolor.value
			f = (i shr 16 and 0xFF).toFloat() / 255.0f * this.dimFactor
			f1 = (i shr 8 and 0xFF).toFloat() / 255.0f * this.dimFactor
			f2 = (i and 0xFF).toFloat() / 255.0f * this.dimFactor
		} else {
			f = this.r
			f1 = this.g
			f2 = this.b
		}

		if (bakedGlyph !is EmptyGlyph) {
			val f5 = if (flag) glyphInfo.boldOffset else 0.0f
			val f4 = if (this.dropShadow) glyphInfo.shadowOffset else 0.0f
			font.renderChar(
				bakedGlyph,
				flag,
				style.isItalic,
				f5,
				this.x + f4,
				this.y + f4,
				this.pose,
				this.vertexConsumer,
				f,
				f1,
				f2,
				f3,
				this.packedLightCoords
			)
		}

		val f6 = glyphInfo.getAdvance(flag)
		val f7 = if (this.dropShadow) 1.0f else 0.0f
		if (style.isStrikethrough) {
			this.addEffect(
				BakedGlyph.Effect(
					this.x + f7 - 1.0f,
					this.y + f7 + 4.5f,
					this.x + f7 + f6,
					this.y + f7 + 4.5f - 1.0f,
					0.01f, f, f1, f2, f3
				)
			)
		}

		if (style.isUnderlined) {
			this.addEffect(
				BakedGlyph.Effect(
					this.x + f7 - 1.0f,
					this.y + f7 + 9.0f,
					this.x + f7 + f6,
					this.y + f7 + 9.0f - 1.0f,
					0.01f, f, f1, f2, f3
				)
			)
		}

		this.x += f6
		return true
	}

	private fun addEffect(effect: BakedGlyph.Effect) {
		if (this.effects == null) {
			this.effects = mutableListOf()
		}

		this.effects?.add(effect)
	}

	fun finish(backgroundColor: Int, x: Float): Float {
		if (backgroundColor != 0) {
			val f = (backgroundColor shr 24 and 0xFF).toFloat() / 255.0f
			val f1 = (backgroundColor shr 16 and 0xFF).toFloat() / 255.0f
			val f2 = (backgroundColor shr 8 and 0xFF).toFloat() / 255.0f
			val f3 = (backgroundColor and 0xFF).toFloat() / 255.0f
			this.addEffect(
				BakedGlyph.Effect(
					x - 1.0f,
					this.y + 9.0f,
					this.x + 1.0f,
					this.y - 1.0f,
					0.01f,
					f1,
					f2,
					f3,
					f
				)
			)
		}

		if (this.effects != null) {
			val bakedGlyph = font.getFontSet(Style.DEFAULT_FONT).whiteGlyph()

			for (bakedGlyphEffect in this.effects) {
				bakedGlyph.renderEffect(bakedGlyphEffect, this.pose, this.vertexConsumer, this.packedLightCoords)
			}
		}

		return this.x
	}
}
