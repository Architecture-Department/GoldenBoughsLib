package architecture.goldenboughs_lib.api.world.entity.ai.behavior

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.ParallelNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.TargetExistCondition
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.RandomStrollAction
import net.minecraft.world.entity.PathfinderMob

/**
 * 带阶段的AI
 */
abstract class BTCommonRoot<T : PathfinderMob>(mob: T) : BTRoot<T>(mob) {
	override fun createBehaviorTree(): BTNode {
		return BTFactory.parallel(ParallelNode.Policy.REQUIRE_ALL, ParallelNode.Policy.REQUIRE_ALL) // 阶段触发器
			.addChild(BTFactory.infinite(this.createStageTrigger().setDescription("阶段触发器"))) // AI
			.addChild(
				BTFactory.infinite(
					BTFactory.selector() // 游走
						.addWithCondition(
							ConditionBT.not(TargetExistCondition(mob)),
							BTFactory.infinite(this.createWonderBehavior())
						) // 攻击
						.addWithCondition(TargetExistCondition(mob), this.createAttackBehavior())
						.setDescription("AI")
				)
			)
	}

	/**
	 * 创建阶段触发器
	 * 
	 * 一般用于阶段切换，也可以用做默认的并行行为
	 */
	protected abstract fun createStageTrigger(): BTNode

	/**
	 * 创建攻击行为
	 */
	protected abstract fun createAttackBehavior(): BTNode

	protected fun createWonderBehavior(): BTNode {
		return BTFactory.sequence()
			.addChild(RandomStrollAction(mob, 2.0, 70))
	}

	override fun tick() {
		super.tick()
	}
}
