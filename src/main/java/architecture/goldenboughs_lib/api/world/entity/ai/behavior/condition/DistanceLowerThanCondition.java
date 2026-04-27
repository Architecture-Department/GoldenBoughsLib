package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * 距离目标小于指定距离条件
 */
public class DistanceLowerThanCondition extends AbstractConditionLeaf {
	final Mob mob;
	final double distance;

	public DistanceLowerThanCondition(Mob mob, double distance) {
		this.mob = mob;
		this.distance = distance;
	}

	@Override
	public boolean check() {
		LivingEntity entity = mob.getTarget();
		if (entity == null) {
			return false;
		}
		return entity.distanceToSqr(mob) <= distance * distance;
	}

	@Override
	public String getDesc() {
		return "Distance to target lower than ";
	}
}
