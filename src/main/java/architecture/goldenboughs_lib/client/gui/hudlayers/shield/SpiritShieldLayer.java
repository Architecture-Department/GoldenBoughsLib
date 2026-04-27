package architecture.goldenboughs_lib.client.gui.hudlayers.shield;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibMobEffects;
import net.minecraft.resources.ResourceLocation;

public class SpiritShieldLayer extends ShieldBarLayer {
	protected static final ResourceLocation TEXTURE = GoldenBoughsLib.modRl("hud_bar/shield/spirit_shield_bar");
	protected static final ResourceLocation BOTTOM_TEXTURE = GoldenBoughsLib.modRl("hud_bar/shield/spirit_shield_bar_bottom");
	protected static final ResourceLocation LIGHT_TEXTURE = GoldenBoughsLib.modRl("hud_bar/shield/spirit_shield_bar_light");

	public SpiritShieldLayer() {
		super(TEXTURE, BOTTOM_TEXTURE, LIGHT_TEXTURE, LibMobEffects.SPIRIT_ABSORPTION_SHIELD);
	}
}
