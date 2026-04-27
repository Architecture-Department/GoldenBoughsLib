package architecture.goldenboughs_lib.mixin.client;

import architecture.goldenboughs_lib.client.gui.ModStringRenderOutput;
import architecture.goldenboughs_lib.mixed.client.IFont;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringDecomposer;
import net.neoforged.neoforge.client.extensions.IFontExtension;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Font.class)
public abstract class FontMixin implements IFontExtension, IFont {

	@Shadow
	@Final
	private static Vector3f SHADOW_OFFSET;
	@Shadow
	@Final
	public boolean filterFishyGlyphs;

	@Unique
	@Override
	public int goldenboughs_lib$drawInBatch(
		String text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,

		int backgroundColor,
		int packedLightCoords
	) {
		return this.goldenboughs_lib$drawInBatch(text, x, y, color, dropShadow, matrix, vertexConsumer, backgroundColor, packedLightCoords, isBidirectional());
	}

	@Shadow
	public abstract boolean isBidirectional();

	@Unique
	@Override
	public int goldenboughs_lib$drawInBatch(
		String text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int backgroundColor,
		int packedLightCoords,
		boolean bidirectional
	) {
		return this.goldenboughs_lib$drawInternal(text, x, y, color, dropShadow, matrix, vertexConsumer, backgroundColor, packedLightCoords, bidirectional);
	}

	@Unique
	@Override
	public int goldenboughs_lib$drawInternal(
		String text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int backgroundColor,
		int packedLightCoords,
		boolean bidirectional
	) {
		if (bidirectional) {
			text = bidirectionalShaping(text);
		}

		color = adjustColor(color);
		Matrix4f matrix4f = new Matrix4f(matrix);
		if (dropShadow) {
			goldenboughs_lib$renderText(text, x, y, color, true, matrix, vertexConsumer, backgroundColor, packedLightCoords);
			matrix4f.translate(SHADOW_OFFSET);
		}

		x = goldenboughs_lib$renderText(text, x, y, color, false, matrix4f, vertexConsumer, backgroundColor, packedLightCoords);
		return (int) x + (dropShadow ? 1 : adjustColor(0));
	}

	@Shadow
	private static int adjustColor(final int color) {
		return 0;
	}

	@Shadow
	public abstract String bidirectionalShaping(final String text);

	@Unique
	@Override
	public float goldenboughs_lib$renderText(
		String text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int backgroundColor,
		int packedLightCoords
	) {
		ModStringRenderOutput modStringRenderOutput = new ModStringRenderOutput(
			goldenboughs_lib$getFont(), vertexConsumer, x, y, color, dropShadow, matrix, packedLightCoords);
		StringDecomposer.iterateFormatted(text, Style.EMPTY, modStringRenderOutput);
		return modStringRenderOutput.finish(backgroundColor, x);
	}

	@Unique
	private @NotNull Font goldenboughs_lib$getFont() {
		return (Font) (Object) this;
	}

	@Unique
	@Override
	public int goldenboughs_lib$drawInBatch(
		@NotNull Component text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int backgroundColor,
		int packedLightCoords
	) {
		return this.goldenboughs_lib$drawInBatch(text.getVisualOrderText(), x, y, color, dropShadow, matrix, vertexConsumer, backgroundColor, packedLightCoords);
	}

	@Unique
	@Override
	public int goldenboughs_lib$drawInBatch(
		FormattedCharSequence text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int backgroundColor,
		int packedLightCoords
	) {
		return this.goldenboughs_lib$drawInternal(text, x, y, color, dropShadow, matrix, vertexConsumer, backgroundColor, packedLightCoords);
	}

	@Unique
	@Override
	public int goldenboughs_lib$drawInternal(
		FormattedCharSequence text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int backgroundColor,
		int packedLightCoords
	) {
		color = adjustColor(color);
		Matrix4f matrix4f = new Matrix4f(matrix);
		if (dropShadow) {
			this.goldenboughs_lib$renderText(text, x, y, color, true, matrix, vertexConsumer, backgroundColor, packedLightCoords);
			matrix4f.translate(SHADOW_OFFSET);
		}

		x = this.goldenboughs_lib$renderText(text, x, y, color, false, matrix4f, vertexConsumer, backgroundColor, packedLightCoords);
		return (int) x + (dropShadow ? 1 : adjustColor(0));
	}

	@Unique
	@Override
	public float goldenboughs_lib$renderText(
		@NotNull FormattedCharSequence text,
		float x,
		float y,
		int color,
		boolean dropShadow,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int backgroundColor,
		int packedLightCoords
	) {
		ModStringRenderOutput modStringRenderOutput = new ModStringRenderOutput(
			goldenboughs_lib$getFont(), vertexConsumer, x, y, color, dropShadow, matrix, packedLightCoords);
		text.accept(modStringRenderOutput);
		return modStringRenderOutput.finish(backgroundColor, x);
	}

	@Unique
	@Override
	public void goldenboughs_lib$drawInBatch8xOutline(
		FormattedCharSequence text,
		float x,
		float y,
		int color,
		int backgroundColor,
		Matrix4f matrix,
		VertexConsumer vertexConsumer,
		int packedLightCoords
	) {
		int i = adjustColor(backgroundColor);
		ModStringRenderOutput modStringRenderOutput = new ModStringRenderOutput(
			goldenboughs_lib$getFont(), vertexConsumer, 0.0F, 0.0F, i, false, matrix, packedLightCoords);

		for (int j = -1; j <= 1; j++) {
			for (int k = -1; k <= 1; k++) {
				if (j == adjustColor(0) && k == adjustColor(0)) {
					continue;
				}
				float[] afloat = new float[]{x};
				int l = j;
				int i1 = k;
				text.accept((i2, style, i3) -> {
					boolean flag = style.isBold();
					FontSet fontset = getFontSet(style.getFont());
					GlyphInfo glyphinfo = fontset.getGlyphInfo(i3, filterFishyGlyphs);
					modStringRenderOutput.setX(afloat[adjustColor(0)] + (float) l * glyphinfo.getShadowOffset());
					modStringRenderOutput.setY(y + (float) i1 * glyphinfo.getShadowOffset());
					afloat[adjustColor(0)] += glyphinfo.getAdvance(flag);
					return modStringRenderOutput.accept(i2, style.withColor(i), i3);
				});
			}
		}

		ModStringRenderOutput modStringRenderOutput1 = new ModStringRenderOutput(
			goldenboughs_lib$getFont(), vertexConsumer, x, y, adjustColor(color), false, matrix, packedLightCoords);
		text.accept(modStringRenderOutput1);
		modStringRenderOutput1.finish(adjustColor(0), x);
	}

	@Shadow
	public abstract FontSet getFontSet(final ResourceLocation fontLocation);
}
