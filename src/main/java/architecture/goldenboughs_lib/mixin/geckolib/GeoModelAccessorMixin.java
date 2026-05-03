package architecture.goldenboughs_lib.mixin.geckolib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

@Mixin(GeoModel.class)
public interface GeoModelAccessorMixin {
	@Accessor("currentModel")
	BakedGeoModel goldenboughs_lib$getCurrentModel();

	@Accessor("currentModel")
	void goldenboughs_lib$setCurrentModel(BakedGeoModel model);
}
