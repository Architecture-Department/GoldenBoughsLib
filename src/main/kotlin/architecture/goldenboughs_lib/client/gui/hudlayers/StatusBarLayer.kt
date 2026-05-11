package architecture.goldenboughs_lib.client.gui.hudlayers

import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar
import architecture.goldenboughs_lib.util.TextUtil.formatNumber
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.Mth
import kotlin.math.abs

/**
 * 状态条层
 */
abstract class StatusBarLayer protected constructor(
	protected val statusBar: HorizontalStatusBar
) : IHudLayer() {
	protected var currentValue: Float = 0f
	protected var renderedValue: Float = 0f
	protected var previousValue: Float = 0f
	protected var maxValue: Float = 0f

	override fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		renderStatusBar(guiGraphics, deltaTracker)
		renderValueText(guiGraphics)
	}

	protected fun renderStatusBar(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val deltaTime = deltaTracker.realtimeDeltaTicks
		this.statusBar.render(guiGraphics, 0, 0, deltaTime)
	}

	protected fun renderValueText(guiGraphics: GuiGraphics) {
		val font = this.font
		val text: Component = this.renderText
		guiGraphics.drawString(
			font,
			text,
			this.leftPos - font.width(text),
			this.topPos + font.lineHeight / 2 - 3,
			0xFFFFFF
		)
	}

	protected val renderText: MutableComponent
		get() = Component.empty().append(this.valueText).append("/")
			.append(this.maxValueText)

	protected val valueText: MutableComponent
		get() = Component.literal(
			formatNumber(
				this.currentValue.toDouble(),
				2
			)
		)

	protected val maxValueText: MutableComponent
		get() = Component.literal(
			formatNumber(
				this.maxValue.toDouble(),
				2
			)
		)

	override fun init(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		super.init(guiGraphics, deltaTracker)
		updateMaxValue()
		updateValue(deltaTracker)
		updateStatusBarAppearance()
	}

	protected fun updateMaxValue() {
		val newMaxValue = this.maxValueFromSource
		if (newMaxValue == this.maxValue) {
			return
		}

		this.maxValue = newMaxValue
		this.statusBar.maxValue = abs(newMaxValue)
	}

	protected abstract val maxValueFromSource: Float

	protected fun updateValue(deltaTracker: DeltaTracker) {
		val deltaTime = deltaTracker.realtimeDeltaTicks
		val newCurrentValue = this.currentValueFromSource

		val oldValue = this.currentValue
		if (newCurrentValue != oldValue) {
			this.currentValue = newCurrentValue
			this.statusBar.setLight()
		}

		val calculatedRenderedValue =
			Mth.lerp(Math.clamp(deltaTime * 1.5f, 0f, 1f), this.previousValue, newCurrentValue)
		this.renderedValue = calculatedRenderedValue

		this.statusBar.oldValue = abs(calculatedRenderedValue)
		if (calculatedRenderedValue !in 0.0..1.0) {
			val oldValue2 = abs(oldValue)
			val newValue = abs(newCurrentValue)
			if (oldValue2 != newValue) {
				this.statusBar.setLightWidth(oldValue2, newValue)
			}
		} else if (this.statusBar.lightWidth != 0f) {
			this.statusBar.lightWidth = 0f
		}

		if (this.previousValue != calculatedRenderedValue) {
			this.previousValue = calculatedRenderedValue
		}
	}

	protected abstract val currentValueFromSource: Float

	protected abstract fun updateStatusBarAppearance()

	override var leftPos: Int
		get() = super.leftPos
		set(value) {
			super.leftPos = value
			this.statusBar.x = value
		}

	override var topPos: Int
		get() = super.topPos
		set(value) {
			super.topPos = value
			this.statusBar.y = value
		}

	override val width: Int
		get() = this.statusBar.getWidth()

	override val height: Int
		get() = this.statusBar.getHeight()
}
