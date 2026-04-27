package architecture.goldenboughs_lib.client.model.item;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class ModGeoItemModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
	public ModGeoItemModel(String name) {
		this(name, name, name);
	}

	public ModGeoItemModel(String modelPath, String textureName, String animationsName) {
		super("item/" + modelPath, "item/" + textureName, "item/" + animationsName);
	}
}
