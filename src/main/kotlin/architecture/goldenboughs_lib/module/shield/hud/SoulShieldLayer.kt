package architecture.goldenboughs_lib.module.shield.hud

import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibMobEffects

class SoulShieldLayer : ShieldBarLayer(
	LibConstants.modRl("hud_bar/shield/soul_shield_bar"),
	LibConstants.modRl("hud_bar/shield/soul_shield_bar_bottom"),
	LibConstants.modRl("hud_bar/shield/soul_shield_bar_light"),
	LibMobEffects.SOUL_ABSORPTION_SHIELD
)
