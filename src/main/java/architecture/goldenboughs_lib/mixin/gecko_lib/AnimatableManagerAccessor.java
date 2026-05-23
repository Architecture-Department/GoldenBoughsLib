package architecture.goldenboughs_lib.mixin.gecko_lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;

@Mixin(AnimatableManager.class)
public interface AnimatableManagerAccessor<T extends GeoAnimatable> {
	@Invoker
	void callFinishFirstTick();
}