package architecture.goldenboughs_lib.client.renderer

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.item.Item
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer
import java.util.function.BiFunction

/**
 * 物品渲染提供程序
 */
@AllOpe
class GeoItemRenderProvider<T> @JvmOverloads constructor(
	@JvmField protected final val defaultModel: GeoModel<T>,
	@JvmField protected final val guiModel: GeoModel<T>,
	private val rendererFunction: BiFunction<GeoModel<T>, GeoModel<T>, GeoItemRenderer<T>> =
		{ model: GeoModel<T>, guiModel: GeoModel<T> ->
			GeoItemRendererExpand<T>(model, guiModel)
		}
) : GeoRenderProvider where T : Item, T : GeoItem {
	private var renderer: GeoItemRenderer<T>? = null

	override fun getGeoItemRenderer(): BlockEntityWithoutLevelRenderer? {
		if (this.renderer == null) {
			this.renderer = rendererFunction.apply(this.defaultModel, this.guiModel)
		}
		return this.renderer
	}
}
