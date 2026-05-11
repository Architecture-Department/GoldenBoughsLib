package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.GoldenBoughsLib
import architecture.goldenboughs_lib.init.LibMobEffects

class SoulShieldLayer : ShieldBarLayer(
	GoldenBoughsLib.modRl("hud_bar/shield/soul_shield_bar"),
	GoldenBoughsLib.modRl("hud_bar/shield/soul_shield_bar_bottom"),
	GoldenBoughsLib.modRl("hud_bar/shield/soul_shield_bar_light"),
	LibMobEffects.SOUL_ABSORPTION_SHIELD
)
