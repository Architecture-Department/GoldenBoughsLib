package architecture.goldenboughs_lib.events.client

import architecture.goldenboughs_lib.client.LibGuiLayers
import architecture.goldenboughs_lib.core.LibConfig
import architecture.goldenboughs_lib.util.GunWeaponUtil
import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.client.AttackIndicatorStatus
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

@EventBusSubscriber(modid = LibUtil.ID, value = [Dist.CLIENT])
object GuiLayersEvents {
	@SubscribeEvent
	fun onRenderGuiLayerEventPre(event: RenderGuiLayerEvent.Pre) {
		val name = event.name
		val instance = Minecraft.getInstance()

		// 关闭原版的血条
		when (name) {
			VanillaGuiLayers.PLAYER_HEALTH -> {
				if (LibConfig.CLIENT.enableNewHealthBar.get()) {
					event.setCanceled(true)
				}
				return
			}

			LibGuiLayers.SCREEN_FILTER -> {
				if (!LibConfig.CLIENT.enableLowRationalityFilter.get()) {
					event.setCanceled(true)
				}
				return
			}

			LibGuiLayers.LC_DAMAGE_SCREEN_FILTER -> {
				if (!LibConfig.CLIENT.enableLcColorDamageFilter.get()) {
					event.setCanceled(true)
				}
				return
			}

			LibGuiLayers.GUN_CHARGE_UP_HUD_LAYER_CROSSHAIR,
			LibGuiLayers.GUN_CHARGE_UP_HUD_LAYER_HOTBAR -> {
				when (instance.options.attackIndicator().get()) {
					AttackIndicatorStatus.CROSSHAIR -> {
						if (name == LibGuiLayers.GUN_CHARGE_UP_HUD_LAYER_HOTBAR) {
							event.setCanceled(true)
						}
					}

					AttackIndicatorStatus.HOTBAR -> {
						if (name == LibGuiLayers.GUN_CHARGE_UP_HUD_LAYER_CROSSHAIR) {
							event.setCanceled(true)
						}
					}

					else -> {
						event.setCanceled(true)
						return
					}
				}
				if (instance.player != null && !GunWeaponUtil.`is`(instance.player!!)) {
					event.setCanceled(true)
				}
			}
		}
	}

	@SubscribeEvent
	fun onRenderGuiLayerEventPost(event: RenderGuiLayerEvent.Post) {
	}
}
