package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import net.minecraft.world.entity.PathfinderMob

/**
 * 是否寻路完成
 */
class NavigationCondition(
	var mob: PathfinderMob
) : AbstractConditionLeaf() {
	override fun check(): Boolean {
		return mob.getNavigation().isDone
	}
}
