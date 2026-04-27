package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Mob;

import java.util.function.Supplier;

/**
 * 同步实体数据动作 - 将数据同步到实体的 DataWatcher
 * <p>用于更新实体的同步数据参数，确保客户端能看到最新状态</p>
 *
 * @param <T> 数据类型
 */
public class SyncAction<T> extends BTNode {
	final EntityDataAccessor<T> data;
	final Supplier<T> dataSupplier;
	final Mob mob;

	public SyncAction(Mob mob, EntityDataAccessor<T> data, Supplier<T> dataSupplier) {
		this.mob = mob;
		this.data = data;
		this.dataSupplier = dataSupplier;
	}

	@Override
	public BTStatus execute() {
		mob.getEntityData().set(data, dataSupplier.get());
		return BTStatus.SUCCESS;
	}
}
