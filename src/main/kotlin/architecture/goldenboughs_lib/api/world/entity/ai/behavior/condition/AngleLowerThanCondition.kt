package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import architecture.goldenboughs_lib.util.world.entity.EntityUtil.angleBetween
import net.minecraft.world.entity.Mob

/**
 * 角度小于条件
 */
class AngleLowerThanCondition(
	val mob: Mob,
	val angle: Double
) : AbstractConditionLeaf() {
	override fun check(): Boolean {
		if (mob.target == null) {
			return false
		}
		return angleBetween(mob.deltaMovement, mob.target!!.position().subtract(mob.position())) < angle
	}
}
