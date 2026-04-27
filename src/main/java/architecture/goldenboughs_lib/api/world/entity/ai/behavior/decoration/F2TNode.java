package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;

/**
 * 当状态为失败时，将其转换为成功状态。用于sequence防止短路
 */
public class F2TNode extends DecorationNode {
	public F2TNode(BTNode child) {
		super(child);
	}

	@Override
	public BTStatus execute() {
		BTStatus status = child.execute();
		if (status == BTStatus.FAILURE) {
			return BTStatus.SUCCESS;
		}
		return status;
	}
}
