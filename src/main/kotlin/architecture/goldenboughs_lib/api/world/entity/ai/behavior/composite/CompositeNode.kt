package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode

/**
 * 组合节点 - 包含多个子节点的抽象基类
 * 
 * 用于组织和管理行为树中的子节点集合
 */
abstract class CompositeNode : BTNode() {
	var children: MutableList<BTNode> = ArrayList()
		protected set

	abstract override fun execute(): BTStatus?
}
