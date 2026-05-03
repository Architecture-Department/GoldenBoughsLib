package architecture.goldenboughs_lib.mixin.geckolib;

import architecture.goldenboughs_lib.mixed.geckolib.IAnimationController;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.keyframe.BoneAnimationQueue;

import java.util.Collection;
import java.util.stream.Collectors;

@Mixin(AnimationProcessor.class)
public abstract class AnimationProcessorMixin {

	@ModifyExpressionValue(method = "tickAnimation", at = @At(value = "INVOKE", ordinal = 1, target = "Ljava/util/Map;values()Ljava/util/Collection;"))
	private <V extends BoneAnimationQueue, T extends GeoAnimatable> Collection<V> tickAnimation(Collection<V> original, @Local(name = "controller") AnimationController<T> controller) {
		IAnimationController<T> iController = IAnimationController.of(controller);
		return original.stream()
			.filter(bones -> iController.goldenboughs_lib$isInfluence(bones.bone().getName()))
			.collect(Collectors.toSet());
	}
}
