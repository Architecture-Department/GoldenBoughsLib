package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import architecture.goldenboughs_lib.util.EntityUtil;
import net.minecraft.world.entity.Mob;

/**
 * 角度小于条件
 */
public class AngleLowerThanCondition extends AbstractConditionLeaf {
	final Mob mob;
	final double angle;

	public AngleLowerThanCondition(Mob mob, double angle) {
		this.mob = mob;
		this.angle = angle;
	}

	@Override
	public boolean check() {
		if (mob.getTarget() == null) {
			return false;
		}
		return EntityUtil.angleBetween(mob.getDeltaMovement(), mob.getTarget().position().subtract(mob.position())) < angle;
	}
}
