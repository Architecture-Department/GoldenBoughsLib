package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibMobEffects

class SpiritShieldLayer : ShieldBarLayer(
	LibConstants.modRl("hud_bar/shield/spirit_shield_bar"),
	LibConstants.modRl("hud_bar/shield/spirit_shield_bar_bottom"),
	LibConstants.modRl("hud_bar/shield/spirit_shield_bar_light"),
	LibMobEffects.SPIRIT_ABSORPTION_SHIELD
)
