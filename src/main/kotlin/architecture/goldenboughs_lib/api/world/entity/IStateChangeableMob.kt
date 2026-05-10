package architecture.goldenboughs_lib.api.world.entity

import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.Entity

/**
 * 受伤或再次生成时自动切换状态
 */
interface IStateChangeableMob {
	/**
	 * 当受伤或生成时触发
	 */
	fun changeState()

	var stage: Int
		get() = this.self.getEntityData().get(dataStatusStatus)
		set(stage) {
			this.self.getEntityData().set(dataStatusStatus, stage)
		}

	val dataStatusStatus: EntityDataAccessor<Int>

	private val self: Entity
		get() = this as Entity

	fun syncStatus(status: Int) {
		this.self.getEntityData().set(dataStatusStatus, status)
	}
}
