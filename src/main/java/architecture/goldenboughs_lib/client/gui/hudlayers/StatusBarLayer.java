package architecture.goldenboughs_lib.client.gui.hudlayers;

import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar;
import architecture.goldenboughs_lib.util.TextUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

/**
 * 状态条层
 */
public abstract class StatusBarLayer extends IHudLayer {
	protected final HorizontalStatusBar statusBar;
	protected float currentValue;
	protected float renderedValue;
	protected float previousValue;
	protected float maxValue;

	protected StatusBarLayer(HorizontalStatusBar statusBar) {
		this.statusBar = statusBar;
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		renderStatusBar(guiGraphics, deltaTracker);
		renderValueText(guiGraphics);
	}

	protected void renderStatusBar(GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
		float deltaTime = deltaTracker.getRealtimeDeltaTicks();
		this.statusBar.render(guiGraphics, 0, 0, deltaTime);
	}

	protected void renderValueText(@NotNull GuiGraphics guiGraphics) {
		Font font = this.font;
		Component text = getRenderText();
		guiGraphics.drawString(font, text, this.leftPos - font.width(text), this.topPos + font.lineHeight / 2 - 3, 0xFFFFFF);
	}

	protected @NotNull MutableComponent getRenderText() {
		return Component.empty().append(getValueText()).append("/").append(getMaxValueText());
	}

	protected MutableComponent getValueText() {
		return Component.literal(TextUtil.formatNumber(this.currentValue, 2));
	}

	protected MutableComponent getMaxValueText() {
		return Component.literal(TextUtil.formatNumber(this.maxValue, 2));
	}

	@Override
	public void init(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		super.init(guiGraphics, deltaTracker);
		updateMaxValue();
		updateValue(deltaTracker);
		updateStatusBarAppearance();
	}

	protected void updateMaxValue() {
		float newMaxValue = getMaxValueFromSource();
		if (newMaxValue == this.maxValue) {
			return;
		}

		this.maxValue = newMaxValue;
		this.statusBar.setMaxValue(Math.abs(newMaxValue));
	}

	protected abstract float getMaxValueFromSource();

	protected void updateValue(@NotNull DeltaTracker deltaTracker) {
		float deltaTime = deltaTracker.getRealtimeDeltaTicks();
		float newCurrentValue = getCurrentValueFromSource();

		float oldValue = this.currentValue;
		if (newCurrentValue != oldValue) {
			this.currentValue = newCurrentValue;
			this.statusBar.setLight();
		}

		float calculatedRenderedValue = Mth.lerp(Math.clamp(deltaTime * 1.5f, 0, 1), this.previousValue, newCurrentValue);
		this.renderedValue = calculatedRenderedValue;

		this.statusBar.setOldValue(Math.abs(calculatedRenderedValue));
		if (calculatedRenderedValue < 0 || calculatedRenderedValue > 1) {
			float oldValue2 = Math.abs(oldValue);
			float newValue = Math.abs(newCurrentValue);
			if (oldValue2 != newValue) {
				this.statusBar.setLightWidth(oldValue2, newValue);
			}
		} else if (this.statusBar.getLightWidth() != 0) {
			this.statusBar.setLightWidth(0);
		}

		if (this.previousValue != calculatedRenderedValue) {
			this.previousValue = calculatedRenderedValue;
		}
	}

	protected abstract float getCurrentValueFromSource();

	protected abstract void updateStatusBarAppearance();

	@Override
	public void setLeftPos(final int leftPos) {
		super.setLeftPos(leftPos);
		this.statusBar.setX(leftPos);
	}

	@Override
	public void setTopPos(final int topPos) {
		super.setTopPos(topPos);
		this.statusBar.setY(topPos);
	}

	@Override
	public int getWidth() {
		return this.statusBar.getWidth();
	}

	@Override
	public int getHeight() {
		return this.statusBar.getHeight();
	}
}
