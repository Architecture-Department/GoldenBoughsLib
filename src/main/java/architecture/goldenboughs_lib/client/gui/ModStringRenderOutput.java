package architecture.goldenboughs_lib.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSink;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.List;

public class ModStringRenderOutput implements FormattedCharSink {
	private final VertexConsumer vertexConsumer;
	private final boolean dropShadow;
	private final float dimFactor;
	private final float r;
	private final float g;
	private final float b;
	private final float a;
	private final Matrix4f pose;
	private final int packedLightCoords;
	private final Font font;
	private float x;
	private float y;
	@Nullable
	private List<BakedGlyph.Effect> effects;

	public ModStringRenderOutput(
		final Font font,
		final VertexConsumer vertexConsumer,
		final float x,
		final float y,
		final int color,
		final boolean dropShadow,
		final Matrix4f pose,
		final int packedLightCoords
	) {
		this.vertexConsumer = vertexConsumer;
		this.x = x;
		this.y = y;
		this.dropShadow = dropShadow;
		this.dimFactor = dropShadow ? 0.25F : 1.0F;
		this.r = (float) (color >> 16 & 0xFF) / 255.0F * this.dimFactor;
		this.g = (float) (color >> 8 & 0xFF) / 255.0F * this.dimFactor;
		this.b = (float) (color & 0xFF) / 255.0F * this.dimFactor;
		this.a = (float) (color >> 24 & 0xFF) / 255.0F;
		this.pose = pose;
		this.packedLightCoords = packedLightCoords;
		this.font = font;
	}

	@Override
	public boolean accept(final int positionInCurrentSequence, final Style style, final int codePoint) {
		FontSet fontset = font.getFontSet(style.getFont());
		GlyphInfo glyphinfo = fontset.getGlyphInfo(codePoint, font.filterFishyGlyphs);
		BakedGlyph bakedglyph = style.isObfuscated() && codePoint != 32 ? fontset.getRandomGlyph(glyphinfo) : fontset.getGlyph(codePoint);
		boolean flag = style.isBold();
		float f3 = this.a;
		TextColor textcolor = style.getColor();
		float f;
		float f1;
		float f2;
		if (textcolor != null) {
			int i = textcolor.getValue();
			f = (float) (i >> 16 & 0xFF) / 255.0F * this.dimFactor;
			f1 = (float) (i >> 8 & 0xFF) / 255.0F * this.dimFactor;
			f2 = (float) (i & 0xFF) / 255.0F * this.dimFactor;
		} else {
			f = this.r;
			f1 = this.g;
			f2 = this.b;
		}

		if (!(bakedglyph instanceof EmptyGlyph)) {
			float f5 = flag ? glyphinfo.getBoldOffset() : 0.0F;
			float f4 = this.dropShadow ? glyphinfo.getShadowOffset() : 0.0F;
			font.renderChar(
				bakedglyph,
				flag,
				style.isItalic(),
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
			);
		}

		float f6 = glyphinfo.getAdvance(flag);
		float f7 = this.dropShadow ? 1.0F : 0.0F;
		if (style.isStrikethrough()) {
			this.addEffect(new BakedGlyph.Effect(
				this.x + f7 - 1.0F,
				this.y + f7 + 4.5F,
				this.x + f7 + f6,
				this.y + f7 + 4.5F - 1.0F,
				0.01F, f, f1, f2, f3));
		}

		if (style.isUnderlined()) {
			this.addEffect(new BakedGlyph.Effect(
				this.x + f7 - 1.0F,
				this.y + f7 + 9.0F,
				this.x + f7 + f6,
				this.y + f7 + 9.0F - 1.0F,
				0.01F, f, f1, f2, f3
			));
		}

		this.x += f6;
		return true;
	}

	private void addEffect(BakedGlyph.Effect effect) {
		if (this.effects == null) {
			this.effects = Lists.newArrayList();
		}

		this.effects.add(effect);
	}

	public float finish(int backgroundColor, float x) {
		if (backgroundColor != 0) {
			float f = (float) (backgroundColor >> 24 & 0xFF) / 255.0F;
			float f1 = (float) (backgroundColor >> 16 & 0xFF) / 255.0F;
			float f2 = (float) (backgroundColor >> 8 & 0xFF) / 255.0F;
			float f3 = (float) (backgroundColor & 0xFF) / 255.0F;
			this.addEffect(new BakedGlyph.Effect(
				x - 1.0F,
				this.y + 9.0F,
				this.x + 1.0F,
				this.y - 1.0F,
				0.01F,
				f1,
				f2,
				f3,
				f
			));
		}

		if (this.effects != null) {
			BakedGlyph bakedglyph = font.getFontSet(Style.DEFAULT_FONT).whiteGlyph();

			for (BakedGlyph.Effect bakedglyph$effect : this.effects) {
				bakedglyph.renderEffect(bakedglyph$effect, this.pose, this.vertexConsumer, this.packedLightCoords);
			}
		}

		return this.x;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
