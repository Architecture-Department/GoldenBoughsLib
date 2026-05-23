package architecture.goldenboughs_lib.client.renderlayer

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.client.LibRenderTypes
import architecture.goldenboughs_lib.util.LibUtil.getTextureLight
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.renderer.GeoRenderer
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer

@AllOpe
class AutoGlowingRenderLayer<T : GeoAnimatable>(
	renderer: GeoRenderer<T>,
	@JvmField protected final val glowmaskValue: Array<Float>
) : AutoGlowingGeoLayer<T>(renderer) {
	@JvmField
	protected final var renderType: RenderType? = null

	override fun render(
		poseStack: PoseStack,
		animatable: T,
		bakedModel: BakedGeoModel,
		renderType: RenderType?,
		bufferSource: MultiBufferSource,
		buffer: VertexConsumer?,
		partialTick: Float,
		packedLight: Int,
		packedOverlay: Int
	) {
		val renderType = getRenderType(animatable, bufferSource)
		val vertexConsumer1 = bufferSource.getBuffer(renderType)
		getRenderer().reRender(
			bakedModel, poseStack, bufferSource, animatable, renderType,
			vertexConsumer1, partialTick, LightTexture.FULL_BRIGHT, packedOverlay,
			getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt()
		)
	}

	override fun getRenderType(animatable: T?, bufferSource: MultiBufferSource?): RenderType {
		if (renderType == null) {
			renderType = LibRenderTypes.glowmask(glowmaskValue, getTextureResource(animatable))
		}
		return renderType!!
	}

	override fun getTextureResource(animatable: T?): ResourceLocation {
		return getTextureLight(super.getTextureResource(animatable))
	}
}
