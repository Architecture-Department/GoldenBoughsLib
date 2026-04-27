package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;

/**
 * 装饰节点 - 包含单个子节点的抽象基类，用于增强或修改子节点行为
 * <p>典型应用：条件检查、中断控制、时间控制等</p>
 */
public abstract class DecorationNode extends BTNode {
	protected final BTNode child;

	public DecorationNode(BTNode child) {
		this.child = child;
	}

	@Override
	public void start() {
		super.start();
		child.start();
	}

	@Override
	protected void cleanup() {
//        if (child.getStatus() == BTStatus.RUNNING) {
		child.stop();
//        }
	}

	public BTNode getChild() {
		return child;
	}
}
