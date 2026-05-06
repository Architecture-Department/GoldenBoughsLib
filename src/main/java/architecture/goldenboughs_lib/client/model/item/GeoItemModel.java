package architecture.goldenboughs_lib.client.model.item;

import architecture.goldenboughs_lib.client.model.GeoModelExpand;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GeoItemModel<T extends GeoAnimatable> extends GeoModelExpand<T> {
	public GeoItemModel(ResourceLocation pathName) {
		this(pathName, pathName, pathName);
	}

	public GeoItemModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(model(getPath(modelPath)), texturePath(getPath(textureName)), animationsPath(getPath(animationsName)));
	}

	public static ResourceLocation getPath(ResourceLocation modelPath) {
		return modelPath.withPrefix("item/");
	}
}
