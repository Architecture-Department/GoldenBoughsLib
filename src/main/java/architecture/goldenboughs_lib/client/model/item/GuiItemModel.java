package architecture.goldenboughs_lib.client.model.item;

import architecture.goldenboughs_lib.client.model.BasicGeoModel;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class GuiItemModel<T extends GeoAnimatable> extends ModGeoItemModel<T> {
	private static final ResourceLocation MODELLED_PATH = BasicGeoModel.modelPath("item/gui_item_model");
	private final ResourceLocation resourceLocation;

	public GuiItemModel(String path) {
		super(path);
		this.resourceLocation = GoldenBoughsLib.modRl("textures/item/" + path + ".png");
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
