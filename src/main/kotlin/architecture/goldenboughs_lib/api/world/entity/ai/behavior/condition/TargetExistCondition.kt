package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import net.minecraft.world.entity.Mob

/**
 * 生物目标存在条件
 */
class TargetExistCondition(val mob: Mob) : AbstractConditionLeaf() {
	override fun check(): Boolean {
		return mob.target != null
	}

	override val desc: String
		get() = "Target Exist"
}
