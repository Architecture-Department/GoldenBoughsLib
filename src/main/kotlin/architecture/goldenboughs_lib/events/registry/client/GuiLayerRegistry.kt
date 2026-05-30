package architecture.goldenboughs_lib.events.registry.client

import architecture.goldenboughs_lib.client.LibGuiLayers
import architecture.goldenboughs_lib.client.gui.hudlayers.LeftBarLayer
import architecture.goldenboughs_lib.client.gui.hudlayers.chop_flavor.ChopFlavorLayer
import architecture.goldenboughs_lib.client.gui.hudlayers.chop_flavor.ChopFlavorLayer.Companion.init
import architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter.LcDamageScreenFilterLayer
import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.module.rationality.hud.RationalityScreenFilterLayer
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

@EventBusSubscriber(modid = LibConstants.ID, value = [Dist.CLIENT])
object GuiLayerRegistry {
	@SubscribeEvent
	fun register(event: RegisterGuiLayersEvent) {
		event.registerAbove(
			VanillaGuiLayers.AIR_LEVEL,
			LibGuiLayers.CHOP_FLAVOR,
			ChopFlavorLayer.INSTANCE
		)
		init()
		event.registerAbove(
			VanillaGuiLayers.CAMERA_OVERLAYS,
			LibGuiLayers.LC_DAMAGE_SCREEN_FILTER,
			LcDamageScreenFilterLayer.INSTANCE
		)
		event.registerAbove(
			VanillaGuiLayers.PLAYER_HEALTH,
			LibGuiLayers.LEFT_BAR,
			LeftBarLayer.INSTANCE
		)
		event.wrapLayer(
			VanillaGuiLayers.ARMOR_LEVEL
		) { layer: LayeredDraw.Layer ->
			LayeredDraw.Layer { guiGraphics: GuiGraphics, deltaTracker: DeltaTracker ->
				val pose = guiGraphics.pose()
				pose.pushPose()
				pose.translate(0f, -(LeftBarLayer.INSTANCE.height + 1).toFloat(), 0f)
				layer.render(guiGraphics, deltaTracker)
				pose.popPose()
			}
		}
		event.registerAbove(
			VanillaGuiLayers.SAVING_INDICATOR,
			LibGuiLayers.SCREEN_FILTER,
			RationalityScreenFilterLayer.INSTANCE
		)
	}
}
