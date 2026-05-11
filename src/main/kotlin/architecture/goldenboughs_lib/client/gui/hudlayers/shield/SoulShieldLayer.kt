package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibMobEffects

class SoulShieldLayer : ShieldBarLayer(
	Lib.modRl("hud_bar/shield/soul_shield_bar"),
	Lib.modRl("hud_bar/shield/soul_shield_bar_bottom"),
	Lib.modRl("hud_bar/shield/soul_shield_bar_light"),
	LibMobEffects.SOUL_ABSORPTION_SHIELD
)
