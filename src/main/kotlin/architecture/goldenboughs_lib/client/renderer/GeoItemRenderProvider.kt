package architecture.goldenboughs_lib.client.renderer

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.item.Item
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer
import java.util.function.Function

/**
 * 物品渲染提供程序
 */
@AllOpe
class GeoItemRenderProvider<T> @JvmOverloads constructor(
	@JvmField protected final val defaultModel: GeoModel<T>,
	private val rendererFunction: Function<GeoModel<T>, GeoItemRenderer<T>> = { GeoItemRendererExpand(it) }
) : GeoRenderProvider where T : Item, T : GeoItem {
	private var renderer: GeoItemRenderer<T>? = null

	override fun getGeoItemRenderer(): BlockEntityWithoutLevelRenderer? {
		if (this.renderer == null) {
			this.renderer = rendererFunction.apply(this.defaultModel)
		}
		return this.renderer
	}
}
