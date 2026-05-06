package architecture.goldenboughs_lib.client.model;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.model.GeoModel;

public class GeoModelExpand<T extends GeoAnimatable> extends GeoModel<T> {
	public static final String MODEL_SUFFIX = ".geo.json";
	public static final String TEXTURE_SUFFIX = ".png";
	public static final String ANIMATIONS_SUFFIX = ".animation.json";
	public static final ResourceLocation ITEM_DEFAULT_MODEL = model(GoldenBoughsLib.modRl("item/default")).withSuffix(MODEL_SUFFIX);
	public final ResourceLocation modelPath;
	public final ResourceLocation texturePath;
	public final ResourceLocation animationsPath;

	public GeoModelExpand(ResourceLocation modelPath, ResourceLocation texturePath, ResourceLocation animationsPath) {
		this.modelPath = modelPath.withSuffix(MODEL_SUFFIX);
		this.texturePath = texturePath.withSuffix(TEXTURE_SUFFIX);
		this.animationsPath = animationsPath.withSuffix(ANIMATIONS_SUFFIX);
	}

	public static ResourceLocation model(ResourceLocation path) {
		return path.withPrefix("geo/");
	}

	public static ResourceLocation texturePath(ResourceLocation path) {
		return path.withPrefix("textures/geo/");
	}

	public static ResourceLocation animationsPath(ResourceLocation path) {
		return path.withPrefix("animations/");
	}

	@Override
	public ResourceLocation getModelResource(T animatable) {
		return GeckoLibCache.getBakedModels().get(this.modelPath) == null ? getDefaultModelResource() : this.modelPath;
	}

	@NotNull
	protected ResourceLocation getDefaultModelResource() {
		return ITEM_DEFAULT_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(T animatable) {
		return texturePath;
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return animationsPath;
	}
}
