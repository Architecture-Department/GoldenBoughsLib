package architecture.goldenboughs_lib.client.model.entity;

import architecture.goldenboughs_lib.client.model.GeoModelExpand;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GeoEntityModel<T extends GeoAnimatable> extends GeoModelExpand<T> {
	public GeoEntityModel(ResourceLocation pathName) {
		this(pathName, pathName, pathName);
	}

	public GeoEntityModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(model(getPath(modelPath)), texturePath(getPath(textureName)), animationsPath(getPath(animationsName)));
	}

	public static ResourceLocation getPath(ResourceLocation location) {
		return location.withPrefix("entity/");
	}
}
