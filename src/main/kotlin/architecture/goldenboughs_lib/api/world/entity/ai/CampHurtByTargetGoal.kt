package architecture.goldenboughs_lib.api.world.entity.ai

import architecture.goldenboughs_lib.util.LcLevelUtil.getLevel
import net.minecraft.world.entity.Mob
import java.util.function.Predicate

/**
 * 阵营受伤反击目标选择器
 * 
 * 
 * 扩展自 ModHurtByTargetGoal，增加了等级比较机制。
 * 低等级生物不会召唤高等级生物来协助战斗
 * 
 */
class CampHurtByTargetGoal(
	mob: Mob,
	vararg toIgnoreDamage: Class<*>
) : ModHurtByTargetGoal(
	mob,
	true,
	createClassPredicate(*toIgnoreDamage),
	{ mob1 -> true },
	createLevelBasedIgnorePredicate(mob)
) {
	companion object {
		@JvmStatic
		private fun createLevelBasedIgnorePredicate(selfMob: Mob): Predicate<Mob> {
			return Predicate { otherMob: Mob ->
				val mobLcLevel = selfMob.getLevel() ?: return@Predicate false
				val otherMobLcLevel = otherMob.getLevel()
				otherMobLcLevel != null && mobLcLevel.levelValue < otherMobLcLevel.levelValue
			}
		}
	}
}
