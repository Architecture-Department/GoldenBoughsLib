package architecture.goldenboughs_lib.module.shield.hud

import architecture.goldenboughs_lib.init.LibMobEffects
import architecture.goldenboughs_lib.util.LibUtil

class SpiritShieldLayer : ShieldBarLayer(
	LibUtil.modRl("hud_bar/shield/spirit_shield_bar"),
	LibUtil.modRl("hud_bar/shield/spirit_shield_bar_bottom"),
	LibUtil.modRl("hud_bar/shield/spirit_shield_bar_light"),
	LibMobEffects.SPIRIT_ABSORPTION_SHIELD
)
