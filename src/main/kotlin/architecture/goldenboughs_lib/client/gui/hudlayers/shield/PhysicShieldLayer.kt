package architecture.goldenboughs_lib.client.gui.hudlayers.shield

import architecture.goldenboughs_lib.core.GoldenBoughsLib
import architecture.goldenboughs_lib.init.LibMobEffects

class PhysicShieldLayer : ShieldBarLayer(
	GoldenBoughsLib.modRl("hud_bar/shield/physic_shield_bar"),
	GoldenBoughsLib.modRl("hud_bar/shield/physic_shield_bar_bottom"),
	GoldenBoughsLib.modRl("hud_bar/shield/physic_shield_bar_light"),
	LibMobEffects.PHYSIC_ABSORPTION_SHIELD
)
