package architecture.goldenboughs_lib.client.renderer

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoArmorRenderer
import java.util.function.Function

/**
 * 盔甲渲染提供程序
 */
@AllOpen
class GeoArmourRenderProvider<T> : GeoRenderProvider where T : Item, T : GeoItem {
	@JvmField
	protected final val model: GeoModel<T>
	private val rendererFunction: Function<GeoModel<T>, GeoArmorRenderer<T>>
	private var renderer: GeoArmorRenderer<T>? = null

	constructor(model: GeoModel<T>, rendererFunction: Function<GeoModel<T>, GeoArmorRenderer<T>>) {
		this.model = model
		this.rendererFunction = rendererFunction
	}

	constructor(model: GeoModel<T>) {
		this.model = model
		this.rendererFunction = Function { model: GeoModel<T> -> RoughAndFineArmorRenderer(model) }
	}

	override fun <T : LivingEntity?> getGeoArmorRenderer(
		livingEntity: T?,
		itemStack: ItemStack,
		equipmentSlot: EquipmentSlot?,
		original: HumanoidModel<T>?
	): HumanoidModel<*>? {
		if (this.renderer == null) {
			this.renderer = rendererFunction.apply(this.model)
		}
		return this.renderer
	}
}
