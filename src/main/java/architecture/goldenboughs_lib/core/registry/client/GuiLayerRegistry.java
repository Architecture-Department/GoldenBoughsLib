package architecture.goldenboughs_lib.core.registry.client;

import architecture.goldenboughs_lib.client.LibGuiLayers;
import architecture.goldenboughs_lib.client.gui.hudlayers.LeftBarLayer;
import architecture.goldenboughs_lib.client.gui.hudlayers.chop_flavor.ChopFlavorLayer;
import architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter.LcDamageScreenFilterLayer;
import architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter.RationalityScreenFilterLayer;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = GoldenBoughsLib.ID, value = Dist.CLIENT)
public final class GuiLayerRegistry {
	@SubscribeEvent
	public static void register(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.AIR_LEVEL, LibGuiLayers.CHOP_FLAVOR, ChopFlavorLayer.INSTANCE);
		ChopFlavorLayer.init();
		event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, LibGuiLayers.LC_DAMAGE_SCREEN_FILTER, LcDamageScreenFilterLayer.INSTANCE);
		event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, LibGuiLayers.LEFT_BAR, LeftBarLayer.INSTANCE);
		event.wrapLayer(VanillaGuiLayers.ARMOR_LEVEL, (layer) -> (guiGraphics, deltaTracker) -> {
			PoseStack pose = guiGraphics.pose();
			pose.pushPose();
			pose.translate(0, -(LeftBarLayer.INSTANCE.getHeight() + 1), 0);
			layer.render(guiGraphics, deltaTracker);
			pose.popPose();
		});
		event.registerAbove(VanillaGuiLayers.SAVING_INDICATOR, LibGuiLayers.SCREEN_FILTER, RationalityScreenFilterLayer.INSTANCE);
	}
}
