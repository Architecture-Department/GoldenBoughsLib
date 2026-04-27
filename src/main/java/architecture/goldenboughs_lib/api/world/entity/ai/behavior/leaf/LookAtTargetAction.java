package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.Mob;

/**
 * 注视目标动作 - 实体持续看向当前目标
 * <p>同时设置头部旋转和视线控制</p>
 */
public class LookAtTargetAction extends BTNode {
	final Mob mob;

	public LookAtTargetAction(Mob mob) {
		this.mob = mob;
	}

	@Override
	public BTStatus execute() {
		if (mob.getTarget() == null) {
			return BTStatus.FAILURE;
		}
		mob.lookAt(mob.getTarget(), 90, 85);
		mob.getLookControl().setLookAt(mob.getTarget());

		return BTStatus.RUNNING;
	}
}
