package architecture.goldenboughs_lib.client.model.entity;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GeoEntityModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
	public GeoEntityModel(ResourceLocation name) {
		super(getPath(name));
	}

	public GeoEntityModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(getPath(modelPath), getPath(textureName), getPath(animationsName));
	}

	public static ResourceLocation getPath(ResourceLocation location) {
		return location.withPrefix("entity/");
	}
}
