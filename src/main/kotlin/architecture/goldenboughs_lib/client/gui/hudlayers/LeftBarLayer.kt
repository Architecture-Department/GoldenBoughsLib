package architecture.goldenboughs_lib.client.gui.hudlayers

import architecture.goldenboughs_lib.client.gui.hudlayers.shield.ErosionShieldLayer
import architecture.goldenboughs_lib.client.gui.hudlayers.shield.PhysicShieldLayer
import architecture.goldenboughs_lib.client.gui.hudlayers.shield.SoulShieldLayer
import architecture.goldenboughs_lib.client.gui.hudlayers.shield.SpiritShieldLayer
import architecture.goldenboughs_lib.config.LibConfig
import architecture.goldenboughs_lib.init.LibMobEffects
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics

class LeftBarLayer : CompositeHudLayer() {
	val rationalityBarLayer: RationalityBarLayer = RationalityBarLayer()

	val newHealthBarLayer: NewHealthBarLayer = NewHealthBarLayer()

	val physicShieldLayer: PhysicShieldLayer = PhysicShieldLayer()

	val spiritShieldLayer: SpiritShieldLayer = SpiritShieldLayer()

	val erosionShieldLayer: ErosionShieldLayer = ErosionShieldLayer()

	val soulShieldLayer: SoulShieldLayer = SoulShieldLayer()

	init {
		addLayer(
			this.newHealthBarLayer
		) {
			!this.minecraft.options.hideGui &&
				LibConfig.CLIENT.enableNewHealthBar.get() && this.player != null && !getPlayerThrow().isSpectator() && !getPlayerThrow().isCreative()
		}
		addLayer(
			this.rationalityBarLayer
		) { !this.minecraft.options.hideGui && this.player != null && !getPlayerThrow().isSpectator() }
		addLayer(
			this.physicShieldLayer
		) {
			!this.minecraft.options.hideGui && this.player != null && !getPlayerThrow().isSpectator() && !getPlayerThrow().isCreative() &&
				getPlayerThrow().hasEffect(LibMobEffects.PHYSIC_ABSORPTION_SHIELD)
		}
		addLayer(
			this.spiritShieldLayer
		) {
			!this.minecraft.options.hideGui && this.player != null && !getPlayerThrow().isSpectator() && !getPlayerThrow().isCreative() &&
				getPlayerThrow().hasEffect(LibMobEffects.SPIRIT_ABSORPTION_SHIELD)
		}
		addLayer(
			this.erosionShieldLayer
		) {
			!this.minecraft.options.hideGui && this.player != null && !getPlayerThrow().isSpectator() && !getPlayerThrow().isCreative() &&
				getPlayerThrow().hasEffect(LibMobEffects.EROSION_ABSORPTION_SHIELD)
		}
		addLayer(
			this.soulShieldLayer
		) {
			!this.minecraft.options.hideGui && this.player != null && !getPlayerThrow().isSpectator() && !getPlayerThrow().isCreative() &&
				getPlayerThrow().hasEffect(LibMobEffects.SOUL_ABSORPTION_SHIELD)
		}
	}

	override fun updatePos(isWidthChange: Boolean, isHeightChange: Boolean, newScreenWidth: Int, newScreenHeight: Int) {
		super.updatePos(isWidthChange, isHeightChange, newScreenWidth, newScreenHeight)
		if (isWidthChange) {
			this.leftPos = newScreenWidth / 2 - 105
		}
		if (isHeightChange) {
			this.topPos = newScreenHeight - 30
		}
	}

	override fun renderSubLayer(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val pose = guiGraphics.pose()
		pose.pushPose()
		if (this.player?.isCreative() ?: false) {
			pose.translate(0f, 5f, 0f)
		} else if (!LibConfig.CLIENT.enableNewHealthBar.get()) {
			pose.translate(0f, -10f, 0f)
		}
		super.renderSubLayer(guiGraphics, deltaTracker)
		pose.popPose()
	}

	companion object {
		@JvmField
		val INSTANCE: LeftBarLayer = LeftBarLayer()
	}
}