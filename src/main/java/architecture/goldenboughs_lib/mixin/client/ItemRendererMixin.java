package architecture.goldenboughs_lib.mixin.client;

import architecture.goldenboughs_lib.mixed.client.IItemModelShaper;
import architecture.goldenboughs_lib.util.LibUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
