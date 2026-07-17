package architecture.goldenboughs_lib.client.gui.hudlayers

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.client.player.LocalPlayer

@AllOpen
abstract class IHudLayer : LayeredDraw.Layer {
	var leftPos: Int = 0
	var topPos: Int = 0

	protected var minecraft: Minecraft = Minecraft.getInstance()

	protected var player: LocalPlayer? = minecraft.player
	var font: Font = this.minecraft.font

	fun init(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
	}

	fun playerChange(newPlayer: LocalPlayer) {
		this.player = newPlayer
	}

	fun getPlayerThrow(): LocalPlayer = player!!

	var x: Int
		get() = topPos
		set(topPos) {
			this.topPos = topPos
		}
	var y: Int
		get() = leftPos
		set(leftPos) {
			this.leftPos = leftPos
		}

	val width: Int
		get() = -1

	val height: Int
		get() = -1
}
