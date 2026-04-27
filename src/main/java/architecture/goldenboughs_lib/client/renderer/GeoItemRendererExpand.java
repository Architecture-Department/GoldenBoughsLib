package architecture.goldenboughs_lib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

/**
 * 一个特殊的物品渲染可在GUI和世界中以不同的模型渲染
 */
public class GeoItemRendererExpand<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
	protected final @Nullable GeoModel<T> guiModel;

	public GeoItemRendererExpand(GeoModel<T> model, @Nullable GeoModel<T> guiModel) {
		super(model);
		this.guiModel = guiModel;
		addRenderLayer(new BlockAndItemGeoLayer<>(this));
	}

	@Override
	public GeoModel<T> getGeoModel() {
		return this.guiModel != null && this.renderPerspective == ItemDisplayContext.GUI ? this.guiModel : this.model;
	}

	public void renderByItem(@Nullable LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
		super.renderByItem(itemStack, displayContext, poseStack, bufferSource, combinedLight, combinedOverlay);
	}
}
