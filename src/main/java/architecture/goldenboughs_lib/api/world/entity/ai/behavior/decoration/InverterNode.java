package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;

/**
 * 反转节点（成功变失败，失败变成功）
 */
public class InverterNode extends DecorationNode {
	public InverterNode(BTNode child) {
		super(child);
	}

	@Override
	public BTStatus execute() {

		child.tryStart();

		if (child.getStatus() == BTStatus.RUNNING) {
			child.tick();
		}

		if (child.getStatus() == BTStatus.RUNNING) {
			return BTStatus.RUNNING;
		}

		return child.getStatus() == BTStatus.SUCCESS ?
			BTStatus.FAILURE : BTStatus.SUCCESS;
	}
}
