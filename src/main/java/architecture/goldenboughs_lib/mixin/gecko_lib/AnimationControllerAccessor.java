package architecture.goldenboughs_lib.mixin.gecko_lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Map;
import java.util.function.Function;

@Mixin(AnimationController.class)
public interface AnimationControllerAccessor<T extends GeoAnimatable> {

	@Accessor
	Function<T, EasingType> getOverrideEasingTypeFunction();

	@Accessor
	void setIsJustStarting(boolean isJustStarting);

	@Accessor
	Map<String, RawAnimation> getTriggerableAnimations();
}