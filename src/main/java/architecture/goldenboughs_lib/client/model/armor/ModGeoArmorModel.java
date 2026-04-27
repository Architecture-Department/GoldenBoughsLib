package architecture.goldenboughs_lib.client.model.armor;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class ModGeoArmorModel<T extends GeoAnimatable> extends BasicGeoModel<T> {

	public ModGeoArmorModel(String name) {
		this(name, name, name);
	}

	public ModGeoArmorModel(String modelPath, String textureName, String animationsName) {
		super("armor/" + modelPath, "armor/" + textureName, "armor/" + animationsName);
	}

	@NotNull
	@Override
	protected ResourceLocation getDefaultModelResource() {
		return modelPath("armor/default");
	}
}
