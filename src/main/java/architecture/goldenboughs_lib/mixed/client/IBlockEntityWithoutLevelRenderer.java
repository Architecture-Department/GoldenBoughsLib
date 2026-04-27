package architecture.goldenboughs_lib.mixed.client;

import architecture.goldenboughs_lib.api.NoMixinException;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IBlockEntityWithoutLevelRenderer {
	static IBlockEntityWithoutLevelRenderer of(BlockEntityWithoutLevelRenderer obj) {
		return obj;
	}

	default void goldenboughs_lib$renderByItem(LivingEntity sourceLivingEntity, ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		throw new NoMixinException();
	}

	default @Nullable LivingEntity goldenboughs_lib$getSourceLivingEntity() {
		throw new NoMixinException();
	}
}
