package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibMobEffects

class ErosionShieldLayer : ShieldBarLayer(
	Lib.modRl("hud_bar/shield/erosion_shield_bar"),
	Lib.modRl("hud_bar/shield/erosion_shield_bar_bottom"),
	Lib.modRl("hud_bar/shield/erosion_shield_bar_light"),
	LibMobEffects.EROSION_ABSORPTION_SHIELD
)
