package architecture.goldenboughs_lib.module.shield.hud

import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibMobEffects

class ErosionShieldLayer : ShieldBarLayer(
	LibConstants.modRl("hud_bar/shield/erosion_shield_bar"),
	LibConstants.modRl("hud_bar/shield/erosion_shield_bar_bottom"),
	LibConstants.modRl("hud_bar/shield/erosion_shield_bar_light"),
	LibMobEffects.EROSION_ABSORPTION_SHIELD
)
