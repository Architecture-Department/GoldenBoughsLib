package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.Mob
import java.util.function.Supplier

/**
 * 同步实体数据动作 - 将数据同步到实体的 DataWatcher
 * 
 * 用于更新实体的同步数据参数，确保客户端能看到最新状态
 * 
 * @param <T> 数据类型
</T> */
class SyncAction<T>(
	val mob: Mob,
	val data: EntityDataAccessor<T>,
	val dataSupplier: Supplier<T>
) : BTNode() where T : Any {
	override fun execute(): BTStatus {
		mob.getEntityData().set(data, dataSupplier.get())
		return BTStatus.SUCCESS
	}
}
