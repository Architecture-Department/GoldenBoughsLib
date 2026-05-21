package architecture.goldenboughs_lib.mixin.gecko_lib;

import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.animation.AnimationProcessor;

@Mixin(AnimationProcessor.class)
public abstract class AnimationProcessorMixin {

//	@ModifyExpressionValue(method = "tickAnimation", at = @At(value = "INVOKE", ordinal = 1, target = "Ljava/util/Map;values()Ljava/util/Collection;"))
//	private <V extends BoneAnimationQueue, T extends GeoAnimatable> Collection<V> tickAnimation(Collection<V> original, @Local(name = "controller") AnimationController<T> controller) {
//		IAnimationController<T> iController = IAnimationController.of(controller);
//		return original.stream()
//			.filter(bones -> iController.goldenboughs_lib$isInfluence(bones.bone().getName()))
//			.collect(Collectors.toSet());
//	}
}
