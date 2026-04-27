package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ISharedFlagControllerHolder;
import architecture.goldenboughs_lib.api.world.entity.SharedFlagController;
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;

/**
 * 同步共享标志位动作 - 设置实体的共享状态标志
 * <p>用于控制客户端可访问的共享状态，如动画播放、特效显示等</p>
 *
 * @param <T> 实体类型，需实现 {@link ISharedFlagControllerHolder}
 */
public class SyncFlagAction<T extends ISharedFlagControllerHolder> extends BTNode {
	protected final T entity;
	protected final SharedFlagController.SharedFlag sharedFlag;
	protected final boolean isEnable;

	public SyncFlagAction(T entity,
	                      SharedFlagController.SharedFlag sharedFlag,
	                      boolean isEnable) {
		this.entity = entity;
		this.sharedFlag = sharedFlag;
		this.isEnable = isEnable;

	}

	@Override
	public BTStatus execute() {
		return BTStatus.SUCCESS;
	}

	@Override
	public void start() {
		super.start();
		entity.getSharedFlagController().setFlag(sharedFlag, isEnable);
	}
}
