package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

/**
 * 接近目标动作 - 实体使用寻路系统接近目标直到进入触发距离
 * <p>支持设置触发距离和寻路间隔，当与目标距离小于等于触发距离时返回成功</p>
 */
public class MoveToTargetAction extends BTNode {
	final int triggerDistance;
	final int _findInterval;
	PathfinderMob mob;
	int findInterval;

	public MoveToTargetAction(PathfinderMob mob, int triggerDistance, int findInterval) {
		this.mob = mob;
		this.triggerDistance = triggerDistance;
		this._findInterval = findInterval;
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && mob.getTarget() != null;
	}

	/**
	 * 执行动作
	 *
	 * @return <p>SUCCESS：进入下一个执行任务</p>
	 * <p>RUNNING：继续执行当前任务</p>
	 * <p>FAILURE：任务失败</p>
	 */
	@Override
	public BTStatus execute() {
		LivingEntity target = mob.getTarget();
		if (target == null) {
			return BTStatus.FAILURE;
		}

		if (--this.findInterval <= 0) {
			mob.getNavigation().moveTo(target, 1.0);
			this.findInterval = _findInterval;
		}

		float distance = mob.distanceTo(target);
		if (distance <= triggerDistance) {
			return BTStatus.SUCCESS;
		}
		return BTStatus.RUNNING;
	}
}
