package architecture.goldenboughs_lib.client.gui.widget

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import kotlin.math.max
import kotlin.math.min

/**
 * 一个基于图像的进度条控件抽象类，支持水平和垂直方向的渲染。
 * 
 * 
 * 该类继承自 [Sprite]，用于在 GUI 中显示一个可变长度的进度条，
 * 并提供工具提示功能。子类可以实现不同的方向（水平或垂直）。
 */
@AllOpe
abstract class ImageProgressBar private
/**
 * 构造一个新的 ImageProgressBar 实例。
 *
 * @param x          控件的 X 坐标
 * @param y          控件的 Y 坐标
 * @param width      控件的宽度
 * @param height     控件的高度
 * @param value      当前进度值
 * @param maxValue   最大进度值
 * @param texture    进度条使用的纹理资源位置
 * @param tooltipKey 工具提示翻译键
 */
constructor(
	x: Int,
	y: Int,
	width: Int,
	height: Int,
	var value: Double = 0.0,
	var maxValue: Double = 0.0,
	texture: ResourceLocation,
	var tooltipKey: String = ""
) : ImageWidget.Sprite(x, y, width, height, texture) {

	/**
	 * 渲染控件及其工具提示。
	 *
	 * @param guiGraphics GUI 图形上下文
	 * @param mouseX      鼠标 X 坐标
	 * @param mouseY      鼠标 Y 坐标
	 * @param partialTick 部分 tick 时间（用于动画插值）
	 */
	override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		if (this.maxValue > 0) {
			renderTexture(guiGraphics, mouseX, mouseY, partialTick)
		}
		if (this.maxValue > 0 && isHovered()) {
			renderWidgetTooltip(guiGraphics, mouseX, mouseY, partialTick)
		}
	}

	protected abstract fun renderTexture(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float)

	/**
	 * 渲染控件的工具提示。
	 *
	 * @param guiGraphics GUI 图形上下文
	 * @param mouseX      鼠标 X 坐标
	 * @param mouseY      鼠标 Y 坐标
	 */
	protected fun renderWidgetTooltip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		guiGraphics.renderTooltip(
			Minecraft.getInstance().font,
			this.tooltipComponent, mouseX, mouseY
		)
	}

	val tooltipComponent: Component
		/**
		 * 获取工具提示组件。
		 *
		 * @return 工具提示组件
		 */
		get() = Component.translatable(this.tooltipKey, this.renderValue, this.maxValue)

	val renderValue: Double
		/**
		 * 获取要渲染的进度值，确保不小于 0。
		 *
		 * @return 渲染用的进度值
		 */
		get() = min(max(0.0, this.value), this.maxValue)

	val texture: ResourceLocation
		/**
		 * 获取纹理资源位置。
		 *
		 * @return 纹理资源位置
		 */
		get() = this.sprite

	/**
	 * 水平方向的进度条实现。
	 *
	 *
	 * 该类重写了部分方法以适应水平方向的绘制逻辑。
	 */
	// 注解生成对应构造函数
	class Horizontal
	@JvmOverloads
	/**
	 * 构造一个新的水平进度条实例。
	 *
	 * @param x          控件的 X 坐标
	 * @param y          控件的 Y 坐标
	 * @param width      控件的宽度
	 * @param height     控件的高度
	 * @param value      当前进度值
	 * @param maxValue   最大进度值
	 * @param texture    纹理资源位置
	 * @param tooltipKey 工具提示翻译键
	 * @param isToLeft   是否向左绘制
	 */
	constructor(
		x: Int = 0,
		y: Int = 0,
		width: Int,
		height: Int,
		value: Double = 0.0,
		maxValue: Double = 0.0,
		texture: ResourceLocation,
		tooltipKey: String = "",
		val isToLeft: Boolean
	) : ImageProgressBar(x, y, width, height, value, maxValue, texture, tooltipKey) {

		override fun renderTexture(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
			val value = ((this.renderValue / this.maxValue) * this.getWidth()).toInt()
			val uWidth = if (isToLeft) value else this.getWidth() - value
			val vHeight = getHeight()
			val xPosition = if (isToLeft) 0 else uWidth
			val yPosition = 0
			val x = if (isToLeft) this.x + xPosition else this.x
			val y = this.y + yPosition

			guiGraphics.blitSprite(
				this.sprite,
				this.getWidth(),
				this.getHeight(),
				if (isToLeft) xPosition else 0,
				0,
				x,
				y,
				uWidth,
				vHeight
			)
		}
	}

	/**
	 * 垂直方向的进度条实现。
	 *
	 *
	 * 该类重写了部分方法以适应垂直方向的绘制逻辑。
	 */
	class Vertical
	@JvmOverloads
	/**
	 * 构造一个新的垂直进度条实例。
	 *
	 * @param x          控件的 X 坐标
	 * @param y          控件的 Y 坐标
	 * @param width      控件的宽度
	 * @param height     控件的高度
	 * @param value      当前进度值
	 * @param maxValue   最大进度值
	 * @param texture    纹理资源位置
	 * @param tooltipKey 工具提示翻译键
	 * @param isToTop    是否向上绘制
	 */
	constructor(
		x: Int = 0,
		y: Int = 0,
		width: Int,
		height: Int,
		value: Double = 0.0,
		maxValue: Double = 0.0,
		texture: ResourceLocation,
		tooltipKey: String = "",
		val isToTop: Boolean
	) : ImageProgressBar(x, y, width, height, value, maxValue, texture, tooltipKey) {

		override fun renderTexture(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
			val uWidth = getWidth()
			val value = ((this.renderValue / this.maxValue) * this.getHeight()).toInt()
			val vHeight = if (isToTop) value else this.getHeight() - value
			val xPosition = 0
			val yPosition = if (isToTop) getHeight() - vHeight else 0
			val x = this.x + xPosition
			val y = if (isToTop) this.y + yPosition else this.y

			guiGraphics.blitSprite(this.sprite, this.getWidth(), this.getHeight(), 0, yPosition, x, y, uWidth, vHeight)
		}
	}

	companion object {
		/**
		 * 渲染垂直方向的进度条
		 *
		 *
		 * 正向绘制：进度从上往下填充
		 * <br></br>
		 * 反向绘制：进度从下往上填充
		 */
		@JvmStatic
		@JvmOverloads
		fun GuiGraphics.renderVerticalProgressBar(
			texture: ResourceLocation,
			x: Int,
			y: Int,
			uPos: Int,
			vPos: Int,
			width: Int,
			height: Int,
			value: Float,
			maxValue: Float,
			isReverseDirection: Boolean
		) {
			renderProgressBar(texture, x, y, uPos, vPos, width, height, value, maxValue, true, isReverseDirection)
		}

		/**
		 * 当isVertical为true时：
		 * <br></br>
		 * 如果是正向绘制（isReverseDirection为false），进度从上往下填充
		 * <br></br>
		 * 如果是反向绘制（isReverseDirection为true），进度从下往上填充
		 *
		 *
		 * 当isVertical为false时：
		 * <br></br>
		 * 如果是正向绘制（isReverseDirection为false），进度从左往右填充
		 * <br></br>
		 * 如果是反向绘制（isReverseDirection为true），进度从右往左填
		 */
		@JvmStatic
		@JvmOverloads
		fun GuiGraphics.renderProgressBar(
			texture: ResourceLocation,
			x: Int, y: Int,
			uPos: Int, vPos: Int,
			textureWidth: Int, textureHeight: Int,
			value: Float, maxValue: Float,
			isVertical: Boolean, isReverseDirection: Boolean
		) {
			if (value <= 0 || maxValue <= 0) {
				return
			}

			val renderValue = min(max(0f, value), maxValue)

			// 根据方向选择尺寸参数
			// 主要尺寸（垂直时是高度，水平时是宽度）
			val mainDimension = (if (isVertical) textureHeight else textureWidth) - uPos
			// 交叉尺寸（垂直时是宽度，水平时是高度）
			val crossDimension = (if (isVertical) textureWidth else textureHeight) - vPos

			// 计算进度值在主维度上的表现
			val textureValue = (renderValue / maxValue * mainDimension).toInt()
			val textureMainSize = if (isReverseDirection) textureValue else mainDimension - textureValue

			// 计算UV坐标
			val uPos1 = uPos + (if (isVertical) 0 else if (isReverseDirection) 0 else textureMainSize)
			val vPos1 = vPos + (if (isVertical) if (isReverseDirection) mainDimension - textureMainSize else 0 else 0)

			// 计算绘制位置
			val x1 = if (isVertical) x else if (isReverseDirection) x else x + textureMainSize
			val y1 = if (isVertical) if (isReverseDirection) y + (mainDimension - textureMainSize) else y else y + vPos1

			// 计算绘制尺寸
			val uWidth = (if (isVertical) crossDimension else textureMainSize)
			val vHeight = (if (isVertical) textureMainSize else crossDimension)

			blitSprite(
				texture,
				textureWidth, textureHeight,
				uPos1, vPos1,
				x1, y1,
				uWidth, vHeight
			)
		}

		/**
		 * 渲染垂直方向的进度条
		 *
		 *
		 * 正向绘制：进度从上往下填充
		 * <br></br>
		 * 反向绘制：进度从下往上填充
		 */
		@JvmStatic
		@JvmOverloads
		fun GuiGraphics.renderVerticalProgressBar(
			texture: ResourceLocation,
			x: Int, y: Int,
			width: Int, height: Int,
			value: Float, maxValue: Float,
			isReverseDirection: Boolean
		) {
			renderProgressBar(texture, x, y, width, height, value, maxValue, true, isReverseDirection)
		}

		/**
		 * 当isVertical为true时：
		 * <br></br>
		 * 如果是正向绘制（isReverseDirection为false），进度从上往下填充
		 * <br></br>
		 * 如果是反向绘制（isReverseDirection为true），进度从下往上填充
		 *
		 *
		 * 当isVertical为false时：
		 * <br></br>
		 * 如果是正向绘制（isReverseDirection为false），进度从左往右填充
		 * <br></br>
		 * 如果是反向绘制（isReverseDirection为true），进度从右往左填
		 */
		@JvmStatic
		@JvmOverloads
		fun GuiGraphics.renderProgressBar(
			texture: ResourceLocation,
			x: Int, y: Int,
			width: Int, height: Int,
			value: Float, maxValue: Float,
			isVertical: Boolean, isReverseDirection: Boolean
		) {
			renderProgressBar(
				texture,
				x,
				y,
				0,
				0,
				width,
				height,
				value,
				maxValue,
				isVertical,
				isReverseDirection
			)
		}

		/**
		 * 渲染水平方向的进度条
		 *
		 *
		 * 正向绘制：进度从左往右填充
		 * <br></br>
		 * 反向绘制：进度从右往左填充
		 */
		@JvmStatic
		@JvmOverloads
		fun GuiGraphics.renderHorizontalProgressBar(
			texture: ResourceLocation,
			x: Int, y: Int,
			width: Int, height: Int,
			value: Float, maxValue: Float,
			isReverseDirection: Boolean
		) {
			renderProgressBar(texture, x, y, width, height, value, maxValue, false, isReverseDirection)
		}

		/**
		 * 渲染水平方向的进度条
		 *
		 *
		 * 正向绘制：进度从左往右填充
		 * <br></br>
		 * 反向绘制：进度从右往左填充
		 */
		@JvmStatic
		@JvmOverloads
		fun GuiGraphics.renderHorizontalProgressBar(
			texture: ResourceLocation,
			x: Int, y: Int,
			uPos: Int, vPos: Int,
			width: Int, height: Int,
			value: Float, maxValue: Float,
			isReverseDirection: Boolean
		) {
			renderProgressBar(texture, x, y, uPos, vPos, width, height, value, maxValue, false, isReverseDirection)
		}
	}
}
