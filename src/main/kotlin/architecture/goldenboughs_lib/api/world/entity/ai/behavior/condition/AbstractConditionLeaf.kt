package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 条件叶节点抽象基类 - 与 Sequence 和 Selector 配合实现短路逻辑
 * 
 * 在使用 Web UI 生成代码时使用此抽象类，简化条件检查的实现
 */
abstract class AbstractConditionLeaf : BTNode(), ConditionBT {
	override fun execute(): BTStatus {
		if (check()) {
			return BTStatus.SUCCESS
		}
		return BTStatus.FAILURE
	}
}
