package architecture.goldenboughs_lib.mixin.gecko_lib;

import architecture.goldenboughs_lib.mixed.geckolib.IAnimationController;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(AnimationController.class)
public abstract class AnimationControllerMixin<T extends GeoAnimatable> implements IAnimationController<T> {
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
	@Shadow
	protected GeoModel<T> lastModel;

	@Unique
	private static <T extends GeoAnimatable> Collection<GeoBone> goldenboughs_lib$getGeoBonesCollect(Collection<GeoBone> geoBones, AnimationController<T> instance) {
		IAnimationController<T> iController = IAnimationController.of(instance);
		return geoBones.stream()
			.filter(bones -> iController.goldenboughs_lib$isInfluence(bones.getName()))
			.collect(Collectors.toSet());
	}

	@Unique
	private static <T extends GeoAnimatable> Map<String, BoneSnapshot> goldenboughs_lib$getStringBoneSnapshotMap(Map<String, BoneSnapshot> stringBoneSnapshotMap, AnimationController<T> instance) {
		IAnimationController<T> iController = IAnimationController.of(instance);
		return stringBoneSnapshotMap.entrySet().stream()
			.filter(bones -> iController.goldenboughs_lib$isInfluence(bones.getKey()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@WrapOperation(method = "process", at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/animation/AnimationController;createInitialQueues(Ljava/util/Collection;)V"))
	public void goldenboughs_lib$process$createInitialQueues(AnimationController<T> instance, Collection<GeoBone> geoBones, Operation<Void> original) {
		original.call(instance, goldenboughs_lib$getGeoBonesCollect(geoBones, instance));
	}

	@WrapOperation(method = "process", at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/animation/AnimationController;saveSnapshotsForAnimation(Lsoftware/bernie/geckolib/animation/AnimationProcessor$QueuedAnimation;Ljava/util/Map;)V"))
	public void goldenboughs_lib$process$saveSnapshotsForAnimation(AnimationController<T> instance, AnimationProcessor.QueuedAnimation snapshot, Map<String, BoneSnapshot> stringBoneSnapshotMap, Operation<Void> original) {
		original.call(instance, snapshot, goldenboughs_lib$getStringBoneSnapshotMap(stringBoneSnapshotMap, instance));
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
