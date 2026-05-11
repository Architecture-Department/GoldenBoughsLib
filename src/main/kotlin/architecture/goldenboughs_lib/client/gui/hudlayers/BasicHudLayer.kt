package architecture.goldenboughs_lib.client.gui.hudlayers

import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.player.LocalPlayer
import java.util.*

/**
 * 基础hud层
 */
abstract class BasicHudLayer : IHudLayer() {
	var screenWidth: Int = 0
		protected set
	var screenHeight: Int = 0
		protected set

	override fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		if (this.minecraft.options.hideGui) {
			return
		}
		init(guiGraphics, deltaTracker)

		renderDrawLayer(guiGraphics, deltaTracker)
	}

	/**
	 * 绘制
	 */
	protected abstract fun renderDrawLayer(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker)

	override fun init(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val newScreenWidth = guiGraphics.guiWidth()
		val newScreenHeight = guiGraphics.guiHeight()

		val newPlayer = Objects.requireNonNull<LocalPlayer>(this.minecraft.player)
		if (this.player !== newPlayer) {
			playerChange(newPlayer)
		}

		val isWidthChange = newScreenWidth != this.screenWidth
		val isHeightChange = newScreenHeight != this.screenHeight
		if (isWidthChange || isHeightChange) {
			sizeChange(newScreenWidth, newScreenHeight)
		}
	}

	override fun playerChange(newPlayer: LocalPlayer) {
		this.player = newPlayer
	}

	protected fun sizeChange(newScreenWidth: Int, newScreenHeight: Int) {
		this.screenWidth = newScreenWidth
		this.screenHeight = newScreenHeight
	}
}
