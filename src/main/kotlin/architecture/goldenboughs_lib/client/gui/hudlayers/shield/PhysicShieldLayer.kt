package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibMobEffects

class PhysicShieldLayer : ShieldBarLayer(
	Lib.modRl("hud_bar/shield/physic_shield_bar"),
	Lib.modRl("hud_bar/shield/physic_shield_bar_bottom"),
	Lib.modRl("hud_bar/shield/physic_shield_bar_light"),
	LibMobEffects.PHYSIC_ABSORPTION_SHIELD
)
