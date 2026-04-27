package architecture.goldenboughs_lib.client.gui.hudlayers;

import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.util.TextUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NewHealthBarLayer extends StatusBarLayer {
	public static final ResourceLocation LIGHT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/light");

	// 伤害吸收
	public static final ResourceLocation ABSORBING_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/absorbing");
	public static final ResourceLocation ABSORBING_LIGHT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/absorbing_light");

	// 冰冻
	public static final ResourceLocation FROZEN_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/frozen");
	public static final ResourceLocation FROZEN_BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/frozen_bottom");

	// 正常
	public static final ResourceLocation FULL_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/full");
	public static final ResourceLocation FULL_BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/full_bottom");

	// 凋零
	public static final ResourceLocation WITHERED_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/withered");
	public static final ResourceLocation WITHERED_BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/withered_bottom");

	// 中毒
	public static final ResourceLocation POISIONED_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/poisioned");
	public static final ResourceLocation POISIONED_BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/health/poisioned_bottom");

	private final ImageWidget absorbingStatusBar;
	private final ImageWidget absorbingLightStatusBar;
	private Gui.HeartType heartType = Gui.HeartType.NORMAL;
	private float absorbingHp;
	private float lightTick;

	public NewHealthBarLayer() {
		super(new HorizontalStatusBar(90, 9,
			new HorizontalStatusBar.TextureLayer(0, 0, 90, 9, FULL_BOTTOM_TEXTURE),
			new HorizontalStatusBar.TextureLayer(11, 1, 78, 7, FULL_TEXTURE),
			new HorizontalStatusBar.TextureLayer(0, 0, 90, 9, LIGHT_TEXTURE)));
		this.absorbingStatusBar = ImageWidget.sprite(80, 9, ABSORBING_TEXTURE);
		this.absorbingLightStatusBar = ImageWidget.sprite(80, 9, ABSORBING_LIGHT_TEXTURE);
	}

	protected void renderStatusBar(GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
		super.renderStatusBar(guiGraphics, deltaTracker);
		float deltaTime = deltaTracker.getRealtimeDeltaTicks();

		if (this.absorbingHp <= 0) {
			return;
		}

		if (this.lightTick > 0) {
			this.absorbingLightStatusBar.render(guiGraphics, 0, 0, deltaTime);
			this.lightTick -= deltaTime;
			return;
		}

		this.absorbingStatusBar.render(guiGraphics, 0, 0, deltaTime);
	}

	@Override
	protected MutableComponent getValueText() {
		MutableComponent value = super.getValueText();
		float absorbingHp = this.absorbingHp;
		if (absorbingHp > 0) {
			String text = "+%s".formatted(TextUtil.formatNumberPlaces(absorbingHp, 2));
			value.append(Component.literal(text).withColor(0xffe400));
		}

		return value;
	}

	@Override
	protected float getCurrentValueFromSource() {
		return this.player.getHealth();
	}

	@Override
	protected float getMaxValueFromSource() {
		return this.player.getMaxHealth();
	}

	@Override
	protected void updateValue(@NotNull DeltaTracker deltaTracker) {
		super.updateValue(deltaTracker);

		float absorptionAmount = this.player.getAbsorptionAmount();
		if (absorptionAmount == this.absorbingHp) {
			return;
		}

		this.absorbingHp = absorptionAmount;
		this.lightTick = this.absorbingHp > 0 ? 2 : 0;
	}

	@Override
	protected void updateStatusBarAppearance() {
		Gui.HeartType heartType = Gui.HeartType.forPlayer(this.player);
		if (heartType == this.heartType) {
			return;
		}

		this.heartType = heartType;
		HorizontalStatusBar statusBar = this.statusBar;
		HorizontalStatusBar.TextureLayer bottomLayer = statusBar.getBottomLayer();
		HorizontalStatusBar.TextureLayer internalLayer = statusBar.getInternalLayer();
		switch (heartType) {
			case POISIONED -> {
				bottomLayer.setTexture(POISIONED_BOTTOM_TEXTURE);
				internalLayer.setTexture(POISIONED_TEXTURE);
			}
			case WITHERED -> {
				bottomLayer.setTexture(WITHERED_BOTTOM_TEXTURE);
				internalLayer.setTexture(WITHERED_TEXTURE);
			}
			case FROZEN -> {
				bottomLayer.setTexture(FROZEN_BOTTOM_TEXTURE);
				internalLayer.setTexture(FROZEN_TEXTURE);
			}
			default -> {
				bottomLayer.setTexture(FULL_BOTTOM_TEXTURE);
				internalLayer.setTexture(FULL_TEXTURE);
			}
		}
	}

	@Override
	public void setLeftPos(final int leftPos) {
		super.setLeftPos(leftPos);
		int leftPosition = leftPos + 3;
		this.statusBar.setX(leftPosition);
		this.absorbingStatusBar.setX(leftPosition + 10);
		this.absorbingLightStatusBar.setX(leftPosition + 10);
	}

	@Override
	public void setTopPos(final int topPos) {
		super.setTopPos(topPos);
		this.absorbingStatusBar.setY(topPos);
		this.absorbingLightStatusBar.setY(topPos);
	}
}
