package architecture.goldenboughs_lib.client.model.entity;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GeoEntityModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
	public GeoEntityModel(ResourceLocation name) {
		super(name.withPrefix("entity/"));
	}

	public GeoEntityModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(modelPath.withPrefix("entity/"), textureName.withPrefix("entity/"), animationsName.withPrefix("entity/"));
	}

	public static ResourceLocation getTexturePath(ResourceLocation textureName) {
		return texturePath(textureName.withPrefix("entity/"));
	}
}
