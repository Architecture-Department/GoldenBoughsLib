package architecture.goldenboughs_lib.client.model.entity;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class ModGeoEntityModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
	public ModGeoEntityModel(String name) {
		this(name, name, name);
	}

	public ModGeoEntityModel(String modelPath, String textureName, String animationsName) {
		super("entity/" + modelPath, "entity/" + textureName, "entity/" + animationsName);
	}

	public static ResourceLocation getTexturePath(String textureName) {
		return texturePath("entity/" + textureName);
	}
}
