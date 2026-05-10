package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.PathfinderMob

/**
 * 接近目标动作 - 实体使用寻路系统接近目标直到进入触发距离
 * 
 * 支持设置触发距离和寻路间隔，当与目标距离小于等于触发距离时返回成功
 */
class MoveToTargetAction(
	var mob: PathfinderMob,
	val triggerDistance: Int,
	val _findInterval: Int
) : BTNode() {
	var findInterval: Int = 0

	override fun canContinueToUse(): Boolean {
		return super.canContinueToUse() && mob.target != null
	}

	/**
	 * 执行动作
	 *
	 * @return
	 *
	 *SUCCESS：进入下一个执行任务
	 *
	 * RUNNING：继续执行当前任务
	 *
	 * FAILURE：任务失败
	 */
	override fun execute(): BTStatus? {
		val target = mob.target ?: return BTStatus.FAILURE

		if (--this.findInterval <= 0) {
			mob.getNavigation().moveTo(target, 1.0)
			this.findInterval = _findInterval
		}

		val distance = mob.distanceTo(target)
		if (distance <= triggerDistance) {
			return BTStatus.SUCCESS
		}
		return BTStatus.RUNNING
	}
}
