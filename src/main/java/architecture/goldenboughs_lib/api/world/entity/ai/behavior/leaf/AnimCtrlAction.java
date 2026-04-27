package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ISharedFlagControllerHolder;
import architecture.goldenboughs_lib.api.world.entity.SharedFlagController;
import software.bernie.geckolib.animatable.GeoEntity;

/**
 * Geo 动画控制器动作 - 触发动画并将状态绑定到共享标志位，用于客户端同步
 * <p>继承自 {@link SyncFlagAction}，在服务端触发动画的同时同步标志位到客户端</p>
 *
 * @param <T> 实体类型，需同时实现 {@link GeoEntity} 和 {@link ISharedFlagControllerHolder}
 */
public class AnimCtrlAction<T extends GeoEntity & ISharedFlagControllerHolder> extends SyncFlagAction<T> {
	final String controllerName;
	final String animationName;

	public AnimCtrlAction(T entity,
	                      String controllerName,
	                      String animationName,
	                      SharedFlagController.SharedFlag sharedFlag,
	                      boolean isEnable) {
		super(entity, sharedFlag, isEnable);

		this.controllerName = controllerName;
		this.animationName = animationName;
	}

	@Override
	public void start() {
		super.start();
		if (isEnable) {
			entity.triggerAnim(controllerName, animationName);
		}
	}
}
