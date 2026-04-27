package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite;


import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.util.EntityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 权重随机节点 - 根据权重随机选择一个子节点执行
 * <p>使用加权随机算法从子节点中选择一个执行，适用于随机行为选择</p>
 */
public class WeightNode extends CompositeNode {
	private final List<Integer> weights;
	private BTNode currentChild;

	public WeightNode() {
		this.children = new ArrayList<>();
		this.weights = new ArrayList<>();
	}

	public WeightNode addChild(int weight, BTNode child) {
		this.children.add(child);
		this.weights.add(weight);
		return this;
	}

	@Override
	public void start() {
		super.start();
		this.currentChild = EntityUtil.getRandomByWeightInt(children, weights);
	}

	@Override
	public BTStatus execute() {
		return this.currentChild.execute();
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		if (this.currentChild != null) {
			this.currentChild.stop();
		}

		this.currentChild = null;
	}
}
