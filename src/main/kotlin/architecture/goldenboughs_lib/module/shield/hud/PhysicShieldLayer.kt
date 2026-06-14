package architecture.goldenboughs_lib.module.shield.hud

import architecture.goldenboughs_lib.init.LibMobEffects
import architecture.goldenboughs_lib.util.LibUtil

class PhysicShieldLayer : ShieldBarLayer(
	LibUtil.modRl("hud_bar/shield/physic_shield_bar"),
	LibUtil.modRl("hud_bar/shield/physic_shield_bar_bottom"),
	LibUtil.modRl("hud_bar/shield/physic_shield_bar_light"),
	LibMobEffects.PHYSIC_ABSORPTION_SHIELD
)
