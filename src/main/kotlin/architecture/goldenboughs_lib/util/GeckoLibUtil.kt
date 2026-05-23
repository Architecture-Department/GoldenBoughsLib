@file:Suppress("UNCHECKED_CAST")

package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.mixin.gecko_lib.AnimatableManagerAccessor
import architecture.goldenboughs_lib.mixin.gecko_lib.AnimationControllerAccessor
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.EasingType
import software.bernie.geckolib.animation.RawAnimation
import java.util.function.Function

fun <T : GeoAnimatable> AnimatableManager<T>.callFinishFirstTick() {
	(this as AnimatableManagerAccessor<T>).callFinishFirstTick()
}

fun <T : GeoAnimatable> AnimationController<T>.getOverrideEasingTypeFunction(): Function<T, EasingType> {
	return (this as AnimationControllerAccessor<T>).getOverrideEasingTypeFunction()
}

fun <T : GeoAnimatable> AnimationController<T>.setIsJustStarting(isJustStarting: Boolean) {
	(this as AnimationControllerAccessor<T>).setIsJustStarting(isJustStarting)
}

fun <T : GeoAnimatable> AnimationController<T>.getTriggerableAnimations(): Map<String, RawAnimation> {
	return (this as AnimationControllerAccessor<T>).getTriggerableAnimations()
}