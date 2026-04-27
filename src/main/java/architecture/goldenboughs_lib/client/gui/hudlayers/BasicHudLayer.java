package architecture.goldenboughs_lib.client.gui.hudlayers;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

import java.util.Objects;

/**
 * 基础hud层
 */
public abstract class BasicHudLayer extends IHudLayer {
	protected final Minecraft minecraft;
	protected final Font font;
	protected LocalPlayer player;
	protected int screenWidth;
	protected int screenHeight;

	public BasicHudLayer() {
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		if (this.minecraft.options.hideGui) {
			return;
		}
		init(guiGraphics, deltaTracker);

		renderDrawLayer(guiGraphics, deltaTracker);
	}

	/**
	 * 绘制
	 */
	protected abstract void renderDrawLayer(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker);

	public void init(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		int newScreenWidth = guiGraphics.guiWidth();
		int newScreenHeight = guiGraphics.guiHeight();

		LocalPlayer newPlayer = Objects.requireNonNull(this.minecraft.player);
		if (this.player != newPlayer) {
			playerChange(newPlayer);
		}

		boolean isWidthChange = newScreenWidth != this.screenWidth;
		boolean isHeightChange = newScreenHeight != this.screenHeight;
		if (isWidthChange || isHeightChange) {
			sizeChange(newScreenWidth, newScreenHeight);
		}
	}

	public void playerChange(final LocalPlayer newPlayer) {
		this.player = newPlayer;
	}

	protected void sizeChange(final int newScreenWidth, final int newScreenHeight) {
		this.screenWidth = newScreenWidth;
		this.screenHeight = newScreenHeight;
	}

	public Minecraft getMinecraft() {
		return minecraft;
	}

	public LocalPlayer getPlayer() {
		return player;
	}

	@Override
	public Font getFont() {
		return font;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}
