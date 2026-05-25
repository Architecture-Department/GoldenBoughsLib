package architecture.goldenboughs_lib.mixin.client.ui;

import architecture.goldenboughs_lib.mixed.client.IItemModelShaper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@ModifyExpressionValue(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getModel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;"))
	private BakedModel goldenboughs_lib$render(
		BakedModel original,
		@Local(argsOnly = true) ItemStack itemStack
	) {
		if (itemStack.isEmpty()) {
			return original;
		}

		ItemModelShaper itemModelShaper = minecraft.getItemRenderer().getItemModelShaper();
		BakedModel bakedModel = IItemModelShaper.of(itemModelShaper).goldenboughs_lib$getItemGuiModel(itemStack.getItem());
		if (bakedModel == null) {
			return original;
		}

		return bakedModel;
	}
}
