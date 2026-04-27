package architecture.goldenboughs_lib.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * 盔甲渲染提供程序
 */
public class GeoArmourRenderProvider<T extends Item & GeoItem> implements GeoRenderProvider {
	protected final GeoModel<T> model;
	private final Function<GeoModel<T>, GeoArmorRenderer<T>> rendererFunction;
	private @Nullable GeoArmorRenderer<T> renderer;

	public GeoArmourRenderProvider(GeoModel<T> model, Function<GeoModel<T>, GeoArmorRenderer<T>> rendererFunction) {
		this.model = model;
		this.rendererFunction = rendererFunction;
	}

	public GeoArmourRenderProvider(GeoModel<T> model) {
		this.model = model;
		this.rendererFunction = RoughAndFineArmorRenderer::new;
	}

	@Nullable
	@Override
	public <E extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable E livingEntity, ItemStack itemStack,
	                                                                     @Nullable EquipmentSlot equipmentSlot,
	                                                                     @Nullable HumanoidModel<E> original) {

		if (this.renderer == null) {
			this.renderer = rendererFunction.apply(this.model);
		}
		return this.renderer;
	}
}
