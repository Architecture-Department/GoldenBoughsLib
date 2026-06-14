package architecture.goldenboughs_lib.module.rationality.hud

import architecture.goldenboughs_lib.client.gui.hudlayers.StatusBarLayer
import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar
import architecture.goldenboughs_lib.module.rationality.util.RationalityUtil.getMaxRationalityValue
import architecture.goldenboughs_lib.module.rationality.util.RationalityUtil.getRationalityValue
import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.resources.ResourceLocation

class RationalityBarLayer : StatusBarLayer(
	HorizontalStatusBar(
		93, 11,
		HorizontalStatusBar.TextureLayer(0f, 0f, 93, 11, DEFAULT_BOTTOM_TEXTURE),
		HorizontalStatusBar.TextureLayer(14f, 2f, 78, 7, DEFAULT_TEXTURE),
		HorizontalStatusBar.TextureLayer(0f, 0f, 93, 11, DEFAULT_LIGHT_TEXTURE)
	)
) {
	private val lowDynamicStatusBar: ImageWidget = ImageWidget.sprite(98, 14, LOW_DYNAMIC_TEXTURE)

	override fun renderStatusBar(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		super.renderStatusBar(guiGraphics, deltaTracker)
		val deltaTime = deltaTracker.realtimeDeltaTicks
		this.lowDynamicStatusBar.render(guiGraphics, 0, 0, deltaTime)
	}

	override val maxValueFromSource: Float
		get() = this.player?.getMaxRationalityValue() ?: 0.0f

	override val currentValueFromSource: Float
		get() = this.player?.getRationalityValue() ?: 0.0f

	override fun updateStatusBarAppearance() {
		val rationality = this.renderedValue
		val maxRationalityValue = this.maxValue

		val dynamicBar = this.lowDynamicStatusBar
		val statusBar = this.statusBar
		val bottomLayer = statusBar.bottomLayer
		val internalLayer = statusBar.internalLayer
		val lightLayer = statusBar.lightLayer

		// 最低理智-动态
		if (rationality <= maxRationalityValue * -0.99f) {
			statusBar.visible = false
			dynamicBar.visible = true
			return
		}

		statusBar.visible = true
		dynamicBar.visible = false

		// 高理智
		if (rationality >= maxRationalityValue * 0.7f) {
			bottomLayer.set(TALL_BOTTOM_TEXTURE, 0f, 0f, 93, 11)

			internalLayer.texture = TALL_TEXTURE
			lightLayer.texture = TALL_LIGHT_TEXTURE
			return
		}

		// 默认理智
		if (rationality >= -0.99f) {
			bottomLayer.set(DEFAULT_BOTTOM_TEXTURE, 0f, 0f, 93, 11)

			internalLayer.texture = DEFAULT_TEXTURE
			lightLayer.texture = DEFAULT_LIGHT_TEXTURE
			return
		}

		// 低理智
		bottomLayer.set(LOW_BOTTOM_TEXTURE, -1f, -2f, 94, 14)

		internalLayer.texture = LOW_TEXTURE
		lightLayer.texture = LOW_LIGHT_TEXTURE
	}

	override var leftPos: Int
		get() = super.leftPos
		set(value) {
			super.leftPos = value
			this.lowDynamicStatusBar.x = value - 3
		}

	override var topPos: Int
		get() = super.topPos
		set(value) {
			super.topPos = value
			this.lowDynamicStatusBar.y = value - 1
		}

	companion object {
		// 默认
		@JvmField
		val DEFAULT_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality")

		@JvmField
		val DEFAULT_BOTTOM_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_bottom")

		@JvmField
		val DEFAULT_LIGHT_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_light")

		// 低理智
		@JvmField
		val LOW_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_low")

		@JvmField
		val LOW_BOTTOM_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_low_bottom")

		@JvmField
		val LOW_LIGHT_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_low_light")

		// 动态
		@JvmField
		val LOW_DYNAMIC_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_low_dynamic")

		// 高理智
		@JvmField
		val TALL_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_tall")

		@JvmField
		val TALL_BOTTOM_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_tall_bottom")

		@JvmField
		val TALL_LIGHT_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/rationality/rationality_tall_light")
	}
}
