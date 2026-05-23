package architecture.goldenboughs_lib.mixin.gecko_lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

@Mixin(GeoModel.class)
public interface GeoModelAccessor {
	@Accessor
	BakedGeoModel getCurrentModel();

	@Accessor
	void setCurrentModel(BakedGeoModel model);
}
