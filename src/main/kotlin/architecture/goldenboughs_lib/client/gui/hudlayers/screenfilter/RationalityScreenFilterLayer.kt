@file:Suppress("ProtectedInFinal")

package architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.client.gui.hudlayers.BasicHudLayer
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.util.RationalityUtil.getMaxRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.getRationalityValue
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import architecture.goldenboughs_lib.core.LibConstants

class RationalityScreenFilterLayer : BasicHudLayer() {
	private val rationalityFilters: Array<Filter> = arrayOf(
		Filter(RATIONALITY),
		Filter(RATIONALITY1),
		Filter(RATIONALITY2)
	)

	protected var currentValue: Float = 0f

	protected var renderedValue: Float = 0f

	protected var previousValue: Float = 0f

	protected var minValue: Float = 0f

	override val width: Int
		get() = screenWidth

	override val height: Int
		get() = screenHeight


	override fun renderDrawLayer(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val partialTick = Math.clamp(deltaTracker.realtimeDeltaTicks, 0f, 1f)
		val pose = guiGraphics.pose()
		pose.pushPose()
		RenderSystem.disableDepthTest()
		RenderSystem.depthMask(false)
		RenderSystem.enableBlend()
		for (filter in this.rationalityFilters) {
			filter.render(guiGraphics, 0, 0, partialTick)
		}
		RenderSystem.disableBlend()
		RenderSystem.depthMask(true)
		RenderSystem.enableDepthTest()
		guiGraphics.setColor(1f, 1f, 1f, 1f)
		pose.popPose()
	}

	override fun init(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		super.init(guiGraphics, deltaTracker)
		val maxValue = this.getPlayerThrow().getMaxRationalityValue()
		val minValue = -maxValue - maxValue / 3
		if (minValue != this.minValue) {
			this.minValue = minValue
		}

		val deltaTime = deltaTracker.realtimeDeltaTicks

		val newCurrentValue = this.getPlayerThrow().getRationalityValue() - maxValue / 3
		if (newCurrentValue != this.currentValue) {
			this.currentValue = newCurrentValue
		}
		val calculatedRenderedValue =
			Mth.lerp(Math.clamp(deltaTime * 0.5f, 0f, 1f), this.previousValue, newCurrentValue)
		this.renderedValue = calculatedRenderedValue

		rationalityChange(calculatedRenderedValue)

		if (this.previousValue != calculatedRenderedValue) {
			this.previousValue = calculatedRenderedValue
		}
	}

	private fun rationalityChange(value: Float) {
		if (value >= 0) {
			for (filter in this.rationalityFilters) {
				filter.setAlpha(0f)
			}
			return
		}
		val v = value / this.minValue

		// 第一个滤镜: 在 0.0-0.5 范围内从 0 升到 1 再降到 0
		this.rationalityFilters[0].setAlpha(max(0f, min(1f, 1 - abs(v - 0.25f) * 4)))

		// 第二个滤镜: 在 0.333-1.0 范围内从 0 升到 1 再降到 0
		this.rationalityFilters[1].setAlpha(max(0f, min(1f, 1 - abs(v - 0.666f) * 1.5f)))

		// 第三个滤镜: 在 0.666-1.0 范围内从 0 升到 1
		this.rationalityFilters[2].setAlpha(max(0f, min(1f, (v - 0.666f) * 3)))
	}

	@AllOpe
	class Filter(private val texture: ResourceLocation) : AbstractWidget(0, 0, 0, 0, Component.empty()) {
		override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
			val pose = guiGraphics.pose()
			pose.pushPose()
			guiGraphics.setColor(1f, 1f, 1f, this.alpha)
			guiGraphics.blit(
				this.texture,
				0,
				0,
				0,
				0f,
				0f,
				guiGraphics.guiWidth(),
				guiGraphics.guiHeight(),
				guiGraphics.guiWidth(),
				guiGraphics.guiHeight()
			)
			guiGraphics.setColor(1f, 1f, 1f, 1f)
			pose.popPose()
		}

		override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
		}

		fun getAlpha(): Float {
			return alpha
		}

		override fun setAlpha(alpha: Float) {
			if (alpha == this.alpha) {
				return
			}
			this.alpha = max(0f, alpha)
		}
	}

	companion object {
		@JvmField
		val RATIONALITY: ResourceLocation = LibConstants.modRl("textures/gui/filter/rationality.png")

		@JvmField
		val RATIONALITY1: ResourceLocation = LibConstants.modRl("textures/gui/filter/rationality1.png")

		@JvmField
		val RATIONALITY2: ResourceLocation = LibConstants.modRl("textures/gui/filter/rationality2.png")

		@JvmField
		val INSTANCE: RationalityScreenFilterLayer = RationalityScreenFilterLayer()
	}
}
