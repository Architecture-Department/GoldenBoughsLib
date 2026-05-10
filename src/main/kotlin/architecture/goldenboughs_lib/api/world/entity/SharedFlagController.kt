package architecture.goldenboughs_lib.api.world.entity

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData

/**
 * 实体共享状态
 */
@AllOpe
class SharedFlagController(
	val entityData: SynchedEntityData,
	val dataStatusStatus: EntityDataAccessor<Int>
) {
	var shareIndex: Int = -1

	/**
	 * 反转状态
	 *
	 * @param index 状态位
	 * @return 反转后的状态
	 */
	protected fun reverseShareFlag(data: Int, index: Int): Int {
		return data xor (index)
	}

	/**
	 * 设置状态位
	 *
	 * @param flag  状态位
	 * @param value 值
	 */
	fun setFlag(flag: SharedFlag, value: Boolean) {
		this.entityData.set<Int?>(
			this.dataStatusStatus,
			this.calShareFlag(this.entityData.get(this.dataStatusStatus), flag.index, value)
		)
	}

	/**
	 * 计算状态
	 *
	 * @param index 状态位
	 * @param value 值
	 * @return 计算后的状态
	 */
	protected fun calShareFlag(data: Int, index: Int, value: Boolean): Int {
		return if (value) (data or (index)) else (data and (index).inv())
	}

	/**
	 * 获取状态位
	 *
	 * @param flag 状态位
	 * @return 值
	 */
	fun getFlag(flag: SharedFlag): Boolean {
		return (this.entityData.get(this.dataStatusStatus) and (flag.index)) != 0
	}

	/**
	 * 注册共享状态
	 *
	 * @return 共享状态
	 */
	fun registerFlag(): SharedFlag {
		if (shareIndex >= 31) throw RuntimeException("Shared flag index overflow")
		++shareIndex
		return SharedFlag(1 shl shareIndex)
	}

	/**
	 * 状态标志位
	 *
	 * @param index 移位后的flag位
	 */
	@JvmRecord
	data class SharedFlag(val index: Int) {
		override fun toString(): String {
			return "SharedFlag : " + Integer.numberOfTrailingZeros(index)
		}
	}
}
