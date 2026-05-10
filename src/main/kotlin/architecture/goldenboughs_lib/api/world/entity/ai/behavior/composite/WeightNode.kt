package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.util.EntityUtil


/**
 * 权重随机节点 - 根据权重随机选择一个子节点执行
 * 
 * 使用加权随机算法从子节点中选择一个执行，适用于随机行为选择
 */
class WeightNode : CompositeNode() {
	private val weights: MutableList<Int> = ArrayList()
	override var children: MutableList<BTNode> = ArrayList()
	private var currentChild: BTNode? = null

	fun addChild(weight: Int, child: BTNode): WeightNode {
		this.children.add(child)
		this.weights.add(weight)
		return this
	}

	override fun start() {
		super.start()
		this.currentChild = EntityUtil.getRandomByWeightInt(children, weights)
	}

	override fun execute(): BTStatus? {
		return this.currentChild?.execute()
	}

	override fun cleanup() {
		super.cleanup()
		if (this.currentChild != null) {
			this.currentChild!!.stop()
		}

		this.currentChild = null
	}
}
