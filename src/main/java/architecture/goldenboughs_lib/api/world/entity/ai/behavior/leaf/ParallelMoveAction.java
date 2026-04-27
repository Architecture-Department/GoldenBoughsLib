package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.api.world.entity.ai.motion.DashComponent;
import net.minecraft.world.entity.Mob;

/**
 * 平行移动动作 - 实体平行于目标位置移动或悬停在目标上方
 * <p>典型应用：魔焰眼一阶段的环绕移动、激光眼二阶段的头顶悬停</p>
 */
public class ParallelMoveAction extends BTNode {
	final Mob mob;
	final DashComponent component;
	final float speed;
	final float offsetY;
	float dist;

	public ParallelMoveAction(Mob mob, float dist, float speed, float offsetY) {
		this.mob = mob;
		this.dist = dist;
		this.component = new DashComponent(mob);
		this.speed = speed;
		this.offsetY = offsetY;
	}

	@Override
	public BTStatus execute() {
		if (mob.getTarget() == null) {
			return BTStatus.FAILURE;
		}
		if (dist <= 0) {
			component.hangAbove(mob.getTarget(), this.offsetY, this.speed);
		} else {
			component.hangOn(mob.getTarget(), dist, this.offsetY, this.speed);
		}
		return BTStatus.RUNNING;
	}
}
