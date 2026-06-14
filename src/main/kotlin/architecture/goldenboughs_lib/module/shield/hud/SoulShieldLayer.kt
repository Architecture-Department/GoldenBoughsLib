package architecture.goldenboughs_lib.module.shield.hud

import architecture.goldenboughs_lib.init.LibMobEffects
import architecture.goldenboughs_lib.util.LibUtil

class SoulShieldLayer : ShieldBarLayer(
	LibUtil.modRl("hud_bar/shield/soul_shield_bar"),
	LibUtil.modRl("hud_bar/shield/soul_shield_bar_bottom"),
	LibUtil.modRl("hud_bar/shield/soul_shield_bar_light"),
	LibMobEffects.SOUL_ABSORPTION_SHIELD
)
