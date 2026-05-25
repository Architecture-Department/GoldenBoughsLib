package architecture.goldenboughs_lib.mixin.client.world.item;

import architecture.goldenboughs_lib.mixed.client.IItemModelShaper;
import architecture.goldenboughs_lib.mixin.client.ModelManagerAccessor;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.RegistryAwareItemModelShaper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RegistryAwareItemModelShaper.class)
public abstract class RegistryAwareItemModelShaperMixin extends ItemModelShaper implements IItemModelShaper {
	@Unique
	private final Map<Item, ModelResourceLocation> goldenboughs_lib$guiLocations = Maps.newIdentityHashMap();
	@Unique
	private final Map<Item, BakedModel> goldenboughs_lib$guiModels = Maps.newIdentityHashMap();

	public RegistryAwareItemModelShaperMixin(ModelManager modelManager) {
		super(modelManager);
	}

	@Inject(method = "register", at = @At("HEAD"))
	private void goldenboughs_lib$register(Item item, ModelResourceLocation location, CallbackInfo ci) {
		ModelResourceLocation mrl = new ModelResourceLocation(location.id().withSuffix("_gui"), location.variant());
		goldenboughs_lib$guiLocations.put(item, mrl);
		goldenboughs_lib$guiModels.put(item, getModelManager().getModel(mrl));
	}

	@Inject(method = "rebuildCache", at = @At("HEAD"))
	private void goldenboughs_lib$rebuildCache(CallbackInfo ci) {
		final ModelManager manager = this.getModelManager();
		for (var e : goldenboughs_lib$guiLocations.entrySet()) {
			goldenboughs_lib$guiModels.put(e.getKey(), ((ModelManagerAccessor) manager).getBakedRegistry().get(e.getValue()));
		}
	}

	@Override
	public @Nullable BakedModel goldenboughs_lib$getItemGuiModel(Item item) {
		return goldenboughs_lib$guiModels.get(item);
	}

	@Override
	public ModelResourceLocation goldenboughs_lib$getGuiLocation(ItemStack stack) {
		ModelResourceLocation location = goldenboughs_lib$guiLocations.get(stack.getItem());
		return location == null ? ModelBakery.MISSING_MODEL_VARIANT : location;
	}
}
