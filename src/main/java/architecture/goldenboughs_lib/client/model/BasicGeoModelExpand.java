package architecture.goldenboughs_lib.client.model;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.mixin.geckolib.GeoModelAccessorMixin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

public abstract class BasicGeoModelExpand<T extends GeoAnimatable> extends GeoModel<T> {
	public final ResourceLocation modelPath;
	public final ResourceLocation texturePath;
	public final ResourceLocation animationsPath;

	public BasicGeoModelExpand(ResourceLocation modelPath, ResourceLocation texturePath, ResourceLocation animationsPath) {
		this.modelPath = modelPath;
		this.texturePath = texturePath;
		this.animationsPath = animationsPath;
	}

	@Override
	public ResourceLocation getModelResource(T animatable) {
		return GeckoLibCache.getBakedModels().get(this.modelPath) == null ? getDefaultModelResource() : this.modelPath;
	}

	@NotNull
	protected ResourceLocation getDefaultModelResource() {
		return BasicGeoModel.model(GoldenBoughsLib.modRl("item/default"));
	}

	@Override
	public ResourceLocation getTextureResource(T animatable) {
		return texturePath;
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return animationsPath;
	}

	@Override
	public BakedGeoModel getBakedModel(ResourceLocation location) {
		BakedGeoModel model = GeckoLibCache.getBakedModels().get(location);

		if (model == null) {
			throw GeckoLibConstants.exception(location, "Unable to find model");
		}

		if (model != getCurrentModel()) {
			getAnimationProcessor().setActiveModel(model);
			setCurrentModel(model);
		}

		return getCurrentModel();
	}

	protected BakedGeoModel getCurrentModel() {
		return ((GeoModelAccessorMixin) (this)).goldenboughs_lib$getCurrentModel();
	}

	protected void setCurrentModel(BakedGeoModel model) {
		((GeoModelAccessorMixin) (this)).goldenboughs_lib$setCurrentModel(model);
	}
}
