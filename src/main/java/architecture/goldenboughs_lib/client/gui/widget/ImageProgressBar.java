package architecture.goldenboughs_lib.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * 一个基于图像的进度条控件抽象类，支持水平和垂直方向的渲染。
 * <p>
 * 该类继承自 {@link Sprite}，用于在 GUI 中显示一个可变长度的进度条，
 * 并提供工具提示功能。子类可以实现不同的方向（水平或垂直）。
 */
public abstract class ImageProgressBar extends ImageWidget.Sprite {
	private final String tooltipKey;
	private double value;
	private double maxValue;

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
	private ImageProgressBar(int x, int y, int width, int height, double value, double maxValue, ResourceLocation texture, String tooltipKey) {
		super(x, y, width, height, texture);
		this.setValue(value);
		this.setMaxValue(maxValue);
		this.tooltipKey = tooltipKey;
	}

	/**
	 * 渲染垂直方向的进度条
	 * <p>
	 * 正向绘制：进度从上往下填充
	 * <br>
	 * 反向绘制：进度从下往上填充
	 */
	public static void renderVerticalProgressBar(@NotNull GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int uPos, int vPos, int width, int height, float value, float maxValue, boolean isReverseDirection) {
		renderProgressBar(guiGraphics, texture, x, y, uPos, vPos, width, height, value, maxValue, true, isReverseDirection);
	}

	/**
	 * 当isVertical为true时：
	 * <br>
	 * 如果是正向绘制（isReverseDirection为false），进度从上往下填充
	 * <br>
	 * 如果是反向绘制（isReverseDirection为true），进度从下往上填充
	 * <p>
	 * 当isVertical为false时：
	 * <br>
	 * 如果是正向绘制（isReverseDirection为false），进度从左往右填充
	 * <br>
	 * 如果是反向绘制（isReverseDirection为true），进度从右往左填
	 */
	public static void renderProgressBar(@NotNull GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int uPos, int vPos, int textureWidth, int textureHeight, float value, float maxValue, boolean isVertical, boolean isReverseDirection) {
		if (value <= 0 || maxValue <= 0) {
			return;
		}

		float renderValue = Math.min(Math.max(0, value), maxValue);

		// 根据方向选择尺寸参数
		// 主要尺寸（垂直时是高度，水平时是宽度）
		int mainDimension = (isVertical ? textureHeight : textureWidth) - uPos;
		// 交叉尺寸（垂直时是宽度，水平时是高度）
		int crossDimension = (isVertical ? textureWidth : textureHeight) - vPos;

		// 计算进度值在主维度上的表现
		int textureValue = (int) (renderValue / maxValue * mainDimension);
		int textureMainSize = isReverseDirection ? textureValue : mainDimension - textureValue;

		// 计算UV坐标
		int uPos1 = uPos + (isVertical ? 0 : isReverseDirection ? 0 : textureMainSize);
		int vPos1 = vPos + (isVertical ? isReverseDirection ? mainDimension - textureMainSize : 0 : 0);

		// 计算绘制位置
		int x1 = isVertical ? x : isReverseDirection ? x : x + textureMainSize;
		int y1 = isVertical ? isReverseDirection ? y + (mainDimension - textureMainSize) : y : y + vPos1;

		// 计算绘制尺寸
		int uWidth = (isVertical ? crossDimension : textureMainSize);
		int vHeight = (isVertical ? textureMainSize : crossDimension);

		guiGraphics.blitSprite(
			texture,
			textureWidth, textureHeight,
			uPos1, vPos1,
			x1, y1,
			uWidth, vHeight);
	}

	/**
	 * 渲染垂直方向的进度条
	 * <p>
	 * 正向绘制：进度从上往下填充
	 * <br>
	 * 反向绘制：进度从下往上填充
	 */
	public static void renderVerticalProgressBar(@NotNull GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, float value, float maxValue, boolean isReverseDirection) {
		renderProgressBar(guiGraphics, texture, x, y, width, height, value, maxValue, true, isReverseDirection);
	}

	/**
	 * 当isVertical为true时：
	 * <br>
	 * 如果是正向绘制（isReverseDirection为false），进度从上往下填充
	 * <br>
	 * 如果是反向绘制（isReverseDirection为true），进度从下往上填充
	 * <p>
	 * 当isVertical为false时：
	 * <br>
	 * 如果是正向绘制（isReverseDirection为false），进度从左往右填充
	 * <br>
	 * 如果是反向绘制（isReverseDirection为true），进度从右往左填
	 */
	public static void renderProgressBar(@NotNull GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, float value, float maxValue, boolean isVertical, boolean isReverseDirection) {
		renderProgressBar(guiGraphics, texture, x, y, 0, 0, width, height, value, maxValue, isVertical, isReverseDirection);
	}

	/**
	 * 渲染水平方向的进度条
	 * <p>
	 * 正向绘制：进度从左往右填充
	 * <br>
	 * 反向绘制：进度从右往左填充
	 */
	public static void renderHorizontalProgressBar(@NotNull GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, float value, float maxValue, boolean isReverseDirection) {
		renderProgressBar(guiGraphics, texture, x, y, width, height, value, maxValue, false, isReverseDirection);
	}

	/**
	 * 渲染水平方向的进度条
	 * <p>
	 * 正向绘制：进度从左往右填充
	 * <br>
	 * 反向绘制：进度从右往左填充
	 */
	public static void renderHorizontalProgressBar(@NotNull GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int uPos, int vPos, int width, int height, float value, float maxValue, boolean isReverseDirection) {
		renderProgressBar(guiGraphics, texture, x, y, uPos, vPos, width, height, value, maxValue, false, isReverseDirection);
	}

	/**
	 * 同时设置当前值和最大值。
	 *
	 * @param energy  当前进度值
	 * @param maxTick 最大进度值
	 */
	public void setValue(double energy, double maxTick) {
		this.setValue(energy);
		this.setMaxValue(maxTick);
	}

	/**
	 * 渲染控件及其工具提示。
	 *
	 * @param guiGraphics GUI 图形上下文
	 * @param mouseX      鼠标 X 坐标
	 * @param mouseY      鼠标 Y 坐标
	 * @param partialTick 部分 tick 时间（用于动画插值）
	 */
	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		if (this.getMaxValue() > 0) {
			renderTexture(guiGraphics, mouseX, mouseY, partialTick);
		}
		if (getMaxValue() > 0 && isHovered()) {
			renderWidgetTooltip(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	/**
	 * 获取最大进度值。
	 *
	 * @return 最大进度值
	 */
	public double getMaxValue() {
		return maxValue;
	}

	/**
	 * 设置最大进度值。
	 *
	 * @param maxValue 新的最大进度值
	 */
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	protected abstract void renderTexture(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

	/**
	 * 渲染控件的工具提示。
	 *
	 * @param guiGraphics GUI 图形上下文
	 * @param mouseX      鼠标 X 坐标
	 * @param mouseY      鼠标 Y 坐标
	 */
	protected void renderWidgetTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.renderTooltip(Minecraft.getInstance().font, getTooltipComponent(), mouseX, mouseY);
	}

	/**
	 * 获取工具提示组件。
	 *
	 * @return 工具提示组件
	 */
	public @NotNull Component getTooltipComponent() {
		return Component.translatable(getTooltipKey(), getRenderValue(), getMaxValue());
	}

	public String getTooltipKey() {
		return tooltipKey;
	}

	/**
	 * 获取要渲染的进度值，确保不小于 0。
	 *
	 * @return 渲染用的进度值
	 */
	public double getRenderValue() {
		return Math.min(Math.max(0, this.getValue()), this.getMaxValue());
	}

	/**
	 * 获取当前进度值。
	 *
	 * @return 当前进度值
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * 设置当前进度值。
	 *
	 * @param value 新的进度值
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * 获取纹理资源位置。
	 *
	 * @return 纹理资源位置
	 */
	public ResourceLocation getTexture() {
		return this.sprite;
	}

	/**
	 * 水平方向的进度条实现。
	 * <p>
	 * 该类重写了部分方法以适应水平方向的绘制逻辑。
	 */
	public static class Horizontal extends ImageProgressBar {
		public final boolean isToLeft;

		public Horizontal(int width, int height, ResourceLocation texture, boolean isToLeft) {
			this(0, 0, width, height, 0, 0, texture, "", isToLeft);
		}

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
		public Horizontal(int x, int y, int width, int height, double value, double maxValue, ResourceLocation texture, String tooltipKey, boolean isToLeft) {
			super(x, y, width, height, value, maxValue, texture, tooltipKey);
			this.isToLeft = isToLeft;
		}

		@Override
		protected void renderTexture(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			int value = (int) ((getRenderValue() / this.getMaxValue()) * this.getWidth());
			int uWidth = isToLeft ? value : this.getWidth() - value;
			int vHeight = getHeight();
			int xPosition = isToLeft ? 0 : uWidth;
			int yPosition = 0;
			int x = isToLeft ? this.getX() + xPosition : this.getX();
			int y = this.getY() + yPosition;

			guiGraphics.blitSprite(this.sprite, this.getWidth(), this.getHeight(), isToLeft ? xPosition : 0, 0, x, y, uWidth, vHeight);
		}
	}

	/**
	 * 垂直方向的进度条实现。
	 * <p>
	 * 该类重写了部分方法以适应垂直方向的绘制逻辑。
	 */
	public static class Vertical extends ImageProgressBar {
		public final boolean isToTop;

		public Vertical(int width, int height, ResourceLocation texture, boolean isToTop) {
			this(0, 0, width, height, 0, 0, texture, "", isToTop);
		}

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
		public Vertical(int x, int y, int width, int height, double value, double maxValue, ResourceLocation texture, String tooltipKey, boolean isToTop) {
			super(x, y, width, height, value, maxValue, texture, tooltipKey);
			this.isToTop = isToTop;
		}

		@Override
		protected void renderTexture(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			int uWidth = getWidth();
			int value = (int) ((getRenderValue() / this.getMaxValue()) * this.getHeight());
			int vHeight = isToTop ? value : this.getHeight() - value;
			int xPosition = 0;
			int yPosition = isToTop ? getHeight() - vHeight : 0;
			int x = this.getX() + xPosition;
			int y = isToTop ? this.getY() + yPosition : this.getY();

			guiGraphics.blitSprite(this.sprite, this.getWidth(), this.getHeight(), 0, yPosition, x, y, uWidth, vHeight);
		}
	}
}
