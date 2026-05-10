package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ISharedFlagControllerHolder
import architecture.goldenboughs_lib.api.world.entity.SharedFlagController
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 同步共享标志位动作 - 设置实体的共享状态标志
 * 
 * 用于控制客户端可访问的共享状态，如动画播放、特效显示等
 * 
 * @param <T> 实体类型，需实现 [ISharedFlagControllerHolder]
</T> */
class SyncFlagAction<T : ISharedFlagControllerHolder>(
	protected val entity: T,
	protected val sharedFlag: SharedFlagController.SharedFlag,
	protected val isEnable: Boolean
) : BTNode() {
	override fun execute(): BTStatus? {
		return BTStatus.SUCCESS
	}

	override fun start() {
		super.start()
		entity.sharedFlagController.setFlag(sharedFlag, isEnable)
	}
}
