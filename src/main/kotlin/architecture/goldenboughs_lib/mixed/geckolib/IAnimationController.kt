package architecture.goldenboughs_lib.mixed.geckolib

import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.animation.AnimationController

interface IAnimationController<T : GeoAnimatable> {
	/**
	 * 如果 lockBones 和 enabledBones 都不为空则先判断 enabledBones 在判断 lockBones
	 *
	 * @return 该骨骼是否可以影响影响
	 */
	fun `goldenboughs_lib$isInfluence`(boneName: String): Boolean

	fun `goldenboughs_lib$lockBones`(vararg boneNames: String): AnimationController<T>

	fun `goldenboughs_lib$removeLockBones`(vararg boneNames: String): AnimationController<T>

	fun `goldenboughs_lib$enabledBones`(vararg boneNames: String): AnimationController<T>

	fun `goldenboughs_lib$removeEnabledBones`(vararg boneNames: String): AnimationController<T>

	fun `goldenboughs_lib$isBoneExist`(boneName: String): Boolean

	companion object {
		@JvmStatic
		fun <T : GeoAnimatable> of(animatable: AnimationController<T>): IAnimationController<T> {
			return animatable as IAnimationController<T>
		}
	}
}
