package architecture.goldenboughs_lib.client.model;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.model.GeoModel;

public abstract class BasicGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
	public final ResourceLocation modelPath;
	public final ResourceLocation texturePath;
	public final ResourceLocation animationsPath;

	public BasicGeoModel(ResourceLocation pathName) {
		this(pathName, pathName, pathName);
	}

	public BasicGeoModel(ResourceLocation modelPath, ResourceLocation texturePath, ResourceLocation animationsPath) {
		this.modelPath = model(modelPath);
		this.texturePath = texturePath(texturePath);
		this.animationsPath = animationsPath(animationsPath);
	}

	public static ResourceLocation model(ResourceLocation path) {
		return path.withPrefix("geo/").withSuffix(".geo.json");
	}

	public static ResourceLocation texturePath(ResourceLocation path) {
		return path.withPrefix("textures/geo/").withSuffix(".png");
	}

	public static ResourceLocation animationsPath(ResourceLocation path) {
		return path.withPrefix("animations/").withSuffix(".animation.json");
	}

	@Override
	public ResourceLocation getModelResource(T animatable) {
		return GeckoLibCache.getBakedModels().get(this.modelPath) == null ? getDefaultModelResource() : this.modelPath;
	}

	@NotNull
	protected ResourceLocation getDefaultModelResource() {
		return model(GoldenBoughsLib.modRl("item/default"));
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
