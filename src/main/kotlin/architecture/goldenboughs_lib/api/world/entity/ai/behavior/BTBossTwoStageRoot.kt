package architecture.goldenboughs_lib.api.world.entity.ai.behavior

import architecture.goldenboughs_lib.api.world.entity.IStateChangeableMob
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard.Blackboard
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard.IBlackboardHolder
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard.KeyType
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.SequenceNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.SyncAction
import net.minecraft.world.entity.PathfinderMob
import java.util.function.Supplier

/**
 * 两个阶段的怪物AI
 */
abstract class BTBossTwoStageRoot<T>(mob: T) :
	BTCommonRoot<T>(mob) where T : PathfinderMob, T : IBlackboardHolder, T : IStateChangeableMob {
	override fun createStageTrigger(): BTNode {
		return BTFactory.selector() // 二阶段
			.addWithCondition(
				Blackboard.containsValue(this.mob, KeyType.STAGE) { v: Int -> v == 3 }
					.setConDesc("STAGE == 3"), BTFactory.wait(10000).setDescription("二阶段")
			) // 转换阶段
			.addWithCondition(
				Blackboard.containsValue(this.mob, KeyType.STAGE) { v: Int -> v == 2 }
					.setConDesc("STAGE == 2"),
				switchPost(
					switchPre(BTFactory.sequence())
						.addChild(
							SyncAction(
								this.mob,
								this.mob.dataStatusStatus,
								{ 3 }).setDescription("sync status = 3")
						)
				).addChild(Blackboard.setValue(this.mob, KeyType.STAGE) { 3 }.setDescription("STAGE = 3"))
					.setDescription("转换阶段")
			) // 一阶段
			.addWithCondition(
				ConditionBT.and(
					this.createStageCondition(),
					Blackboard.containsValue(this.mob, KeyType.STAGE) { v: Int -> v == 1 }
						.setConDesc("STAGE == 1")
				), BTFactory.sequence()
					.addChild(Blackboard.setValue(this.mob, KeyType.STAGE) { 2 }.setDescription("STAGE = 2"))
					.addChild(
						SyncAction(
							this.mob,
							this.mob.dataStatusStatus,
							Supplier { 2 }).setDescription("sync status = 2")
					)
					.setDescription("一阶段")
			)
	}

	/**
	 * 转换二阶段条件
	 */
	protected abstract fun createStageCondition(): ConditionBT

	/**
	 * 转换阶段前，可以添加延迟和共享状态位
	 */
	protected abstract fun switchPre(sequence: SequenceNode): SequenceNode

	/**
	 * 转换阶段后，可以添加延迟和共享状态位
	 */
	protected abstract fun switchPost(sequence: SequenceNode): SequenceNode

	override fun createAttackBehavior(): BTNode {
		return BTFactory.selector() // 一阶段
			.addWithCondition(
				Blackboard.containsValue<Int>(this.mob, KeyType.STAGE) { v: Int -> v == 1 },
				"STAGE == 1",
				BTFactory.infinite(this.createStageOneAttack().setDescription("一阶段AI"))
			) // 二阶段
			.addWithCondition(
				Blackboard.containsValue<Int>(this.mob, KeyType.STAGE) { v: Int -> v == 3 },
				"STAGE == 3",
				BTFactory.infinite(this.createStageTwoAttack().setDescription("二阶段AI"))
			)
			.setDescription("阶段选择器")
	}

	/**
	 * 一阶段AI
	 */
	protected abstract fun createStageOneAttack(): BTNode

	/**
	 * 二阶段AI
	 */
	protected abstract fun createStageTwoAttack(): BTNode
}
