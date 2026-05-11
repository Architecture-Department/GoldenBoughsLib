package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.client.gui.hudlayers.StatusBarLayer
import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar
import architecture.goldenboughs_lib.config.LibConfig
import architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

abstract class ShieldBarLayer(
	protected val texture: ResourceLocation,
	protected val bottomTexture: ResourceLocation,
	protected val lightTexture: ResourceLocation,
	protected val absorptionEffect: Holder<MobEffect>
) : StatusBarLayer(
	HorizontalStatusBar(
		93, 11,
		HorizontalStatusBar.TextureLayer(0f, 0f, 93, 11, bottomTexture),
		HorizontalStatusBar.TextureLayer(14f, 2f, 78, 7, texture),
		HorizontalStatusBar.TextureLayer(0f, 0f, 93, 11, lightTexture)
	)
) {

	override val maxValueFromSource: Float
		get() {
			player ?: return 0f

			val effect = this.player!!.getEffect(absorptionEffect)
			val level = LibConfig.SERVER.shieldAdditionalValuePerLevel.get().toFloat()
			effect ?: return 0f

			return level * (effect.amplifier + 1)
		}

	override fun updateStatusBarAppearance() {
	}

	public override fun renderStatusBar(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		if (currentValueFromSource != 0f) super.renderStatusBar(guiGraphics, deltaTracker)
	}

	override val currentValueFromSource: Float
		get() {
			var shieldAmount = 0.0f
			for (entry in LibAbsorptionShieldsRegistry.shields) {
				if (entry.effect == absorptionEffect) shieldAmount = player?.getData(entry.attachment.get()) ?: 0.0f
			}
			return shieldAmount
		}

}
