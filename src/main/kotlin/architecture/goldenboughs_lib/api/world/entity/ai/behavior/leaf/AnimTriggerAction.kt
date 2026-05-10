package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import software.bernie.geckolib.animatable.GeoEntity

/**
 * 动画触发动作 - 仅在服务端触发动画，不需要同步状态到客户端
 * 
 * 适用于纯服务端控制的动画播放
 */
class AnimTriggerAction(
	val entity: GeoEntity,
	val controllerName: String,
	val animationName: String
) : BTNode() {
	override fun execute(): BTStatus? {
		return BTStatus.SUCCESS
	}

	override fun start() {
		super.start()
		entity.triggerAnim(controllerName, animationName)
	}
}
