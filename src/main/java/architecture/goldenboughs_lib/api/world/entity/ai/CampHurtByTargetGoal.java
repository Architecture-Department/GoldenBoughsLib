package architecture.goldenboughs_lib.api.world.entity.ai;

import architecture.goldenboughs_lib.api.LcLevel;
import architecture.imaginarycraft.util.LcLevelUtil;
import net.minecraft.world.entity.Mob;

import java.util.function.Predicate;

/**
 * 阵营受伤反击目标选择器
 * <p>
 * 扩展自 ModHurtByTargetGoal，增加了等级比较机制。
 * 低等级生物不会召唤高等级生物来协助战斗
 * </p>
 */
public class CampHurtByTargetGoal extends ModHurtByTargetGoal {

	public CampHurtByTargetGoal(Mob mob, Class<?>... toIgnoreDamage) {
		super(mob, true, createClassPredicate(toIgnoreDamage), mob1 -> true, createLevelBasedIgnorePredicate(mob));
	}

	private static Predicate<Mob> createLevelBasedIgnorePredicate(Mob selfMob) {
		return otherMob -> {
			LcLevel mobLcLevel = LcLevelUtil.getLevel(selfMob);
			if (mobLcLevel == null) {
				return false;
			}
			LcLevel otherMobLcLevel = LcLevelUtil.getLevel(otherMob);
			return otherMobLcLevel != null && mobLcLevel.getLevelValue() < otherMobLcLevel.getLevelValue();
		};
	}
}
