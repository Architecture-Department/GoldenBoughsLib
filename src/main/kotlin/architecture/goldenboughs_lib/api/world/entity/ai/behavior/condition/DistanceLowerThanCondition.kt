package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import net.minecraft.world.entity.Mob

/**
 * 距离目标小于指定距离条件
 */
class DistanceLowerThanCondition(
	val mob: Mob,
	val distance: Double
) : AbstractConditionLeaf() {
	override fun check(): Boolean {
		val entity = mob.target ?: return false
		return entity.distanceToSqr(mob) <= distance * distance
	}

	override val desc: String = "Distance to target lower than "
}
