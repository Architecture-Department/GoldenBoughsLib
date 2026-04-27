package architecture.goldenboughs_lib.client.gui.widget;

import architecture.goldenboughs_lib.util.GuiUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class HorizontalStatusBar extends AbstractWidget {
	private final @NotNull TextureLayer bottomLayer;
	private final @NotNull TextureLayer internalLayer;
	private final @NotNull TextureLayer lightLayer;
	private boolean isLight;
	private float lightTick;
	private float lightWidth;
	private float value;
	private float oldValue;
	private float maxValue;

	public HorizontalStatusBar(int width, int height, @NotNull TextureLayer bottomLayer, @NotNull TextureLayer internalLayer, @NotNull TextureLayer lightLayer) {
		super(0, 0, width, height, Component.empty());
		this.bottomLayer = bottomLayer;
		this.internalLayer = internalLayer;
		this.lightLayer = lightLayer;
	}

	public float getValue() {
		return this.value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public void setLightWidth(float oldValue, float newValue) {
		float renderValue = oldValue - newValue;
		int maxWidth = this.internalLayer.width;
		float clamp = Math.clamp(renderValue / this.maxValue * maxWidth, -maxWidth, maxWidth);
		if (clamp != 0) {
			this.lightWidth += clamp;
		}
		if (this.lightWidth < 0) {
			this.lightWidth = 0;
		}
		if (this.value != newValue) {
			this.value = newValue;
		}
	}

	public float getLightWidth() {
		return this.lightWidth;
	}

	public void setLightWidth(float value) {
		this.lightWidth = value;
	}

	public void setLight() {
		this.isLight = true;
		this.lightTick = 2;
	}

	@Override
	protected void renderWidget(final @NotNull GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		int x = getX();
		int y = getY();

		renderLayer(guiGraphics, this.bottomLayer, x, y);
		renderInternal(guiGraphics, partialTick, x, y);
		if (this.isLight) {
			float cyclePosition = this.lightTick;
			if ((cyclePosition > 1.5 && cyclePosition <= 2) || (cyclePosition > 0 && cyclePosition <= 0.5)) {
				renderLayer(guiGraphics, this.lightLayer, x, y);
			}
		}

		if (this.lightTick <= 0) {
			this.lightTick = 0;
			isLight = false;
			return;
		}
		this.lightTick -= partialTick;
	}

	private void renderLayer(final @NotNull GuiGraphics guiGraphics, final TextureLayer layer, int x, int y) {
		GuiUtil.blitSprite(guiGraphics, layer.texture, x + layer.xPos, y + layer.yPos, layer.width, layer.height);
	}

	private void renderInternal(final GuiGraphics guiGraphics, final float partialTick, final int x, final int y) {
		final int maxWidth = this.internalLayer.width;
		float value = getClampValue();
		// 使用更平滑的插值算法
		float maxValue = getMaxValue();

		float internalHeight = this.internalLayer.height;
		float internalUWidth = value / maxValue * maxWidth;

		float posX = x + this.internalLayer.xPos;
		float posY = y + this.internalLayer.yPos;

		if (this.lightWidth > 0) {
			float v = this.value / maxValue * maxWidth;
			float max = posX + maxWidth;
			float minX = Math.clamp(v, 0, max);
			float maxX = Math.clamp(minX + this.lightWidth, 0, max);
			float maxY = posY + internalHeight;
			int color = 0xb2ffffff;
			if (this.isLight) {
				color = 0xffffffff;
			}
			float x1 = Math.clamp(posX + minX, posX, max);
			float x2 = Math.clamp(posX + maxX, posX, max);
			GuiUtil.fill(guiGraphics, x1, posY, x2, maxY, color);
			this.lightWidth = Mth.lerp(Math.clamp(partialTick * 0.2f, 0, 1), this.lightWidth, 0);
		}

		GuiUtil.blitSprite(guiGraphics, this.internalLayer.texture, maxWidth, internalHeight,
			0, 0, posX, posY, internalUWidth, internalHeight);
	}

	public float getClampValue() {
		return Math.clamp(this.getOldValue(), 0, this.getMaxValue());
	}

	public float getOldValue() {
		return this.oldValue;
	}

	public void setOldValue(float value) {
		this.oldValue = value;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	protected void updateWidgetNarration(final @NotNull NarrationElementOutput narrationElementOutput) {
	}

	public @NotNull TextureLayer getBottomLayer() {
		return bottomLayer;
	}

	public @NotNull TextureLayer getInternalLayer() {
		return internalLayer;
	}

	public @NotNull TextureLayer getLightLayer() {
		return lightLayer;
	}

	public static final class TextureLayer {
		private float xPos;
		private float yPos;
		private int width;
		private int height;
		private @NotNull ResourceLocation texture;

		public TextureLayer(float xPos, float yPos, int width, int height, @NotNull ResourceLocation texture) {
			this.xPos = xPos;
			this.yPos = yPos;
			this.width = width;
			this.height = height;
			this.texture = texture;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public @NotNull ResourceLocation getTexture() {
			return texture;
		}

		public void setTexture(@NotNull ResourceLocation texture) {
			this.texture = texture;
		}

		public void setPos(float xPos, float yPos) {
			this.xPos = xPos;
			this.yPos = yPos;
		}

		public void setSize(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public void setPosAndSize(float xPos, float yPos, int width, int height) {
			this.xPos = xPos;
			this.yPos = yPos;
			this.width = width;
			this.height = height;
		}

		public void set(ResourceLocation texture, float xPos, float yPos, int width, int height) {
			this.texture = texture;
			this.xPos = xPos;
			this.yPos = yPos;
			this.width = width;
			this.height = height;
		}

		public float getXPos() {
			return xPos;
		}

		public void setXPos(float xPos) {
			this.xPos = xPos;
		}

		public float getYPos() {
			return yPos;
		}

		public void setYPos(float yPos) {
			this.yPos = yPos;
		}
	}
}
