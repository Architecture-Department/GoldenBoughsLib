package architecture.goldenboughs_lib.api.world.entity.ai.behavior

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.world.entity.Mob

/**
 * 行为树根节点
 */
@AllOpen
abstract class BTRoot<T : Mob>(protected var mob: T) : BTNode() {
	var child: BTNode? = null
		protected set

	override fun canContinueToUse(): Boolean {
		return this.canUse()
	}

	override fun canUse(): Boolean {
		return true
	}

	override fun start() {
		super.start()
		if (this.child == null) {
			this.child = this.createBehaviorTree()
		}
		child?.start()
	}

	/**
	 * 延迟构造行为树
	 */
	protected abstract fun createBehaviorTree(): BTNode

	override fun tick() {
		child?.tick()
	}

	override fun stop() {
		super.stop()
		child?.stop()
	}

	override fun execute(): BTStatus? {
		return child?.execute()
	}
}
