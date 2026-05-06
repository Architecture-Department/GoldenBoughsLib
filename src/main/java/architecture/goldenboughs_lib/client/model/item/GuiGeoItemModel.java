package architecture.goldenboughs_lib.client.model.item;

import architecture.goldenboughs_lib.client.model.GeoModelExpand;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GuiGeoItemModel<T extends GeoAnimatable> extends GeoItemModel<T> {
	private static final ResourceLocation MODELLED_PATH = GeoModelExpand.model(GoldenBoughsLib.modRl("item/gui_item_model")).withPrefix(MODEL_SUFFIX);
	private final ResourceLocation resourceLocation;

	public GuiGeoItemModel(ResourceLocation path) {
		super(path);
		this.resourceLocation = path.withPrefix("textures/item/").withSuffix(".png");
	}

	public GuiGeoItemModel(ResourceLocation modelPath, ResourceLocation textureName, ResourceLocation animationsName) {
		super(modelPath, textureName, animationsName);
		this.resourceLocation = textureName.withPrefix("textures/item/").withSuffix(".png");
	}

	@Override
	public ResourceLocation getModelResource(T animatable) {
		return MODELLED_PATH;
	}

	@Override
	public ResourceLocation getTextureResource(T animatable) {
		return resourceLocation;
	}
}
