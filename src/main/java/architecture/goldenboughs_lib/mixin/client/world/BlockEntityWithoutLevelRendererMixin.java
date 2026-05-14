package architecture.goldenboughs_lib.mixin.client.world;

import architecture.goldenboughs_lib.client.renderer.GeoItemRendererExpand;
import architecture.goldenboughs_lib.mixed.client.IBlockEntityWithoutLevelRendererLib;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

@Mixin(value = BlockEntityWithoutLevelRenderer.class, priority = 1500)
public abstract class BlockEntityWithoutLevelRendererMixin implements IBlockEntityWithoutLevelRendererLib {
	@Unique
	private @Nullable LivingEntity goldenboughs_lib$sourceLivingEntity;

	@Override
	public void goldenboughs_lib$renderByItem(LivingEntity sourceLivingEntity, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		goldenboughs_lib$sourceLivingEntity = sourceLivingEntity;

		if (GeoRenderProvider.of(stack).getGeoItemRenderer() instanceof GeoItemRendererExpand<?> renderer) {
			renderer.renderByItem(sourceLivingEntity, stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
			return;
		}

		renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);

		goldenboughs_lib$sourceLivingEntity = null;
	}

	@Shadow
	public abstract void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay);

	@Override
	public @Nullable LivingEntity goldenboughs_lib$getSourceLivingEntity() {
		return goldenboughs_lib$sourceLivingEntity;
	}
}
