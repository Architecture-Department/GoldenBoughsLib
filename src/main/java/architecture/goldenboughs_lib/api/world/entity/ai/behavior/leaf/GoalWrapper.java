package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * 目标包装器 - 将现有的 Goal 包装为行为树节点
 * <p><strong>警告：</strong>此实现可能存在兼容性问题，极不推荐使用</p>
 * <p>仅用于临时迁移或测试目的</p>
 */
public class GoalWrapper extends BTNode {
	private final Goal goal;

	public GoalWrapper(Goal goal) {
		this.goal = goal;
	}

	@Override
	public BTStatus execute() {
//        if (!goal.canUse()) {
//            return BTStatus.FAILURE;
//        }

		if (goal.canContinueToUse()) {
			goal.tick();
			return BTStatus.RUNNING;
		}

		return BTStatus.SUCCESS;
	}

	@Override
	public boolean canUse() {
		return super.canUse();
	}

	@Override
	public boolean isReady() {
		return super.isReady() && goal.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse();
	}

	@Override
	public void start() {
		goal.start();
		super.start();
	}

	@Override
	public void stop() {
		goal.stop();
		super.stop();
	}

	@Override
	public void setFlags(EnumSet<Flag> flagSet) {
		goal.setFlags(flagSet);
	}
}
