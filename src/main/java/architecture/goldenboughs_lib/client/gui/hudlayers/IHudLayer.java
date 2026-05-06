package architecture.goldenboughs_lib.client.gui.hudlayers;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;

public abstract class IHudLayer implements LayeredDraw.Layer {
	protected int leftPos;
	protected int topPos;
	protected Minecraft minecraft;
	protected LocalPlayer player;
	protected Font font;

	public IHudLayer() {
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
	}

	public void init(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

	}

	public void playerChange(LocalPlayer newPlayer) {
		this.player = newPlayer;
	}

	public final int getX() {
		return getTopPos();
	}

	public void setX(int leftPos) {
		this.leftPos = leftPos;
	}

	public final int getTopPos() {
		return getLeftPos();
	}

	public void setTopPos(int topPos) {
		this.topPos = topPos;
	}

	public final int getLeftPos() {
		return leftPos;
	}

	public void setLeftPos(int leftPos) {
		this.leftPos = leftPos;
	}

	public final int getY() {
		return topPos;
	}

	public void setY(int topPos) {
		this.topPos = topPos;
	}

	public int getWidth() {
		return -1;
	}

	public int getHeight() {
		return -1;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
}
