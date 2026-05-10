package architecture.goldenboughs_lib.api.world.entity.ai.behavior

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.ParallelNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.SelectorNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.SequenceNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration.ConditionNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration.InverterNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration.RepeaterNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.GoalWrapper
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.RandomWaitAction
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.WaitAction
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.ai.goal.Goal

/**
 * 行为树工厂
 */
object BTFactory {
	/**
	 * 创建一个行为树
	 * 
	 * @return 一个行为树
	 */
	@JvmStatic
	fun sequence(): SequenceNode {
		return SequenceNode()
	}

	/**
	 * 创建一个选择行为树
	 * 
	 * @return 一个选择行为树
	 */
	@JvmStatic
	fun selector(): SelectorNode {
		return SelectorNode()
	}

	/**
	 * 创建一个反转行为树
	 * 
	 * @param child 子行为树
	 * @return 一个反转行为树
	 */
	@JvmStatic
	fun inverter(child: BTNode): InverterNode {
		return InverterNode(child)
	}

	/**
	 * 创建一个重复行为树
	 * 
	 * @param count 重复次数
	 * @param child 子行为树
	 * @return 一个重复行为树
	 */
	@JvmStatic
	fun repeater(count: Int, child: BTNode): RepeaterNode {
		return RepeaterNode(count, child)
	}

	/**
	 * 创建一个无限重复行为树
	 * 
	 * @param child 子行为树
	 * @return 一个无限重复行为树
	 */
	@JvmStatic
	fun infinite(child: BTNode): RepeaterNode {
		return RepeaterNode(-1, child)
	}

	/**
	 * 创建一个条件行为树
	 * 
	 * @param condition 条件
	 * @param child     子行为树
	 * @return 一个条件行为树
	 */
	@JvmStatic
	fun condition(condition: ConditionBT, child: BTNode): ConditionNode {
		return ConditionNode(condition, child)
	}


	/**
	 * 创建一个随机等待行为树
	 * 
	 * @param minInclusive 最小等待时间（包含）
	 * @param maxInclusive 最大等待时间（包含）
	 * @param random       随机数生成器
	 * @return 一个随机等待行为树
	 */
	@JvmStatic
	@JvmOverloads
	fun waitRandom(minInclusive: Int, maxInclusive: Int, random: RandomSource = RandomSource.create()): BTNode {
		return RandomWaitAction(minInclusive, maxInclusive, random)
	}

	/**
	 * 创建一个成功行为树
	 * 
	 * @param runnable 成功时执行的代码
	 * @return 一个成功行为树
	 */
	@JvmStatic
	fun success(runnable: Runnable): BTNode {
		return object : BTNode() {
			override fun execute(): BTStatus {
				runnable.run()
				return BTStatus.SUCCESS
			}
		}
	}

	/**
	 * 创建一个无限等待行为树
	 * 
	 * @return 一个无限等待行为树
	 */
	@JvmStatic
	fun waitForever(): BTNode {
		return object : BTNode() {
			override fun execute(): BTStatus {
				return BTStatus.RUNNING
			}
		}
	}

	/**
	 * 创建一个带计时器的并行行为树
	 * 
	 * @param duration 计时器时长
	 * @param node     子行为树
	 * @return 一个带计时器的并行行为树
	 */
	@JvmStatic
	fun withTimer(duration: Int, node: BTNode): ParallelNode {
		return parallel(ParallelNode.Policy.REQUIRE_ONE, ParallelNode.Policy.REQUIRE_ONE)
			.addChild(wait(duration))
			.addChild(node)
	}

	/**
	 * 创建一个并行行为树
	 * 
	 * @param successPolicy 成功策略
	 * @param failurePolicy 失败策略
	 * @return 一个并行行为树
	 */
	@JvmStatic
	fun parallel(successPolicy: ParallelNode.Policy, failurePolicy: ParallelNode.Policy): ParallelNode {
		return ParallelNode(successPolicy, failurePolicy)
	}

	/**
	 * 创建一个等待行为树
	 * 
	 * @param ticks 等待时间
	 * @return 一个等待行为树
	 */
	@JvmStatic
	fun wait(ticks: Int): BTNode {
		return WaitAction(ticks)
	}

	/**
	 * 创建一个带计时器的并行行为树
	 * 
	 * @param duration 计时器时长
	 * @return 一个带计时器的并行行为树
	 */
	@JvmStatic
	fun withTimer(duration: Int): ParallelNode {
		return parallel(ParallelNode.Policy.REQUIRE_ONE, ParallelNode.Policy.REQUIRE_ONE)
			.addChild(wait(duration))
	}

	/**
	 * 创建一个目标行为树
	 * 
	 * @param goal 目标
	 * @return 一个目标行为树
	 */
	@JvmStatic
	fun goal(goal: Goal): GoalWrapper {
		return GoalWrapper(goal)
	}
}
