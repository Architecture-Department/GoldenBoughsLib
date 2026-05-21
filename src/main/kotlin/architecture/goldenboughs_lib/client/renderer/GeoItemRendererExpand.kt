package architecture.goldenboughs_lib.client.renderer

import architecture.goldenboughs_lib.api.AllOpe
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer

/**
 * 一个特殊的物品渲染可在GUI和世界中以不同的模型渲染
 */
@AllOpe
class GeoItemRendererExpand<T>(
	model: GeoModel<T>,
	@JvmField protected final val guiModel: GeoModel<T>?
) : GeoItemRenderer<T>(model) where T : Item, T : GeoAnimatable {
	init {
		addRenderLayer(BlockAndItemGeoLayer(this))
	}

	override fun getGeoModel(): GeoModel<T> {
		return if (this.guiModel != null && this.renderPerspective == ItemDisplayContext.GUI) this.guiModel else this.model
	}

	fun renderByItem(
		livingEntity: LivingEntity?,
		itemStack: ItemStack,
		displayContext: ItemDisplayContext,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		combinedLight: Int,
		combinedOverlay: Int
	) {
		super.renderByItem(itemStack, displayContext, poseStack, bufferSource, combinedLight, combinedOverlay)
	}
}
