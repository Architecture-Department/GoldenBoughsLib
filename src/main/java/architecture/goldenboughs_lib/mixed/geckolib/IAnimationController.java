package architecture.goldenboughs_lib.mixed.geckolib;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;

public interface IAnimationController<T extends GeoAnimatable> {
	static <T extends GeoAnimatable> IAnimationController<T> of(AnimationController<T> animatable) {
		//noinspection unchecked
		return (IAnimationController<T>) animatable;
	}

	/**
	 * 如果 lockBones 和 enabledBones 都不为空则先判断 enabledBones 在判断 lockBones
	 *
	 * @return 该骨骼是否可以影响影响
	 */
	boolean goldenboughs_lib$isInfluence(String boneName);

	AnimationController<T> goldenboughs_lib$lockBones(String... boneNames);

	AnimationController<T> goldenboughs_lib$removeLockBones(String... boneNames);

	AnimationController<T> goldenboughs_lib$enabledBones(String... boneNames);

	AnimationController<T> goldenboughs_lib$removeEnabledBones(String... boneNames);

	boolean goldenboughs_lib$isBoneExist(String boneName);
}
