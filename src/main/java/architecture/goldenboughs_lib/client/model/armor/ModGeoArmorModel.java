package architecture.goldenboughs_lib.client.model.armor;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class ModGeoArmorModel<T extends GeoAnimatable> extends BasicGeoModel<T> {

	public ModGeoArmorModel(ResourceLocation name) {
		super(name.withPrefix("armor/"));
	}

	public ModGeoArmorModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(modelPath.withPrefix("armor/"), textureName.withPrefix("armor/"), animationsName.withPrefix("armor/"));
	}

	@NotNull
	@Override
	protected ResourceLocation getDefaultModelResource() {
		return modelPath(GoldenBoughsLib.modRl("armor/default"));
	}
}
