package architecture.goldenboughs_lib.mixin.geckolib;

import architecture.goldenboughs_lib.mixed.geckolib.IAnimationController;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Mixin(AnimationController.class)
public abstract class AnimationControllerMixin<T extends GeoAnimatable> implements IAnimationController<T> {
	@Shadow
	protected GeoModel<T> lastModel;
	/**
	 * 锁定的骨骼，类似黑名单
	 */
	@Unique
	protected final Set<String> goldenboughs_lib$lockedBones = new ObjectOpenHashSet<>();
	/**
	 * 启用的骨骼，类似白名单
	 */
	@Unique
	protected final Set<String> goldenboughs_lib$enabledBones = new ObjectOpenHashSet<>();
	@Unique
	protected final Map<String, GeoBone> goldenboughs_lib$bones = new Object2ObjectOpenHashMap<>();

	@Inject(method = "process", at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/animation/AnimationController;createInitialQueues(Ljava/util/Collection;)V"))
	public void goldenboughs_lib$processAnimation(GeoModel<T> model, AnimationState<T> state, Map<String, GeoBone> bones, Map<String, BoneSnapshot> snapshots, double seekTime, boolean crashWhenCantFindBone, CallbackInfo ci) {

	}

	@Inject(method = "process", at = @At("HEAD"))
	public void goldenboughs_lib$process(GeoModel<T> model, AnimationState<T> state, Map<String, GeoBone> bones, Map<String, BoneSnapshot> snapshots, double seekTime, boolean crashWhenCantFindBone, CallbackInfo ci) {
		goldenboughs_lib$bones.clear();
		goldenboughs_lib$bones.putAll(bones);
	}

	@Override
	public boolean goldenboughs_lib$isInfluence(String boneName) {
		if (!goldenboughs_lib$enabledBones.isEmpty()) {
			return !goldenboughs_lib$lockedBones.contains(boneName) && goldenboughs_lib$enabledBones.contains(boneName);
		}
		return !goldenboughs_lib$lockedBones.contains(boneName);
	}

	@Override
	public AnimationController<T> goldenboughs_lib$lockBones(String... boneNames) {
		goldenboughs_lib$lockedBones.addAll(Arrays.asList(boneNames));
		return goldenboughs_lib$getThis();
	}

	@Override
	public AnimationController<T> goldenboughs_lib$removeLockBones(String... boneNames) {
		Arrays.stream(boneNames).forEach(goldenboughs_lib$lockedBones::remove);
		return goldenboughs_lib$getThis();
	}

	@Override
	public AnimationController<T> goldenboughs_lib$enabledBones(String... boneNames) {
		goldenboughs_lib$enabledBones.addAll(Arrays.asList(boneNames));
		return goldenboughs_lib$getThis();
	}

	@Override
	public AnimationController<T> goldenboughs_lib$removeEnabledBones(String... boneNames) {
		Arrays.stream(boneNames).forEach(goldenboughs_lib$enabledBones::remove);
		return goldenboughs_lib$getThis();
	}

	@Override
	public boolean goldenboughs_lib$isBoneExist(String boneName) {
		return lastModel != null && lastModel.getBone(boneName).isPresent();
	}

	@Unique
	private AnimationController<T> goldenboughs_lib$getThis() {
		//noinspection unchecked
		return (AnimationController<T>) (Object) this;
	}
}
