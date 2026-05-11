package architecture.goldenboughs_lib.client.gui.hudlayers

import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.player.LocalPlayer
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BooleanSupplier
import java.util.function.Consumer

/**
 * 组合层
 */
abstract class CompositeHudLayer : IHudLayer() {
	private val layers: MutableMap<IHudLayer, BooleanSupplier> = LinkedHashMap()

	protected var screenWidth: Int = 0

	protected var screenHeight: Int = 0

	fun addLayer(layer: IHudLayer): CompositeHudLayer {
		this.layers[layer] = BooleanSupplier { true }
		return this
	}

	fun addLayer(layer: IHudLayer, supplier: BooleanSupplier): CompositeHudLayer {
		this.layers[layer] = supplier
		return this
	}

	fun getLayers(): MutableSet<MutableMap.MutableEntry<IHudLayer, BooleanSupplier>> {
		return this.layers.entries
	}

	override fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		if (this.minecraft.options.hideGui) {
			return
		}

		init(guiGraphics, deltaTracker)
		renderSubLayer(guiGraphics, deltaTracker)
	}

	override fun init(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val newScreenWidth = guiGraphics.guiWidth()
		val newScreenHeight = guiGraphics.guiHeight()

		val isWidthChange = newScreenWidth != this.screenWidth
		val isHeightChange = newScreenHeight != this.screenHeight
		if (isWidthChange || isHeightChange) {
			sizeChange(isWidthChange, isHeightChange, newScreenWidth, newScreenHeight)
		}

		if (this.font !== this.minecraft.font) {
			this.font = this.minecraft.font
			updateSubLayerFont(this.font)
		}

		val newPlayer = Objects.requireNonNull<LocalPlayer>(this.minecraft.player)
		if (this.player !== newPlayer) {
			playerChange(newPlayer)
		}

		subInit(guiGraphics, deltaTracker)
	}

	protected fun subInit(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		this.layers.keys.forEach(Consumer { layer: IHudLayer -> layer.init(guiGraphics, deltaTracker) })
	}

	protected fun updateSubLayerFont(font: Font) {
		this.layers.forEach { (layer: IHudLayer, supplier: BooleanSupplier) -> layer.font = font }
	}

	override fun playerChange(newPlayer: LocalPlayer) {
		this.player = newPlayer
		updateSubLayerPlayer(newPlayer)
	}

	protected fun updateSubLayerPlayer(newPlayer: LocalPlayer) {
		this.layers.keys.forEach(Consumer { layer: IHudLayer -> layer.playerChange(newPlayer) })
	}

	protected fun sizeChange(
		isWidthChange: Boolean,
		isHeightChange: Boolean,
		newScreenWidth: Int,
		newScreenHeight: Int
	) {
		if (isWidthChange) {
			this.screenWidth = newScreenWidth
		}

		if (isHeightChange) {
			this.screenHeight = newScreenHeight
		}

		updatePos(isWidthChange, isHeightChange, newScreenWidth, newScreenHeight)
		this.layers.forEach { (layer: IHudLayer, supplier: BooleanSupplier) ->
			if (isHeightChange) {
				layer.topPos = this.topPos
			}
			if (isWidthChange) {
				layer.leftPos = this.leftPos
			}
		}
	}

	protected fun updatePos(
		isWidthChange: Boolean,
		isHeightChange: Boolean,
		newScreenWidth: Int,
		newScreenHeight: Int
	) {
	}

	protected fun renderSubLayer(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val pose = guiGraphics.pose()
		pose.pushPose()
		for (entry in this.layers.entries) {
			val layer: IHudLayer = entry.key
			val supplier: BooleanSupplier = entry.value
			if (!supplier.asBoolean) {
				continue
			}
			pose.translate(0f, -layer.height.toFloat(), 0f)
			layer.render(guiGraphics, deltaTracker)
			pose.translate(0f, -2f, 0f)
		}
		pose.popPose()
	}

	override val width: Int
		get() = this.layers.entries.stream()
			.filter { a -> a.value.asBoolean }
			.mapToInt { entry -> entry.key.width }
			.max()
			.orElse(0)

	override val height: Int
		get() {
			if (this.layers.isEmpty()) {
				return 0
			}

			val atomicInteger = AtomicInteger(0)
			val height: Int = this.layers.entries.stream()
				.filter { a -> a.value.asBoolean }
				.mapToInt { entry ->
					atomicInteger.incrementAndGet()
					entry.key.height
				}.sum()
			val count = atomicInteger.get()
			if (count == 0) {
				return 0
			}
			return count - 1 + height
		}
}
