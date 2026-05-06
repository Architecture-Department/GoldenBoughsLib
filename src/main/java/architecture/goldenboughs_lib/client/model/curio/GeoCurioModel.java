package architecture.goldenboughs_lib.client.model.curio;

import architecture.goldenboughs_lib.client.model.GeoModelExpand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GeoCurioModel<T extends Item & GeoAnimatable> extends GeoModelExpand<T> {

	public GeoCurioModel(ResourceLocation pathName) {
		this(pathName, pathName, pathName);
	}

	public GeoCurioModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(model(getPath(modelPath)), texturePath(getPath(textureName)), animationsPath(getPath(animationsName)));
	}

	public static ResourceLocation getPath(ResourceLocation pathName) {
		return pathName.withPrefix("curio/");
	}
}
