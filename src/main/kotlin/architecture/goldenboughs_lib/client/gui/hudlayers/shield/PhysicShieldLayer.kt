package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibMobEffects
import architecture.goldenboughs_lib.core.LibConstants

class PhysicShieldLayer : ShieldBarLayer(
	LibConstants.modRl("hud_bar/shield/physic_shield_bar"),
	LibConstants.modRl("hud_bar/shield/physic_shield_bar_bottom"),
	LibConstants.modRl("hud_bar/shield/physic_shield_bar_light"),
	LibMobEffects.PHYSIC_ABSORPTION_SHIELD
)
