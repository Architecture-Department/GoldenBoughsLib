package architecture.goldenboughs_lib.api

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibAttachmentTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.neoforged.neoforge.attachment.AttachmentHolder
import net.neoforged.neoforge.attachment.IAttachmentHolder
import java.util.stream.Collectors
import architecture.goldenboughs_lib.core.LibConstants

// TODO 提供饰品栏移除
class DelayTaskHolder(val attachmentHolder: IAttachmentHolder) {
	private val runList: MutableMap<ResourceLocation, ITask> = LinkedHashMap()

	fun tick() {
		if (runList.isEmpty()) {
			return
		}
		val iterator = runList.values.iterator()
		while (iterator.hasNext()) {
			val consumer = iterator.next()
			if (consumer.isRemoved) {
				consumer.removed()
				iterator.remove()
				continue
			}
			consumer.run(this)
		}
	}

	/**
	 * 通过该方法添加的任务会在对应手的物品更替时移除
	 */
	fun addTask(handUsed: InteractionHand, task: ITask) {
		addTask(if (handUsed == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND, task)
	}

	/**
	 * 通过该方法添加的任务会在对应槽位的物品更替时移除
	 */
	fun addTask(slot: EquipmentSlot, task: ITask) {
		addTask(LibConstants.modRl(slot.getName()), task)
	}

	fun addTask(id: ResourceLocation, task: ITask) {
		runList[id] = task
	}

	/**
	 * 通过该方法添加的任务会在对应手的物品更替时移除
	 */
	fun addTask(handUsed: InteractionHand, name: String, task: ITask) {
		addTask(
			if (handUsed == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND,
			name,
			task
		)
	}

	/**
	 * 通过该方法添加的任务会在对应槽位的物品更替时移除
	 */
	fun addTask(slot: EquipmentSlot, name: String, task: ITask) {
		addTask(LibConstants.modRl(slot.getName() + "." + name), task)
	}

	/**
	 * 使用此方法会移除对应手的任务包括相关的
	 */
	fun removeTask(handUsed: InteractionHand) {
		removeTask(if (handUsed == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND)
	}

	/**
	 * 使用此方法会移除对应槽位的任务包括相关的
	 */
	fun removeTask(slot: EquipmentSlot) {
		if (containsTask(slot).isEmpty()) {
			return
		}
		for (key in runList.keys) {
			if (key.path.startsWith(slot.getName())) {
				runList.remove(key)?.removed()
			}
		}
	}

	/**
	 * 如果返回的是空集合就表示该槽位没有任务
	 */
	fun containsTask(slot: EquipmentSlot): MutableSet<ResourceLocation> {
		return runList.keys.stream().filter { key: ResourceLocation -> key.path.startsWith(slot.getName()) }
			.collect(Collectors.toSet())
	}

	fun removeTask(handUsed: InteractionHand, name: String) {
		removeTask(if (handUsed == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND, name)
	}

	fun removeTask(slot: EquipmentSlot, name: String) {
		if (!containsTask(slot, name)) {
			return
		}
		removeTask(LibConstants.modRl(slot.getName() + "." + name))
	}

	fun removeTask(id: ResourceLocation) {
		if (!containsTask(id)) {
			return
		}
		runList.remove(id)!!.removed()
	}

	fun containsTask(id: ResourceLocation): Boolean {
		return runList.containsKey(id)
	}

	fun containsTask(slot: EquipmentSlot, name: String): Boolean {
		return runList.containsKey(LibConstants.modRl(slot.getName() + "." + name))
	}

	/**
	 * 如果返回的是空集合就表示该槽位没有任务
	 */
	fun containsTask(handUsed: InteractionHand): MutableSet<ResourceLocation> {
		return containsTask(if (handUsed == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND)
	}

	fun containsTask(handUsed: InteractionHand, name: String): Boolean {
		return containsTask(
			if (handUsed == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND,
			name
		)
	}

	fun removeAllTask() {
		runList.clear()
	}

	interface ITask {
		fun run(delayTaskHolder: DelayTaskHolder)

		fun removed()

		var isRemoved: Boolean

		/**
		 * 每一tick执行一次可通过修改返回值来自定义结束的时间之类的逻辑
		 */
		fun interface TickRun {
			fun run(tick: Int, maxTick: Int, iTask: ITask): Int
		}

		fun interface ResultRun {
			fun run()
		}

		fun interface RemovedRun {
			fun run(tick: Int)
		}

		/**
		 * 运行任务类，isRemoved为true时将在下一刻移除该任务
		 */
		@AllOpe
		open class BaseTask(
			protected val resultRun: ResultRun,
			protected val removedRun: RemovedRun?,
			protected val maxTick: Int,
			protected val maxRepeatCount: Int
		) : ITask {
			protected var tick: Int = 0
			protected var repeatCount: Int = 0
			override var isRemoved: Boolean = false

			override fun run(delayTaskHolder: DelayTaskHolder) {
				if (maxRepeatCount > 0) {
					if (repeatCount == maxRepeatCount) {
						isRemoved = true
						return
					}
				}

				if (tick >= maxTick) {
					resultRun.run()
					repeatCount++
					tick = 0
				}
				tick++
			}

			override fun removed() {
				removedRun?.run(tick)
			}
		}

		@AllOpe
		class TickTask(
			private val tickRun: TickRun,
			removedRun: RemovedRun?,
			resultRun: ResultRun,
			removedTick: Int,
			maxRepeatCount: Int
		) : BaseTask(resultRun, removedRun, removedTick, maxRepeatCount) {
			override fun run(delayTaskHolder: DelayTaskHolder) {
				if (maxRepeatCount > 0) {
					if (repeatCount == maxRepeatCount) {
						isRemoved = true
						return
					}
				}

				if (tick >= maxTick) {
					resultRun.run()
					repeatCount++
					tick = 0
				}

				tick = tickRun.run(tick, maxTick, this)
			}
		}

		class Builder private constructor() {
			private var tickRun: TickRun? = null
			private var removedRun: RemovedRun? = null
			private var resultRun: ResultRun? = null
			private var removedTick = 0
			private var repeatCount = 1

			fun tickRun(tickRun: TickRun): Builder {
				this.tickRun = tickRun
				return this
			}

			fun resultRun(resultRun: ResultRun): Builder {
				this.resultRun = resultRun
				return this
			}

			fun removedRun(removedRun: RemovedRun): Builder {
				this.removedRun = removedRun
				return this
			}

			fun removedTick(removedTick: Int): Builder {
				this.removedTick = removedTick
				return this
			}

			fun repeatCount(repeatCount: Int): Builder {
				this.repeatCount = repeatCount
				return this
			}

			fun build(): ITask {
				checkNotNull(resultRun) { "resultRun can not be null" }
				assert(repeatCount == 0) { "repeatCount can not be less than 1" }
				return if (tickRun == null) BaseTask(resultRun!!, removedRun, removedTick, repeatCount)
				else TickTask(tickRun!!, removedRun, resultRun!!, removedTick, repeatCount)
			}

			companion object {
				@JvmStatic
				fun create(): Builder {
					return Builder()
				}
			}
		}
	}

	companion object {
		@JvmStatic
		fun of(attachmentHolder: AttachmentHolder): DelayTaskHolder {
			return attachmentHolder.getData(LibAttachmentTypes.DELAY_TASK_HOLDER)
		}

		@JvmStatic
		fun createTaskBilder(): ITask.Builder {
			return ITask.Builder.create()
		}
	}
}
