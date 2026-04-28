package architecture.goldenboughs_lib.client.model.item;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GeoItemModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
	public GeoItemModel(ResourceLocation name) {
		super(name.withPrefix("item/"));
	}

	public GeoItemModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(modelPath.withPrefix("item/"), textureName.withPrefix("item/"), animationsName.withPrefix("item/"));
	}
}
