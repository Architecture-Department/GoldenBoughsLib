package architecture.goldenboughs_lib.client.gui.hudlayers;

import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.util.RationalityUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RationalityBarLayer extends StatusBarLayer {
	// 默认
	public static final ResourceLocation DEFAULT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality");
	public static final ResourceLocation DEFAULT_BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_bottom");
	public static final ResourceLocation DEFAULT_LIGHT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_light");

	// 低理智
	public static final ResourceLocation LOW_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_low");
	public static final ResourceLocation LOW_BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_low_bottom");
	public static final ResourceLocation LOW_LIGHT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_low_light");
	// 动态
	public static final ResourceLocation LOW_DYNAMIC_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_low_dynamic");

	// 高理智
	public static final ResourceLocation TALL_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_tall");
	public static final ResourceLocation TALL_BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_tall_bottom");
	public static final ResourceLocation TALL_LIGHT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/rationality/rationality_tall_light");

	private final ImageWidget lowDynamicStatusBar;

	public RationalityBarLayer() {
		super(new HorizontalStatusBar(93, 11,
			new HorizontalStatusBar.TextureLayer(0, 0, 93, 11, DEFAULT_BOTTOM_TEXTURE),
			new HorizontalStatusBar.TextureLayer(14, 2, 78, 7, DEFAULT_TEXTURE),
			new HorizontalStatusBar.TextureLayer(0, 0, 93, 11, DEFAULT_LIGHT_TEXTURE)));
		this.lowDynamicStatusBar = ImageWidget.sprite(98, 14, LOW_DYNAMIC_TEXTURE);
	}

	@Override
	protected void renderStatusBar(GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
		super.renderStatusBar(guiGraphics, deltaTracker);
		float deltaTime = deltaTracker.getRealtimeDeltaTicks();
		this.lowDynamicStatusBar.render(guiGraphics, 0, 0, deltaTime);
	}

	@Override
	protected float getMaxValueFromSource() {
		return RationalityUtil.getMaxRationalityValue(this.player);
	}

	@Override
	protected float getCurrentValueFromSource() {
		return RationalityUtil.getRationalityValue(this.player);
	}

	@Override
	protected void updateStatusBarAppearance() {
		float rationality = this.renderedValue;
		float maxRationalityValue = this.maxValue;

		ImageWidget dynamicBar = this.lowDynamicStatusBar;
		HorizontalStatusBar statusBar = this.statusBar;
		HorizontalStatusBar.TextureLayer bottomLayer = statusBar.getBottomLayer();
		HorizontalStatusBar.TextureLayer internalLayer = statusBar.getInternalLayer();
		HorizontalStatusBar.TextureLayer lightLayer = statusBar.getLightLayer();

		// 最低理智-动态
		if (rationality <= maxRationalityValue * -0.99f) {
			statusBar.visible = false;
			dynamicBar.visible = true;
			return;
		}

		statusBar.visible = true;
		dynamicBar.visible = false;

		// 高理智
		if (rationality >= maxRationalityValue * 0.7f) {
			bottomLayer.set(TALL_BOTTOM_TEXTURE, 0, 0, 93, 11);

			internalLayer.setTexture(TALL_TEXTURE);
			lightLayer.setTexture(TALL_LIGHT_TEXTURE);
			return;
		}

		// 默认理智
		if (rationality >= -0.99f) {
			bottomLayer.set(DEFAULT_BOTTOM_TEXTURE, 0, 0, 93, 11);

			internalLayer.setTexture(DEFAULT_TEXTURE);
			lightLayer.setTexture(DEFAULT_LIGHT_TEXTURE);
			return;
		}

		// 低理智
		bottomLayer.set(LOW_BOTTOM_TEXTURE, -1, -2, 94, 14);

		internalLayer.setTexture(LOW_TEXTURE);
		lightLayer.setTexture(LOW_LIGHT_TEXTURE);
	}

	@Override
	public void setLeftPos(final int leftPos) {
		super.setLeftPos(leftPos);
		this.lowDynamicStatusBar.setX(leftPos - 3);
	}

	@Override
	public void setTopPos(final int topPos) {
		super.setTopPos(topPos);
		this.lowDynamicStatusBar.setY(topPos - 1);
	}
}
