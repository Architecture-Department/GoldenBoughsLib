package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.ai.goal.Goal
import java.util.*

/**
 * 目标包装器 - 将现有的 Goal 包装为行为树节点
 * 
 * **警告：**此实现可能存在兼容性问题，极不推荐使用
 * 
 * 仅用于临时迁移或测试目的
 */
class GoalWrapper(private val goal: Goal) : BTNode() {
	override fun execute(): BTStatus {
//        if (!goal.canUse()) {
//            return BTStatus.FAILURE;
//        }

		if (goal.canContinueToUse()) {
			goal.tick()
			return BTStatus.RUNNING
		}

		return BTStatus.SUCCESS
	}

	override fun canUse(): Boolean {
		return super.canUse()
	}

	override val isReady: Boolean
		get() = super.isReady && goal.canUse()

	override fun canContinueToUse(): Boolean {
		return super.canContinueToUse()
	}

	override fun start() {
		goal.start()
		super.start()
	}

	override fun stop() {
		goal.stop()
		super.stop()
	}

	override fun setFlags(flagSet: EnumSet<Flag>) {
		goal.setFlags(flagSet)
	}
}
