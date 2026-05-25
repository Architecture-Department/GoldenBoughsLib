package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibMobEffects
import architecture.goldenboughs_lib.core.LibConstants

class SoulShieldLayer : ShieldBarLayer(
	LibConstants.modRl("hud_bar/shield/soul_shield_bar"),
	LibConstants.modRl("hud_bar/shield/soul_shield_bar_bottom"),
	LibConstants.modRl("hud_bar/shield/soul_shield_bar_light"),
	LibMobEffects.SOUL_ABSORPTION_SHIELD
)
