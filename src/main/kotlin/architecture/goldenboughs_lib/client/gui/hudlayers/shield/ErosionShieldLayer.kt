package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibMobEffects
import architecture.goldenboughs_lib.core.LibConstants

class ErosionShieldLayer : ShieldBarLayer(
	LibConstants.modRl("hud_bar/shield/erosion_shield_bar"),
	LibConstants.modRl("hud_bar/shield/erosion_shield_bar_bottom"),
	LibConstants.modRl("hud_bar/shield/erosion_shield_bar_light"),
	LibMobEffects.EROSION_ABSORPTION_SHIELD
)
