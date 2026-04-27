package architecture.goldenboughs_lib.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

/**
 * GUI工具类，提供各种绘制精灵图的方法
 */
public final class GuiUtil {

	/**
	 * 绘制精灵图
	 *
	 * @param guiGraphics GUI图形上下文
	 * @param sprite      精灵图资源位置
	 * @param x           绘制位置x坐标
	 * @param y           绘制位置y坐标
	 * @param width       绘制宽度
	 * @param height      绘制高度
	 */
	public static void blitSprite(
		@NotNull GuiGraphics guiGraphics,
		ResourceLocation sprite,
		float x,
		float y,
		float width,
		float height
	) {
		blitSprite(guiGraphics, sprite, x, y, 0, width, height);
	}

	/**
	 * 绘制精灵图
	 *
	 * @param guiGraphics GUI图形上下文
	 * @param sprite      精灵图资源位置
	 * @param x           绘制位置x坐标
	 * @param y           绘制位置y坐标
	 * @param blitOffset  绘制偏移量
	 * @param width       绘制宽度
	 * @param height      绘制高度
	 */
	public static void blitSprite(
		@NotNull GuiGraphics guiGraphics,
		ResourceLocation sprite,
		float x,
		float y,
		float blitOffset,
		float width,
		float height
	) {
		TextureAtlasSprite textureatlassprite = guiGraphics.sprites.getSprite(sprite);
		GuiSpriteScaling guispritescaling = guiGraphics.sprites.getSpriteScaling(textureatlassprite);

		switch (guispritescaling) {
			case GuiSpriteScaling.Stretch stretch -> {
				blitSprite(guiGraphics, textureatlassprite, x, y, blitOffset, width, height);
			}
			case GuiSpriteScaling.Tile(int width1, int height1) -> {
				blitTiledSprite(guiGraphics, textureatlassprite, x, y, blitOffset, width, height, 0, 0, width1, height1, width1, height1);
			}
			case GuiSpriteScaling.NineSlice guispritescaling$nineslice ->
				blitNineSlicedSprite(guiGraphics, textureatlassprite, guispritescaling$nineslice, x, y, blitOffset, width, height);
			default -> {
			}
		}
	}

	/**
	 * 绘制九宫格缩放精灵图
	 *
	 * @param guiGraphics GUI图形上下文
	 * @param sprite      纹理图集精灵
	 * @param nineSlice   九宫格缩放信息
	 * @param x           绘制位置x坐标
	 * @param y           绘制位置y坐标
	 * @param blitOffset  绘制偏移量
	 * @param width       绘制宽度
	 * @param height      绘制高度
	 */
	public static void blitNineSlicedSprite(
		@NotNull GuiGraphics guiGraphics,
		TextureAtlasSprite sprite,
		GuiSpriteScaling.@NotNull NineSlice nineSlice,
		float x,
		float y,
		float blitOffset,
		float width,
		float height
	) {
		GuiSpriteScaling.NineSlice.Border guispritescaling$nineslice$border = nineSlice.border();
		float i = Math.min(guispritescaling$nineslice$border.left(), width / 2);
		float j = Math.min(guispritescaling$nineslice$border.right(), width / 2);
		float k = Math.min(guispritescaling$nineslice$border.top(), height / 2);
		float l = Math.min(guispritescaling$nineslice$border.bottom(), height / 2);
		if (width == nineSlice.width() && height == nineSlice.height()) {
			blitSprite(guiGraphics, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, blitOffset, width, height);
			return;
		}

		// 处理高度相等的情况
		if (height == nineSlice.height()) {
			blitSprite(guiGraphics, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, blitOffset, i, height);
			blitTiledSprite(
				guiGraphics,
				sprite,
				x + i,
				y,
				blitOffset,
				width - j - i,
				height,
				i,
				0,
				nineSlice.width() - j - i,
				nineSlice.height(),
				nineSlice.width(),
				nineSlice.height()
			);
			blitSprite(
				guiGraphics, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, blitOffset, j, height
			);
			return;
		}

		// 处理宽度相等的情况
		if (width == nineSlice.width()) {
			blitSprite(guiGraphics, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, blitOffset, width, k);
			blitTiledSprite(guiGraphics,
				sprite,
				x,
				y + k,
				blitOffset,
				width,
				height - l - k,
				0,
				k,
				nineSlice.width(),
				nineSlice.height() - l - k,
				nineSlice.width(),
				nineSlice.height()
			);
			blitSprite(guiGraphics,
				sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, blitOffset, width, l
			);
			return;
		}

		// 处理一般情况，分别绘制九个区域
		blitSprite(guiGraphics, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, blitOffset, i, k);
		blitTiledSprite(guiGraphics,
			sprite, x + i, y, blitOffset, width - j - i, k, i, 0, nineSlice.width() - j - i, k, nineSlice.width(), nineSlice.height()
		);
		blitSprite(guiGraphics, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, blitOffset, j, k);
		blitSprite(guiGraphics, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, blitOffset, i, l);
		blitTiledSprite(guiGraphics,
			sprite,
			x + i,
			y + height - l,
			blitOffset,
			width - j - i,
			l,
			i,
			nineSlice.height() - l,
			nineSlice.width() - j - i,
			l,
			nineSlice.width(),
			nineSlice.height()
		);
		blitSprite(guiGraphics,
			sprite,
			nineSlice.width(),
			nineSlice.height(),
			nineSlice.width() - j,
			nineSlice.height() - l,
			x + width - j,
			y + height - l,
			blitOffset,
			j,
			l
		);
		blitTiledSprite(guiGraphics,
			sprite,
			x,
			y + k,
			blitOffset,
			i,
			height - l - k,
			0,
			k,
			i,
			nineSlice.height() - l - k,
			nineSlice.width(),
			nineSlice.height()
		);
		blitTiledSprite(guiGraphics,
			sprite,
			x + i,
			y + k,
			blitOffset,
			width - j - i,
			height - l - k,
			i,
			k,
			nineSlice.width() - j - i,
			nineSlice.height() - l - k,
			nineSlice.width(),
			nineSlice.height()
		);
		blitTiledSprite(guiGraphics,
			sprite,
			x + width - j,
			y + k,
			blitOffset,
			i,
			height - l - k,
			nineSlice.width() - j,
			k,
			j,
			nineSlice.height() - l - k,
			nineSlice.width(),
			nineSlice.height()
		);
	}

	/**
	 * 平铺绘制精灵图
	 *
	 * @param guiGraphics     GUI图形上下文
	 * @param sprite          纹理图集精灵
	 * @param x               绘制位置x坐标
	 * @param y               绘制位置y坐标
	 * @param blitOffset      绘制偏移量
	 * @param width           绘制宽度
	 * @param height          绘制高度
	 * @param uPosition       UV坐标的u起始位置
	 * @param vPosition       UV坐标的v起始位置
	 * @param spriteWidth     精灵图宽度
	 * @param spriteHeight    精灵图高度
	 * @param nineSliceWidth  九宫格切片宽度
	 * @param nineSliceHeight 九宫格切片高度
	 */
	public static void blitTiledSprite(
		@NotNull GuiGraphics guiGraphics,
		TextureAtlasSprite sprite,
		float x,
		float y,
		float blitOffset,
		float width,
		float height,
		float uPosition,
		float vPosition,
		float spriteWidth,
		float spriteHeight,
		float nineSliceWidth,
		float nineSliceHeight
	) {
		if (width <= 0 || height <= 0) {
			return;
		}

		if (spriteWidth <= 0 || spriteHeight <= 0) {
			throw new IllegalArgumentException("Tiled sprite texture size must be positive, got " + spriteWidth + "x" + spriteHeight);
		}

		for (float i = 0; i < width; i += spriteWidth) {
			float j = Math.min(spriteWidth, width - i);

			for (float k = 0; k < height; k += spriteHeight) {
				float l = Math.min(spriteHeight, height - k);
				blitSprite(guiGraphics, sprite, nineSliceWidth, nineSliceHeight, uPosition, vPosition, x + i, y + k, blitOffset, j, l);
			}
		}
	}

	/**
	 * 绘制精灵图
	 *
	 * @param guiGraphics   GUI图形上下文
	 * @param sprite        精灵图资源位置
	 * @param textureWidth  纹理宽度
	 * @param textureHeight 纹理高度
	 * @param uPosition     UV坐标的u起始位置
	 * @param vPosition     UV坐标的v起始位置
	 * @param x             绘制位置x坐标
	 * @param y             绘制位置y坐标
	 * @param uWidth        UV坐标的u宽度
	 * @param vHeight       UV坐标的v高度
	 */
	public static void blitSprite(
		@NotNull GuiGraphics guiGraphics,
		ResourceLocation sprite,
		float textureWidth,
		float textureHeight,
		float uPosition,
		float vPosition,
		float x,
		float y,
		float uWidth,
		float vHeight
	) {
		blitSprite(guiGraphics, sprite, textureWidth, textureHeight, uPosition, vPosition, x, y, 0, uWidth, vHeight);
	}

	/**
	 * 绘制精灵图
	 *
	 * @param guiGraphics   GUI图形上下文
	 * @param sprite        精灵图资源位置
	 * @param textureWidth  纹理宽度
	 * @param textureHeight 纹理高度
	 * @param uPosition     UV坐标的u起始位置
	 * @param vPosition     UV坐标的v起始位置
	 * @param x             绘制位置x坐标
	 * @param y             绘制位置y坐标
	 * @param blitOffset    绘制偏移量
	 * @param uWidth        UV坐标的u宽度
	 * @param vHeight       UV坐标的v高度
	 */
	public static void blitSprite(
		@NotNull GuiGraphics guiGraphics,
		ResourceLocation sprite,
		float textureWidth,
		float textureHeight,
		float uPosition,
		float vPosition,
		float x,
		float y,
		float blitOffset,
		float uWidth,
		float vHeight
	) {
		TextureAtlasSprite textureatlassprite = guiGraphics.sprites.getSprite(sprite);
		GuiSpriteScaling guispritescaling = guiGraphics.sprites.getSpriteScaling(textureatlassprite);
		if (guispritescaling instanceof GuiSpriteScaling.Stretch) {
			blitSprite(guiGraphics, textureatlassprite, textureWidth, textureHeight, uPosition, vPosition, x, y, blitOffset, uWidth, vHeight);
			return;
		}

		blitSprite(guiGraphics, textureatlassprite, x, y, blitOffset, uWidth, vHeight);
	}

	/**
	 * 绘制精灵图
	 *
	 * @param guiGraphics   GUI图形上下文
	 * @param sprite        纹理图集精灵
	 * @param textureWidth  纹理宽度
	 * @param textureHeight 纹理高度
	 * @param uPosition     UV坐标的u起始位置
	 * @param vPosition     UV坐标的v起始位置
	 * @param x             绘制位置x坐标
	 * @param y             绘制位置y坐标
	 * @param blitOffset    绘制偏移量
	 * @param uWidth        UV坐标的u宽度
	 * @param vHeight       UV坐标的v高度
	 */
	public static void blitSprite(
		@NotNull GuiGraphics guiGraphics,
		TextureAtlasSprite sprite,
		float textureWidth,
		float textureHeight,
		float uPosition,
		float vPosition,
		float x,
		float y,
		float blitOffset,
		float uWidth,
		float vHeight
	) {
		if (uWidth == 0 || vHeight == 0) {
			return;
		}
		innerBlit(
			guiGraphics,
			sprite.atlasLocation(),
			x,
			x + uWidth,
			y,
			y + vHeight,
			blitOffset,
			sprite.getU(uPosition / textureWidth),
			sprite.getU((uPosition + uWidth) / textureWidth),
			sprite.getV(vPosition / textureHeight),
			sprite.getV((vPosition + vHeight) / textureHeight)
		);
	}

	/**
	 * 内部绘制方法，执行实际的顶点绘制操作
	 *
	 * @param guiGraphics   GUI图形上下文
	 * @param atlasLocation 图集资源位置
	 * @param x1            左侧x坐标
	 * @param x2            右侧x坐标
	 * @param y1            上方y坐标
	 * @param y2            下方y坐标
	 * @param blitOffset    绘制偏移量
	 * @param minU          最小U坐标
	 * @param maxU          最大U坐标
	 * @param minV          最小V坐标
	 * @param maxV          最大V坐标
	 */
	private static void innerBlit(
		@NotNull GuiGraphics guiGraphics,
		ResourceLocation atlasLocation,
		float x1,
		float x2,
		float y1,
		float y2,
		float blitOffset,
		float minU,
		float maxU,
		float minV,
		float maxV
	) {
		RenderSystem.setShaderTexture(0, atlasLocation);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.addVertex(matrix4f, x1, y1, blitOffset).setUv(minU, minV);
		bufferbuilder.addVertex(matrix4f, x1, y2, blitOffset).setUv(minU, maxV)
			.addVertex(matrix4f, x2, y2, blitOffset).setUv(maxU, maxV)
			.addVertex(matrix4f, x2, y1, blitOffset).setUv(maxU, minV);
		BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
	}

	/**
	 * 绘制精灵图
	 *
	 * @param guiGraphics GUI图形上下文
	 * @param sprite      纹理图集精灵
	 * @param x           绘制位置x坐标
	 * @param y           绘制位置y坐标
	 * @param blitOffset  绘制偏移量
	 * @param width       绘制宽度
	 * @param height      绘制高度
	 */
	public static void blitSprite(
		GuiGraphics guiGraphics,
		TextureAtlasSprite sprite,
		float x,
		float y,
		float blitOffset,
		float width,
		float height
	) {
		if (width == 0 || height == 0) {
			return;
		}
		innerBlit(
			guiGraphics,
			sprite.atlasLocation(),
			x,
			x + width,
			y,
			y + height,
			blitOffset,
			sprite.getU0(),
			sprite.getU1(),
			sprite.getV0(),
			sprite.getV1()
		);
	}

	public static void fill(@NotNull GuiGraphics guiGraphics, float minX, float minY, float maxX, float maxY, int color) {
		fill(guiGraphics, minX, minY, maxX, maxY, 0, color);
	}

	public static void fill(@NotNull GuiGraphics guiGraphics, float minX, float minY, float maxX, float maxY, float z, int color) {
		fill(guiGraphics, RenderType.gui(), minX, minY, maxX, maxY, z, color);
	}

	public static void fill(@NotNull GuiGraphics guiGraphics, RenderType renderType, float minX, float minY, float maxX, float maxY, float z, int color) {
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		if (minX < maxX) {
			float i = minX;
			minX = maxX;
			maxX = i;
		}

		if (minY < maxY) {
			float j = minY;
			minY = maxY;
			maxY = j;
		}

		VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
		vertexconsumer.addVertex(matrix4f, minX, minY, z).setColor(color);
		vertexconsumer.addVertex(matrix4f, minX, maxY, z).setColor(color);
		vertexconsumer.addVertex(matrix4f, maxX, maxY, z).setColor(color);
		vertexconsumer.addVertex(matrix4f, maxX, minY, z).setColor(color);
		guiGraphics.flushIfUnmanaged();
	}

	public static void fill(@NotNull GuiGraphics guiGraphics, RenderType renderType, float minX, float minY, float maxX, float maxY, int color) {
		fill(guiGraphics, renderType, minX, minY, maxX, maxY, 0, color);
	}

	public static void fillGradient(@NotNull GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, int colorFrom, int colorTo) {
		fillGradient(guiGraphics, x1, y1, x2, y2, 0, colorFrom, colorTo);
	}

	public static void fillGradient(@NotNull GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, float z, int colorFrom, int colorTo) {
		fillGradient(guiGraphics, RenderType.gui(), x1, y1, x2, y2, colorFrom, colorTo, z);
	}

	public static void fillGradient(@NotNull GuiGraphics guiGraphics, RenderType renderType, float x1, float y1, float x2, float y2, int colorFrom, int colorTo, float z) {
		VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
		fillGradient(guiGraphics, vertexconsumer, x1, y1, x2, y2, z, colorFrom, colorTo);
		guiGraphics.flushIfUnmanaged();
	}

	public static void fillGradient(@NotNull GuiGraphics guiGraphics, VertexConsumer consumer, float x1, float y1, float x2, float y2, float z, int colorFrom, int colorTo) {
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		consumer.addVertex(matrix4f, x1, y1, z).setColor(colorFrom);
		consumer.addVertex(matrix4f, x1, y2, z).setColor(colorTo);
		consumer.addVertex(matrix4f, x2, y2, z).setColor(colorTo);
		consumer.addVertex(matrix4f, x2, y1, z).setColor(colorFrom);
	}

	public static void fillRenderType(@NotNull GuiGraphics guiGraphics, RenderType renderType, float x1, float y1, float x2, float y2, float z) {
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
		vertexconsumer.addVertex(matrix4f, x1, y1, z);
		vertexconsumer.addVertex(matrix4f, x1, y2, z);
		vertexconsumer.addVertex(matrix4f, x2, y2, z);
		vertexconsumer.addVertex(matrix4f, x2, y1, z);
		guiGraphics.flushIfUnmanaged();
	}
}
