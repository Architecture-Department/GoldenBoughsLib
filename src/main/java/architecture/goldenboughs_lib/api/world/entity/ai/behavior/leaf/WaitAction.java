package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;

/**
 * 等待动作 - 在指定 tick 数内保持运行状态，超时后返回成功
 * <p>用于延迟执行或暂停行为树</p>
 */
public class WaitAction extends BTNode {
	protected int waitTicks;
	protected int currentTicks = 0;

	public WaitAction(int waitTicks) {
		this.waitTicks = waitTicks;
	}

	@Override
	public BTStatus execute() {
		if (++currentTicks >= waitTicks) {
			return BTStatus.SUCCESS;
		}
		return BTStatus.RUNNING;
	}

	@Override
	protected void cleanup() {
		currentTicks = 0;
	}

	public int getWaitTicks() {
		return waitTicks;
	}

	public int getCurrentTicks() {
		return currentTicks;
	}
}
