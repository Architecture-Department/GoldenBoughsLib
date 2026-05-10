package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import net.minecraft.world.entity.Mob

/**
 * 生命值小于阈值
 */
class HealthLowerThanCondition(
	val mob: Mob,
	val percentage: Float
) : AbstractConditionLeaf() {
	override fun check(): Boolean {
		return mob.health / mob.maxHealth < percentage
	}

	override val desc: String
		get() = "Health lower than ${percentage * 100}%"
}
