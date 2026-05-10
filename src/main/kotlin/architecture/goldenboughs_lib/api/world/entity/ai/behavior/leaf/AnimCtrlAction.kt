package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ISharedFlagControllerHolder
import architecture.goldenboughs_lib.api.world.entity.SharedFlagController
import software.bernie.geckolib.animatable.GeoEntity

/**
 * Geo 动画控制器动作 - 触发动画并将状态绑定到共享标志位，用于客户端同步
 * 
 * 继承自 [SyncFlagAction]，在服务端触发动画的同时同步标志位到客户端
 * 
 * @param <T> 实体类型，需同时实现 [GeoEntity] 和 [ISharedFlagControllerHolder]
</T> */
class AnimCtrlAction<T>(
	entity: T,
	val controllerName: String,
	val animationName: String,
	sharedFlag: SharedFlagController.SharedFlag,
	isEnable: Boolean
) : SyncFlagAction<T>(entity, sharedFlag, isEnable)
	where T : GeoEntity, T : ISharedFlagControllerHolder {
	override fun start() {
		super.start()
		if (isEnable) {
			entity!!.triggerAnim(controllerName, animationName)
		}
	}
}
