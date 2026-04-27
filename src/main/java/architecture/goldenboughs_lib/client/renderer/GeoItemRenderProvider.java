package architecture.goldenboughs_lib.client.renderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.BiFunction;

/**
 * 物品渲染提供程序
 */
public class GeoItemRenderProvider<T extends Item & GeoItem> implements GeoRenderProvider {
	protected final GeoModel<T> defaultModel;
	protected final @Nullable GeoModel<T> guiModel;
	private final BiFunction<GeoModel<T>, GeoModel<T>, GeoItemRenderer<T>> rendererFunction;
	private GeoItemRenderer<T> renderer;

	public GeoItemRenderProvider(GeoModel<T> defaultModel, @Nullable GeoModel<T> guiModel) {
		this(defaultModel, guiModel, GeoItemRendererExpand::new);
	}

	public GeoItemRenderProvider(GeoModel<T> defaultModel, @Nullable GeoModel<T> guiModel, BiFunction<GeoModel<T>, GeoModel<T>, GeoItemRenderer<T>> rendererFunction) {
		this.defaultModel = defaultModel;
		this.guiModel = guiModel;
		this.rendererFunction = rendererFunction;
	}

	@Override
	public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
		if (this.renderer == null) {
			this.renderer = rendererFunction.apply(this.defaultModel, this.guiModel);
		}
		return this.renderer;
	}
}
