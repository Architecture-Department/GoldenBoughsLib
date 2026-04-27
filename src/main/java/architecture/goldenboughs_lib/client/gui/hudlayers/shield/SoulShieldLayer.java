package architecture.goldenboughs_lib.client.gui.hudlayers.shield;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibMobEffects;
import net.minecraft.resources.ResourceLocation;

public class SoulShieldLayer extends ShieldBarLayer {
	protected static final ResourceLocation TEXTURE = GoldenBoughsLib.modRl("hud_bar/shield/soul_shield_bar");
	protected static final ResourceLocation BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/shield/soul_shield_bar_bottom");
	protected static final ResourceLocation LIGHT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/shield/soul_shield_bar_light");

	public SoulShieldLayer() {
		super(TEXTURE, BOTTOM_TEXTURE, LIGHT_TEXTURE, LibMobEffects.SOUL_ABSORPTION_SHIELD);
	}
}
