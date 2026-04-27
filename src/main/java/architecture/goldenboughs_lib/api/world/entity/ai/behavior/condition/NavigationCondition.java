package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import net.minecraft.world.entity.PathfinderMob;

/**
 * 是否寻路完成
 */
public class NavigationCondition extends AbstractConditionLeaf {
	PathfinderMob mob;

	public NavigationCondition(PathfinderMob mob) {
		this.mob = mob;
	}

	@Override
	public boolean check() {
		return mob.getNavigation().isDone();
	}
}
