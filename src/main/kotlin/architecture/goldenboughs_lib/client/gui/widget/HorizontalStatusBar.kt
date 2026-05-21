package architecture.goldenboughs_lib.client.gui.widget

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.util.client.GuiUtil.blitSprite
import architecture.goldenboughs_lib.util.client.GuiUtil.fill
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

@AllOpe
class HorizontalStatusBar(
	width: Int,
	height: Int,
	val bottomLayer: TextureLayer,
	val internalLayer: TextureLayer,
	val lightLayer: TextureLayer
) : AbstractWidget(0, 0, width, height, Component.empty()) {
	private var isLight = false
	private var lightTick = 0f
	var lightWidth: Float = 0f
	var value: Float = 0f
	var oldValue: Float = 0f
	var maxValue: Float = 0f

	fun setLightWidth(oldValue: Float, newValue: Float) {
		val renderValue = oldValue - newValue
		val maxWidth = this.internalLayer.width
		val clamp = Math.clamp(renderValue / this.maxValue * maxWidth, -maxWidth.toFloat(), maxWidth.toFloat())
		if (clamp != 0f) {
			this.lightWidth += clamp
		}
		if (this.lightWidth < 0) {
			this.lightWidth = 0f
		}
		if (this.value != newValue) {
			this.value = newValue
		}
	}

	fun setLight() {
		this.isLight = true
		this.lightTick = 2f
	}

	override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		val x = getX()
		val y = getY()

		renderLayer(guiGraphics, this.bottomLayer, x, y)
		renderInternal(guiGraphics, partialTick, x, y)
		if (this.isLight) {
			val cyclePosition = this.lightTick
			if ((cyclePosition > 1.5 && cyclePosition <= 2) || (cyclePosition > 0 && cyclePosition <= 0.5)) {
				renderLayer(guiGraphics, this.lightLayer, x, y)
			}
		}

		if (this.lightTick <= 0) {
			this.lightTick = 0f
			isLight = false
			return
		}
		this.lightTick -= partialTick
	}

	private fun renderLayer(guiGraphics: GuiGraphics, layer: TextureLayer, x: Int, y: Int) {
		guiGraphics.blitSprite(
			layer.texture,
			x + layer.xPos,
			y + layer.yPos,
			layer.width.toFloat(),
			layer.height.toFloat()
		)
	}

	private fun renderInternal(guiGraphics: GuiGraphics, partialTick: Float, x: Int, y: Int) {
		val maxWidth = this.internalLayer.width
		val value = this.clampValue
		// 使用更平滑的插值算法
		val maxValue = this.maxValue

		val internalHeight = this.internalLayer.height.toFloat()
		val internalUWidth = value / maxValue * maxWidth

		val posX = x + this.internalLayer.xPos
		val posY = y + this.internalLayer.yPos

		if (this.lightWidth > 0) {
			val v = this.value / maxValue * maxWidth
			val max = posX + maxWidth
			val minX = Math.clamp(v, 0f, max)
			val maxX = Math.clamp(minX + this.lightWidth, 0f, max)
			val maxY = posY + internalHeight
			var color = -0x4d000001
			if (this.isLight) {
				color = -0x1
			}
			val x1 = Math.clamp(posX + minX, posX, max)
			val x2 = Math.clamp(posX + maxX, posX, max)
			guiGraphics.fill(x1, posY, x2, maxY, color)
			this.lightWidth = Mth.lerp(Math.clamp(partialTick * 0.2f, 0f, 1f), this.lightWidth, 0f)
		}

		guiGraphics.blitSprite(
			this.internalLayer.texture, maxWidth.toFloat(), internalHeight,
			0f, 0f, posX, posY, internalUWidth, internalHeight
		)
	}

	val clampValue: Float
		get() = Math.clamp(this.oldValue, 0f, this.maxValue)

	override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
	}

	@AllOpe
	class TextureLayer(
		var xPos: Float,
		var yPos: Float,
		var width: Int,
		var height: Int,
		var texture: ResourceLocation
	) {
		fun setPos(xPos: Float, yPos: Float) {
			this.xPos = xPos
			this.yPos = yPos
		}

		fun setSize(width: Int, height: Int) {
			this.width = width
			this.height = height
		}

		fun setPosAndSize(xPos: Float, yPos: Float, width: Int, height: Int) {
			this.xPos = xPos
			this.yPos = yPos
			this.width = width
			this.height = height
		}

		fun set(texture: ResourceLocation, xPos: Float, yPos: Float, width: Int, height: Int) {
			this.texture = texture
			this.xPos = xPos
			this.yPos = yPos
			this.width = width
			this.height = height
		}
	}
}
