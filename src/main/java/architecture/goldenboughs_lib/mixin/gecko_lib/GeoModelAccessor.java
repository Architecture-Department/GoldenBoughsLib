package architecture.goldenboughs_lib.mixin.gecko_lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

@Mixin(GeoModel.class)
public interface GeoModelAccessor {
	@Accessor("currentModel")
	BakedGeoModel goldenboughs_lib$getCurrentModel();

	@Accessor("currentModel")
	void goldenboughs_lib$setCurrentModel(BakedGeoModel model);
}
