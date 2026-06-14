package architecture.goldenboughs_lib.client.gui.hudlayers

import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar
import architecture.goldenboughs_lib.util.LibUtil
import architecture.goldenboughs_lib.util.TextUtil.formatNumberPlaces
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

class NewHealthBarLayer : StatusBarLayer(
	HorizontalStatusBar(
		90, 9,
		HorizontalStatusBar.TextureLayer(0f, 0f, 90, 9, FULL_BOTTOM_TEXTURE),
		HorizontalStatusBar.TextureLayer(11f, 1f, 78, 7, FULL_TEXTURE),
		HorizontalStatusBar.TextureLayer(0f, 0f, 90, 9, LIGHT_TEXTURE)
	)
) {
	private val absorbingStatusBar: ImageWidget = ImageWidget.sprite(80, 9, ABSORBING_TEXTURE)
	private val absorbingLightStatusBar: ImageWidget = ImageWidget.sprite(80, 9, ABSORBING_LIGHT_TEXTURE)
	private var heartType = Gui.HeartType.NORMAL
	private var absorbingHp = 0f
	private var lightTick = 0f

	override fun renderStatusBar(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		super.renderStatusBar(guiGraphics, deltaTracker)
		val deltaTime = deltaTracker.realtimeDeltaTicks

		if (this.absorbingHp <= 0) {
			return
		}

		if (this.lightTick > 0) {
			this.absorbingLightStatusBar.render(guiGraphics, 0, 0, deltaTime)
			this.lightTick -= deltaTime
			return
		}

		this.absorbingStatusBar.render(guiGraphics, 0, 0, deltaTime)
	}

	override val valueText: MutableComponent
		get() {
			val value = super.valueText
			val absorbingHp = this.absorbingHp
			if (absorbingHp > 0) {
				val text: String = "+%s".format(formatNumberPlaces(absorbingHp.toDouble(), 2))
				value.append(Component.literal(text).withColor(0xffe400))
			}

			return value
		}

	override val currentValueFromSource: Float
		get() = this.player?.health ?: 0f

	override val maxValueFromSource: Float
		get() = this.player?.maxHealth ?: 0f


	override fun updateValue(deltaTracker: DeltaTracker) {
		super.updateValue(deltaTracker)

		val absorptionAmount = this.player?.absorptionAmount ?: 0f
		if (absorptionAmount == this.absorbingHp) {
			return
		}

		this.absorbingHp = absorptionAmount
		this.lightTick = (if (this.absorbingHp > 0) 2 else 0).toFloat()
	}

	override fun updateStatusBarAppearance() {
		val heartType = player?.run { Gui.HeartType.forPlayer(this) } ?: Gui.HeartType.NORMAL
		if (heartType == this.heartType) {
			return
		}

		this.heartType = heartType
		val statusBar = this.statusBar
		val bottomLayer = statusBar.bottomLayer
		val internalLayer = statusBar.internalLayer
		when (heartType) {
			Gui.HeartType.POISIONED -> {
				bottomLayer.texture = POISIONED_BOTTOM_TEXTURE
				internalLayer.texture = POISIONED_TEXTURE
			}

			Gui.HeartType.WITHERED -> {
				bottomLayer.texture = WITHERED_BOTTOM_TEXTURE
				internalLayer.texture = WITHERED_TEXTURE
			}

			Gui.HeartType.FROZEN -> {
				bottomLayer.texture = FROZEN_BOTTOM_TEXTURE
				internalLayer.texture = FROZEN_TEXTURE
			}

			else -> {
				bottomLayer.texture = FULL_BOTTOM_TEXTURE
				internalLayer.texture = FULL_TEXTURE
			}
		}
	}

	override var leftPos: Int
		get() = super.leftPos
		set(value) {
			super.leftPos = value
			val leftPosition = value + 3
			this.statusBar.x = leftPosition
			this.absorbingStatusBar.x = leftPosition + 10
			this.absorbingLightStatusBar.x = leftPosition + 10
		}

	override var topPos: Int
		get() = super.topPos
		set(value) {
			super.topPos = value
			this.absorbingStatusBar.y = value
			this.absorbingLightStatusBar.y = value
		}

	companion object {
		@JvmField
		val LIGHT_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/light")

		// 伤害吸收
		@JvmField
		val ABSORBING_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/absorbing")

		@JvmField
		val ABSORBING_LIGHT_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/absorbing_light")

		// 冰冻
		@JvmField
		val FROZEN_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/frozen")

		@JvmField
		val FROZEN_BOTTOM_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/frozen_bottom")

		// 正常
		@JvmField
		val FULL_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/full")

		@JvmField
		val FULL_BOTTOM_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/full_bottom")

		// 凋零
		@JvmField
		val WITHERED_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/withered")

		@JvmField
		val WITHERED_BOTTOM_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/withered_bottom")

		// 中毒
		@JvmField
		val POISIONED_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/poisioned")

		@JvmField
		val POISIONED_BOTTOM_TEXTURE: ResourceLocation = LibUtil.modRl("hud_bar/health/poisioned_bottom")

	}
}
