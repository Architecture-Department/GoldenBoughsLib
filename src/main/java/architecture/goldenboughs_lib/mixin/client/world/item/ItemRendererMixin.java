package architecture.goldenboughs_lib.mixin.client.world.item;

import architecture.goldenboughs_lib.mixed.client.IBlockEntityWithoutLevelRendererLib;
import architecture.goldenboughs_lib.mixed.client.IItemModelShaper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	@Shadow
	@Final
	private ItemModelShaper itemModelShaper;

	@ModifyVariable(method = "render", at = @At(value = "HEAD"), index = 8, argsOnly = true)
	private BakedModel goldenboughs_lib$render(
		BakedModel arg8,
		@Local(argsOnly = true) ItemStack itemStack,
		@Local(argsOnly = true) ItemDisplayContext displayContext
	) {
		if (displayContext != ItemDisplayContext.GUI ||
			itemStack.isEmpty()
		) {
			return arg8;
		}

		BakedModel bakedModel = IItemModelShaper.of(itemModelShaper).goldenboughs_lib$getItemGuiModel(itemStack.getItem());
		if (bakedModel == null) {
			return arg8;
		}
		return bakedModel;
	}

	@Unique
	private @Nullable LivingEntity goldenboughs_lib$sourceLivingEntity;

	@WrapOperation(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"))
	private void goldenboughs_lib$renderStatic(
		ItemRenderer instance,
		ItemStack itemStack,
		ItemDisplayContext diplayContext,
		boolean leftHand,
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		int combinedLight,
		int combinedOverlay,
		BakedModel bakedmodel,
		Operation<Void> original,
		@Local(type = LivingEntity.class, argsOnly = true) LivingEntity livingEntity
	) {
		goldenboughs_lib$sourceLivingEntity = livingEntity;
		original.call(instance, itemStack, diplayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, bakedmodel);
		goldenboughs_lib$sourceLivingEntity = null;
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockEntityWithoutLevelRenderer;renderByItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"))
	private void goldenboughs_lib$render(
		BlockEntityWithoutLevelRenderer instance,
		ItemStack itemStack,
		ItemDisplayContext displayContext,
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		int combinedLight,
		int combinedOverlay,
		Operation<Void> original
	) {
		if (instance instanceof IBlockEntityWithoutLevelRendererLib renderer) {
			renderer.goldenboughs_lib$renderByItem(goldenboughs_lib$sourceLivingEntity, itemStack, displayContext, poseStack, bufferSource, combinedLight, combinedOverlay);
			return;
		}
		original.call(instance, itemStack, displayContext, poseStack, bufferSource, combinedLight, combinedOverlay);
	}
}
