package architecture.goldenboughs_lib.util

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.resources.ResourceLocation
import kotlin.math.min

/**
 * GUI工具类，提供各种绘制精灵图的方法
 */
object GuiUtil {
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
	@JvmStatic
	fun GuiGraphics.blitSprite(
		sprite: ResourceLocation,
		x: Float,
		y: Float,
		width: Float,
		height: Float
	) {
		blitSprite(sprite, x, y, 0f, width, height)
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
	@JvmStatic
	fun GuiGraphics.blitSprite(
		sprite: ResourceLocation,
		x: Float,
		y: Float,
		blitOffset: Float,
		width: Float,
		height: Float
	) {
		val textureatlassprite = sprites.getSprite(sprite)
		when (val guispritescaling = sprites.getSpriteScaling(textureatlassprite)) {
			is GuiSpriteScaling.Stretch -> blitSprite(textureatlassprite, x, y, blitOffset, width, height)

			is GuiSpriteScaling.Tile -> guispritescaling.apply {
				val spriteWidth = this.width.toFloat()
				val spriteHeight = this.height.toFloat()
				blitTiledSprite(
					textureatlassprite,
					x,
					y,
					blitOffset,
					width,
					height,
					0f,
					0f,
					spriteWidth,
					spriteHeight,
					spriteWidth,
					spriteHeight
				)
			}

			is GuiSpriteScaling.NineSlice -> blitNineSlicedSprite(
				textureatlassprite,
				guispritescaling,
				x,
				y,
				blitOffset,
				width,
				height
			)

			else -> {}
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
	@JvmStatic
	fun GuiGraphics.blitNineSlicedSprite(
		sprite: TextureAtlasSprite,
		nineSlice: GuiSpriteScaling.NineSlice,
		x: Float,
		y: Float,
		blitOffset: Float,
		width: Float,
		height: Float
	) {
		val `guispritescaling$nineslice$border` = nineSlice.border()
		val i = min(`guispritescaling$nineslice$border`.left().toFloat(), width / 2)
		val j = min(`guispritescaling$nineslice$border`.right().toFloat(), width / 2)
		val k = min(`guispritescaling$nineslice$border`.top().toFloat(), height / 2)
		val l = min(`guispritescaling$nineslice$border`.bottom().toFloat(), height / 2)
		if (width == nineSlice.width().toFloat() && height == nineSlice.height().toFloat()) {
			blitSprite(
				sprite,
				nineSlice.width().toFloat(),
				nineSlice.height().toFloat(),
				0f,
				0f,
				x,
				y,
				blitOffset,
				width,
				height
			)
			return
		}

		// 处理高度相等的情况
		if (height == nineSlice.height().toFloat()) {
			blitSprite(
				sprite,
				nineSlice.width().toFloat(),
				nineSlice.height().toFloat(),
				0f,
				0f,
				x,
				y,
				blitOffset,
				i,
				height
			)
			blitTiledSprite(
				sprite,
				x + i,
				y,
				blitOffset,
				width - j - i,
				height,
				i,
				0f,
				nineSlice.width() - j - i,
				nineSlice.height().toFloat(),
				nineSlice.width().toFloat(),
				nineSlice.height().toFloat()
			)
			blitSprite(
				sprite,
				nineSlice.width().toFloat(),
				nineSlice.height().toFloat(),
				nineSlice.width() - j,
				0f,
				x + width - j,
				y,
				blitOffset,
				j,
				height
			)
			return
		}

		// 处理宽度相等的情况
		if (width == nineSlice.width().toFloat()) {
			blitSprite(
				sprite,
				nineSlice.width().toFloat(),
				nineSlice.height().toFloat(),
				0f,
				0f,
				x,
				y,
				blitOffset,
				width,
				k
			)
			blitTiledSprite(
				sprite,
				x,
				y + k,
				blitOffset,
				width,
				height - l - k,
				0f,
				k,
				nineSlice.width().toFloat(),
				nineSlice.height() - l - k,
				nineSlice.width().toFloat(),
				nineSlice.height().toFloat()
			)
			blitSprite(
				sprite,
				nineSlice.width().toFloat(),
				nineSlice.height().toFloat(),
				0f,
				nineSlice.height() - l,
				x,
				y + height - l,
				blitOffset,
				width,
				l
			)
			return
		}

		// 处理一般情况，分别绘制九个区域
		blitSprite(
			sprite,
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat(),
			0f,
			0f,
			x,
			y,
			blitOffset,
			i,
			k
		)
		blitTiledSprite(
			sprite,
			x + i,
			y,
			blitOffset,
			width - j - i,
			k,
			i,
			0f,
			nineSlice.width() - j - i,
			k,
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat()
		)
		blitSprite(
			sprite,
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat(),
			nineSlice.width() - j,
			0f,
			x + width - j,
			y,
			blitOffset,
			j,
			k
		)
		blitSprite(
			sprite,
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat(),
			0f,
			nineSlice.height() - l,
			x,
			y + height - l,
			blitOffset,
			i,
			l
		)
		blitTiledSprite(
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
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat()
		)
		blitSprite(
			sprite,
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat(),
			nineSlice.width() - j,
			nineSlice.height() - l,
			x + width - j,
			y + height - l,
			blitOffset,
			j,
			l
		)
		blitTiledSprite(
			sprite,
			x,
			y + k,
			blitOffset,
			i,
			height - l - k,
			0f,
			k,
			i,
			nineSlice.height() - l - k,
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat()
		)
		blitTiledSprite(
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
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat()
		)
		blitTiledSprite(
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
			nineSlice.width().toFloat(),
			nineSlice.height().toFloat()
		)
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
	@JvmStatic
	fun GuiGraphics.blitTiledSprite(
		sprite: TextureAtlasSprite,
		x: Float,
		y: Float,
		blitOffset: Float,
		width: Float,
		height: Float,
		uPosition: Float,
		vPosition: Float,
		spriteWidth: Float,
		spriteHeight: Float,
		nineSliceWidth: Float,
		nineSliceHeight: Float
	) {
		if (width <= 0 || height <= 0) {
			return
		}

		require(!(spriteWidth <= 0 || spriteHeight <= 0)) { "Tiled sprite texture size must be positive, got " + spriteWidth + "x" + spriteHeight }

		var i = 0f
		while (i < width) {
			val j = min(spriteWidth, width - i)

			var k = 0f
			while (k < height) {
				val l = min(spriteHeight, height - k)
				blitSprite(
					sprite,
					nineSliceWidth,
					nineSliceHeight,
					uPosition,
					vPosition,
					x + i,
					y + k,
					blitOffset,
					j,
					l
				)
				k += spriteHeight
			}
			i += spriteWidth
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
	@JvmStatic
	fun GuiGraphics.blitSprite(
		sprite: ResourceLocation,
		textureWidth: Float,
		textureHeight: Float,
		uPosition: Float,
		vPosition: Float,
		x: Float,
		y: Float,
		uWidth: Float,
		vHeight: Float
	) {
		blitSprite(sprite, textureWidth, textureHeight, uPosition, vPosition, x, y, 0f, uWidth, vHeight)
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
	@JvmStatic
	fun GuiGraphics.blitSprite(
		sprite: ResourceLocation,
		textureWidth: Float,
		textureHeight: Float,
		uPosition: Float,
		vPosition: Float,
		x: Float,
		y: Float,
		blitOffset: Float,
		uWidth: Float,
		vHeight: Float
	) {
		val textureatlassprite = sprites.getSprite(sprite)
		val guispritescaling = sprites.getSpriteScaling(textureatlassprite)
		if (guispritescaling is GuiSpriteScaling.Stretch) {
			blitSprite(
				textureatlassprite,
				textureWidth,
				textureHeight,
				uPosition,
				vPosition,
				x,
				y,
				blitOffset,
				uWidth,
				vHeight
			)
			return
		}

		blitSprite(textureatlassprite, x, y, blitOffset, uWidth, vHeight)
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
	@JvmStatic
	fun GuiGraphics.blitSprite(
		sprite: TextureAtlasSprite,
		textureWidth: Float,
		textureHeight: Float,
		uPosition: Float,
		vPosition: Float,
		x: Float,
		y: Float,
		blitOffset: Float,
		uWidth: Float,
		vHeight: Float
	) {
		if (uWidth == 0f || vHeight == 0f) {
			return
		}
		innerBlit(
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
		)
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
	@JvmStatic
	private fun GuiGraphics.innerBlit(
		atlasLocation: ResourceLocation,
		x1: Float,
		x2: Float,
		y1: Float,
		y2: Float,
		blitOffset: Float,
		minU: Float,
		maxU: Float,
		minV: Float,
		maxV: Float
	) {
		RenderSystem.setShaderTexture(0, atlasLocation)
		RenderSystem.setShader { GameRenderer.getPositionTexShader() }
		val matrix4f = pose().last().pose()
		val bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
		bufferbuilder.addVertex(matrix4f, x1, y1, blitOffset).setUv(minU, minV)
		bufferbuilder.addVertex(matrix4f, x1, y2, blitOffset).setUv(minU, maxV)
			.addVertex(matrix4f, x2, y2, blitOffset).setUv(maxU, maxV)
			.addVertex(matrix4f, x2, y1, blitOffset).setUv(maxU, minV)
		BufferUploader.drawWithShader(bufferbuilder.buildOrThrow())
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
	@JvmStatic
	fun GuiGraphics.blitSprite(

		sprite: TextureAtlasSprite,
		x: Float,
		y: Float,
		blitOffset: Float,
		width: Float,
		height: Float
	) {
		if (width == 0f || height == 0f) {
			return
		}
		innerBlit(
			sprite.atlasLocation(),
			x,
			x + width,
			y,
			y + height,
			blitOffset,
			sprite.u0,
			sprite.u1,
			sprite.v0,
			sprite.v1
		)
	}

	@JvmStatic
	fun GuiGraphics.fill(minX: Float, minY: Float, maxX: Float, maxY: Float, color: Int) {
		fill(minX, minY, maxX, maxY, 0f, color)
	}

	@JvmStatic
	fun GuiGraphics.fill(minX: Float, minY: Float, maxX: Float, maxY: Float, z: Float, color: Int) {
		fill(RenderType.gui(), minX, minY, maxX, maxY, z, color)
	}

	@JvmStatic
	fun GuiGraphics.fill(
		renderType: RenderType,
		minX: Float,
		minY: Float,
		maxX: Float,
		maxY: Float,
		z: Float,
		color: Int
	) {
		var minX = minX
		var minY = minY
		var maxX = maxX
		var maxY = maxY
		val matrix4f = pose().last().pose()
		if (minX < maxX) {
			val i = minX
			minX = maxX
			maxX = i
		}

		if (minY < maxY) {
			val j = minY
			minY = maxY
			maxY = j
		}

		val vertexconsumer = bufferSource().getBuffer(renderType)
		vertexconsumer.addVertex(matrix4f, minX, minY, z).setColor(color)
		vertexconsumer.addVertex(matrix4f, minX, maxY, z).setColor(color)
		vertexconsumer.addVertex(matrix4f, maxX, maxY, z).setColor(color)
		vertexconsumer.addVertex(matrix4f, maxX, minY, z).setColor(color)
		flushIfUnmanaged()
	}

	@JvmStatic
	fun GuiGraphics.fill(
		renderType: RenderType,
		minX: Float,
		minY: Float,
		maxX: Float,
		maxY: Float,
		color: Int
	) {
		fill(renderType, minX, minY, maxX, maxY, 0f, color)
	}

	@JvmStatic
	fun GuiGraphics.fillGradient(
		x1: Float,
		y1: Float,
		x2: Float,
		y2: Float,
		colorFrom: Int,
		colorTo: Int
	) {
		fillGradient(x1, y1, x2, y2, 0f, colorFrom, colorTo)
	}

	@JvmStatic
	fun GuiGraphics.fillGradient(
		x1: Float,
		y1: Float,
		x2: Float,
		y2: Float,
		z: Float,
		colorFrom: Int,
		colorTo: Int
	) {
		fillGradient(RenderType.gui(), x1, y1, x2, y2, colorFrom, colorTo, z)
	}

	@JvmStatic
	fun GuiGraphics.fillGradient(
		renderType: RenderType,
		x1: Float,
		y1: Float,
		x2: Float,
		y2: Float,
		colorFrom: Int,
		colorTo: Int,
		z: Float
	) {
		val vertexconsumer = bufferSource().getBuffer(renderType)
		fillGradient(vertexconsumer, x1, y1, x2, y2, z, colorFrom, colorTo)
		flushIfUnmanaged()
	}

	@JvmStatic
	fun GuiGraphics.fillGradient(
		consumer: VertexConsumer,
		x1: Float,
		y1: Float,
		x2: Float,
		y2: Float,
		z: Float,
		colorFrom: Int,
		colorTo: Int
	) {
		val matrix4f = pose().last().pose()
		consumer.addVertex(matrix4f, x1, y1, z).setColor(colorFrom)
		consumer.addVertex(matrix4f, x1, y2, z).setColor(colorTo)
		consumer.addVertex(matrix4f, x2, y2, z).setColor(colorTo)
		consumer.addVertex(matrix4f, x2, y1, z).setColor(colorFrom)
	}

	@JvmStatic
	fun GuiGraphics.fillRenderType(
		renderType: RenderType,
		x1: Float,
		y1: Float,
		x2: Float,
		y2: Float,
		z: Float
	) {
		val matrix4f = pose().last().pose()
		val vertexconsumer = bufferSource().getBuffer(renderType)
		vertexconsumer.addVertex(matrix4f, x1, y1, z)
		vertexconsumer.addVertex(matrix4f, x1, y2, z)
		vertexconsumer.addVertex(matrix4f, x2, y2, z)
		vertexconsumer.addVertex(matrix4f, x2, y1, z)
		flushIfUnmanaged()
	}
}
