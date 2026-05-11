package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.GoldenBoughsLib
import architecture.goldenboughs_lib.init.LibMobEffects

class ErosionShieldLayer : ShieldBarLayer(
	GoldenBoughsLib.modRl("hud_bar/shield/erosion_shield_bar"),
	GoldenBoughsLib.modRl("hud_bar/shield/erosion_shield_bar_bottom"),
	GoldenBoughsLib.modRl("hud_bar/shield/erosion_shield_bar_light"),
	LibMobEffects.EROSION_ABSORPTION_SHIELD
)
