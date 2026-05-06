package architecture.goldenboughs_lib.client.model.armor;

import architecture.goldenboughs_lib.client.model.GeoModelExpand;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class ModGeoArmorModel<T extends GeoAnimatable> extends GeoModelExpand<T> {

	public static final ResourceLocation DEFAULT_MODEL = model(GoldenBoughsLib.modRl("armor/default")).withSuffix(MODEL_SUFFIX);

	public ModGeoArmorModel(ResourceLocation pathName) {
		this(pathName, pathName, pathName);
	}

	public ModGeoArmorModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(model(getPath(modelPath)), texturePath(getPath(textureName)), animationsPath(getPath(animationsName)));
	}

	public static ResourceLocation getPath(ResourceLocation name) {
		return name.withPrefix("armor/");
	}

	@NotNull
	@Override
	protected ResourceLocation getDefaultModelResource() {
		return DEFAULT_MODEL;
	}
}
