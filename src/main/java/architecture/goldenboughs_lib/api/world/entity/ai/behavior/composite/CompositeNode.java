package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合节点 - 包含多个子节点的抽象基类
 * <p>用于组织和管理行为树中的子节点集合</p>
 */
public abstract class CompositeNode extends BTNode {
	protected List<BTNode> children = new ArrayList<>();

	@Override
	public BTStatus execute() {
		return null;
	}

	public List<BTNode> getChildren() {
		return children;
	}
}
