package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import software.bernie.geckolib.animatable.GeoEntity;

/**
 * 动画触发动作 - 仅在服务端触发动画，不需要同步状态到客户端
 * <p>适用于纯服务端控制的动画播放</p>
 */
public class AnimTriggerAction extends BTNode {
	final String controllerName;
	final String animationName;
	final GeoEntity entity;

	public AnimTriggerAction(GeoEntity entity,
	                         String controllerName,
	                         String animationName) {
		this.entity = entity;
		this.controllerName = controllerName;
		this.animationName = animationName;
	}

	@Override
	public BTStatus execute() {
		return BTStatus.SUCCESS;
	}

	@Override
	public void start() {
		super.start();
		entity.triggerAnim(controllerName, animationName);
	}
}
