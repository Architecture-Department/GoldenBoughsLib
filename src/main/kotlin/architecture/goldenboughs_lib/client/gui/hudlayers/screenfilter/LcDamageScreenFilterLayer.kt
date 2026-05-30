package architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.client.gui.hudlayers.BasicHudLayer
import architecture.goldenboughs_lib.core.LibConstants
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import java.util.Map

class LcDamageScreenFilterLayer : BasicHudLayer() {
	private val rationalityFilters: MutableSet<Filter> = LinkedHashSet(4)

	private val filterObjects: MutableMap<LcDamageType, Filter> = Map.of(
		LcDamageType.PHYSICS, Filter(PHYSICS),
		LcDamageType.SPIRIT, Filter(SPIRIT),
		LcDamageType.EROSION, Filter(EROSION),
		LcDamageType.THE_SOUL, Filter(THE_SOUL)
	)

	fun addFilter(damageType: LcDamageType?) {
		val filter = this.filterObjects.getOrDefault(
			damageType ?: PHYSICS,
			this.filterObjects[LcDamageType.PHYSICS]
		)!!

		filter.setValue(1f)
		this.rationalityFilters.add(filter)
	}

	override val width: Int
		get() = screenWidth

	override val height: Int
		get() = screenHeight

	override fun renderDrawLayer(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		if (this.rationalityFilters.isEmpty()) {
			return
		}
		val pose = guiGraphics.pose()
		pose.pushPose()
		RenderSystem.disableDepthTest()
		RenderSystem.depthMask(false)
		RenderSystem.enableBlend()

		val iterator = this.rationalityFilters.iterator()
		val partialTick = Math.clamp(deltaTracker.realtimeDeltaTicks, 0f, 1f)
		while (iterator.hasNext()) {
			val filter = iterator.next()
			if (filter.getAlpha() <= 0 && filter.currentValue <= 0 && filter.previousValue <= 0) {
				iterator.remove()
				continue
			}
			filter.render(guiGraphics, 0, 0, partialTick)
		}

		RenderSystem.disableBlend()
		RenderSystem.depthMask(true)
		RenderSystem.enableDepthTest()
		guiGraphics.setColor(1f, 1f, 1f, 1f)
		pose.popPose()
	}

	class Filter(
		texture: ResourceLocation
	) : RationalityScreenFilterLayer.Filter(texture) {
		var currentValue: Float = 0f
		var previousValue: Float = 0f

		fun reset() {
			super.setAlpha(1f)
		}

		fun setValue(value: Float) {
			this.currentValue = value
			this.previousValue = value
		}

		override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
			val clamp = Math.clamp(partialTick * 0.5, 0.0, 1.0)
			val calculatedRenderedValue =
				Math.clamp(Mth.lerp(clamp, this.previousValue.toDouble(), this.currentValue.toDouble()), 0.0, 1.0)
					.toFloat()
			setAlpha(calculatedRenderedValue)
			super.renderWidget(guiGraphics, mouseX, mouseY, partialTick)
			if (this.previousValue != calculatedRenderedValue) {
				this.previousValue = calculatedRenderedValue
			}
			this.currentValue -= clamp.toFloat()
		}
	}

	companion object {
		@JvmField
		val PHYSICS: ResourceLocation = LibConstants.modRl("textures/gui/filter/physics.png")

		@JvmField
		val SPIRIT: ResourceLocation = LibConstants.modRl("textures/gui/filter/spirit.png")

		@JvmField
		val EROSION: ResourceLocation = LibConstants.modRl("textures/gui/filter/erosion.png")

		@JvmField
		val THE_SOUL: ResourceLocation = LibConstants.modRl("textures/gui/filter/the_soul.png")

		@JvmField
		val INSTANCE: LcDamageScreenFilterLayer = LcDamageScreenFilterLayer()
	}
}
