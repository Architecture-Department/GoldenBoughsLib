package architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter;

import architecture.goldenboughs_lib.client.gui.hudlayers.BasicHudLayer;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.util.RationalityUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RationalityScreenFilterLayer extends BasicHudLayer {
	public static final ResourceLocation RATIONALITY = GoldenBoughsLib.modRl("textures/gui/filter/rationality.png");
	public static final ResourceLocation RATIONALITY1 = GoldenBoughsLib.modRl("textures/gui/filter/rationality1.png");
	public static final ResourceLocation RATIONALITY2 = GoldenBoughsLib.modRl("textures/gui/filter/rationality2.png");
	public static final RationalityScreenFilterLayer INSTANCE = new RationalityScreenFilterLayer();

	private final Filter[] rationalityFilters;
	protected float currentValue;
	protected float renderedValue;
	protected float previousValue;
	protected float minValue;

	public RationalityScreenFilterLayer() {
		this.rationalityFilters = new Filter[]{
			new Filter(RATIONALITY),
			new Filter(RATIONALITY1),
			new Filter(RATIONALITY2)
		};
	}

	@Override
	public int getWidth() {
		return getScreenWidth();
	}

	@Override
	public int getHeight() {
		return getScreenHeight();
	}

	@Override
	protected void renderDrawLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		float partialTick = Math.clamp(deltaTracker.getRealtimeDeltaTicks(), 0, 1);
		PoseStack pose = guiGraphics.pose();
		pose.pushPose();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		for (Filter filter : this.rationalityFilters) {
			filter.render(guiGraphics, 0, 0, partialTick);
		}
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		guiGraphics.setColor(1, 1, 1, 1);
		pose.popPose();
	}

	@Override
	public void init(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.init(guiGraphics, deltaTracker);
		float maxValue = RationalityUtil.getMaxValue(this.getPlayer());
		float minValue = -maxValue - maxValue / 3;
		if (minValue != this.minValue) {
			this.minValue = minValue;
		}

		float deltaTime = deltaTracker.getRealtimeDeltaTicks();

		float newCurrentValue = RationalityUtil.getValue(this.getPlayer()) - maxValue / 3;
		if (newCurrentValue != this.currentValue) {
			this.currentValue = newCurrentValue;
		}
		float calculatedRenderedValue = Mth.lerp(Math.clamp(deltaTime * 0.5f, 0, 1), this.previousValue, newCurrentValue);
		this.renderedValue = calculatedRenderedValue;

		rationalityChange(calculatedRenderedValue);

		if (this.previousValue != calculatedRenderedValue) {
			this.previousValue = calculatedRenderedValue;
		}
	}

	private void rationalityChange(float value) {
		if (value >= 0) {
			for (Filter filter : this.rationalityFilters) {
				filter.setAlpha(0);
			}
			return;
		}
		float v = value / this.minValue;

		// 第一个滤镜: 在 0.0-0.5 范围内从 0 升到 1 再降到 0
		this.rationalityFilters[0].setAlpha(Math.max(0, Math.min(1, 1 - Math.abs(v - 0.25f) * 4)));

		// 第二个滤镜: 在 0.333-1.0 范围内从 0 升到 1 再降到 0
		this.rationalityFilters[1].setAlpha(Math.max(0, Math.min(1, 1 - Math.abs(v - 0.666f) * 1.5f)));

		// 第三个滤镜: 在 0.666-1.0 范围内从 0 升到 1
		this.rationalityFilters[2].setAlpha(Math.max(0, Math.min(1, (v - 0.666f) * 3)));
	}

	public static class Filter extends AbstractWidget {
		private final ResourceLocation texture;
		private float alpha;

		public Filter(ResourceLocation texture) {
			super(0, 0, 0, 0, Component.empty());
			this.texture = texture;
		}

		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			PoseStack pose = guiGraphics.pose();
			pose.pushPose();
			guiGraphics.setColor(1, 1, 1, this.alpha);
			guiGraphics.blit(this.texture, 0, 0, 0, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
			guiGraphics.setColor(1, 1, 1, 1);
			pose.popPose();
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

		}

		public float getAlpha() {
			return alpha;
		}

		public void setAlpha(float alpha) {
			if (alpha == this.alpha) {
				return;
			}
			this.alpha = Math.max(0, alpha);
		}
	}
}
