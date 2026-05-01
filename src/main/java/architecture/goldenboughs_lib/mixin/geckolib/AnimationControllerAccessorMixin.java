package architecture.goldenboughs_lib.mixin.geckolib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Map;

@Mixin(AnimationController.class)
public interface AnimationControllerAccessorMixin {
	@Accessor("triggerableAnimations")
	Map<String, RawAnimation> goldenboughs_lib$getTriggerableAnimations();
}
