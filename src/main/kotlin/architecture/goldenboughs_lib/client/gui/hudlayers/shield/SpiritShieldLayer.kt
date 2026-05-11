package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibMobEffects

class SpiritShieldLayer : ShieldBarLayer(
	Lib.modRl("hud_bar/shield/spirit_shield_bar"),
	Lib.modRl("hud_bar/shield/spirit_shield_bar_bottom"),
	Lib.modRl("hud_bar/shield/spirit_shield_bar_light"),
	LibMobEffects.SPIRIT_ABSORPTION_SHIELD
)
