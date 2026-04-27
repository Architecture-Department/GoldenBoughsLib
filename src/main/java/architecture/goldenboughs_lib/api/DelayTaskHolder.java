package architecture.goldenboughs_lib.api;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DelayTaskHolder {
	private final IAttachmentHolder attachmentHolder;
	private final Map<ResourceLocation, ITask> runList = new LinkedHashMap<>();

	public DelayTaskHolder(IAttachmentHolder attachmentHolder) {
		this.attachmentHolder = attachmentHolder;
	}

	public static DelayTaskHolder of(AttachmentHolder attachmentHolder) {
		return attachmentHolder.getData(LibAttachments.DELAY_TASK_HOLDER);
	}

	public static ITask.Builder createTaskBilder() {
		return ITask.Builder.create();
	}

	public IAttachmentHolder getAttachmentHolder() {
		return attachmentHolder;
	}

	public Map<ResourceLocation, ITask> getRunList() {
		return runList;
	}

	public void tick() {
		if (runList.isEmpty()) {
			return;
		}
		Iterator<ITask> iterator = runList.values().iterator();
		while (iterator.hasNext()) {
			ITask consumer = iterator.next();
			if (consumer.isRemoved()) {
				consumer.removed();
				iterator.remove();
				continue;
			}
			consumer.run(this);
		}
	}

	/**
	 * 通过该方法添加的任务会在对应手的物品更替时移除
	 */
	public void addTask(InteractionHand handUsed, ITask task) {
		addTask(handUsed == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, task);
	}

	/**
	 * 通过该方法添加的任务会在对应槽位的物品更替时移除
	 */
	public void addTask(EquipmentSlot slot, ITask task) {
		addTask(GoldenBoughsLib.modRl(slot.getName()), task);
	}

	public void addTask(ResourceLocation id, ITask task) {
		runList.put(id, task);
	}

	/**
	 * 通过该方法添加的任务会在对应手的物品更替时移除
	 */
	public void addTask(InteractionHand handUsed, String name, ITask task) {
		addTask(handUsed == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, name, task);
	}

	/**
	 * 通过该方法添加的任务会在对应槽位的物品更替时移除
	 */
	public void addTask(EquipmentSlot slot, String name, ITask task) {
		addTask(GoldenBoughsLib.modRl(slot.getName() + "." + name), task);
	}

	/**
	 * 使用此方法会移除对应手的任务包括相关的
	 */
	public void removeTask(InteractionHand handUsed) {
		removeTask(handUsed == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
	}

	/**
	 * 使用此方法会移除对应槽位的任务包括相关的
	 */
	public void removeTask(EquipmentSlot slot) {
		if (containsTask(slot).isEmpty()) {
			return;
		}
		for (ResourceLocation key : runList.keySet()) {
			if (key.getPath().startsWith(slot.getName())) {
				runList.remove(key).removed();
			}
		}
	}

	/**
	 * 如果返回的是空集合就表示该槽位没有任务
	 */
	public Set<ResourceLocation> containsTask(EquipmentSlot slot) {
		return runList.keySet().stream().filter(key -> key.getPath().startsWith(slot.getName())).collect(Collectors.toSet());
	}

	public void removeTask(InteractionHand handUsed, String name) {
		removeTask(handUsed == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, name);
	}

	public void removeTask(EquipmentSlot slot, String name) {
		if (!containsTask(slot, name)) {
			return;
		}
		removeTask(GoldenBoughsLib.modRl(slot.getName() + "." + name));
	}

	public void removeTask(ResourceLocation id) {
		if (!containsTask(id)) {
			return;
		}
		runList.remove(id).removed();
	}

	public boolean containsTask(ResourceLocation id) {
		return runList.containsKey(id);
	}

	public boolean containsTask(EquipmentSlot slot, String name) {
		return runList.containsKey(GoldenBoughsLib.modRl(slot.getName() + "." + name));
	}

	/**
	 * 如果返回的是空集合就表示该槽位没有任务
	 */
	public Set<ResourceLocation> containsTask(InteractionHand handUsed) {
		return containsTask(handUsed == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
	}

	public boolean containsTask(InteractionHand handUsed, String name) {
		return containsTask(handUsed == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, name);
	}

	public void removeAllTask() {
		runList.clear();
	}

	public interface ITask {
		void run(DelayTaskHolder delayTaskHolder);

		void removed();

		boolean isRemoved();

		void setRemoved(boolean isRemoved);

		/**
		 * 每一tick执行一次可通过修改返回值来自定义结束的时间之类的逻辑
		 */
		@FunctionalInterface
		interface TickRun {
			int run(int tick, int maxTick, ITask iTask);
		}

		@FunctionalInterface
		interface ResultRun {
			void run();
		}

		@FunctionalInterface
		interface RemovedRun {
			void run(int tick);
		}

		/**
		 * 运行任务类，isRemoved为true时将在下一刻移除该任务
		 */
		class BaseTask implements ITask {
			protected final ResultRun resultRun;
			protected final @Nullable RemovedRun removedRun;
			protected final int maxTick;
			protected final int maxRepeatCount;
			protected int tick = 0;
			protected int repeatCount = 0;
			protected boolean isRemoved;

			private BaseTask(ResultRun resultRun, @Nullable RemovedRun removedRun, int removedTick, int maxRepeatCount) {
				this.resultRun = resultRun;
				this.removedRun = removedRun;
				this.maxTick = removedTick;
				this.maxRepeatCount = maxRepeatCount;
			}

			@Override
			public void run(DelayTaskHolder delayTaskHolder) {
				if (maxRepeatCount > 0) {
					if (repeatCount == maxRepeatCount) {
						isRemoved = true;
						return;
					}
				}

				if (tick >= maxTick) {
					resultRun.run();
					repeatCount++;
					tick = 0;
				}
				tick++;
			}

			@Override
			public void removed() {
				if (removedRun != null) {
					removedRun.run(tick);
				}
			}

			@Override
			public boolean isRemoved() {
				return isRemoved;
			}

			@Override
			public void setRemoved(boolean isRemoved) {
				this.isRemoved = isRemoved;
			}
		}

		class TickTask extends BaseTask {
			private final TickRun tickRun;

			private TickTask(TickRun tickRun, RemovedRun removedRun, ResultRun resultRun, int removedTick, int maxRepeatCount) {
				super(resultRun, removedRun, removedTick, maxRepeatCount);
				this.tickRun = tickRun;
			}

			@Override
			public void run(DelayTaskHolder delayTaskHolder) {
				if (maxRepeatCount > 0) {
					if (repeatCount == maxRepeatCount) {
						isRemoved = true;
						return;
					}
				}

				if (tick >= maxTick) {
					resultRun.run();
					repeatCount++;
					tick = 0;
				}

				tick = tickRun.run(tick, maxTick, this);
			}
		}

		class Builder {
			private @Nullable TickRun tickRun;
			private @Nullable RemovedRun removedRun;
			private ResultRun resultRun;
			private int removedTick;
			private int repeatCount = 1;

			private Builder() {
			}

			public static Builder create() {
				return new Builder();
			}

			public Builder tickRun(TickRun tickRun) {
				this.tickRun = tickRun;
				return this;
			}

			public Builder resultRun(ResultRun resultRun) {
				this.resultRun = resultRun;
				return this;
			}

			public Builder removedRun(RemovedRun removedRun) {
				this.removedRun = removedRun;
				return this;
			}

			public Builder removedTick(int removedTick) {
				this.removedTick = removedTick;
				return this;
			}

			public Builder repeatCount(int repeatCount) {
				this.repeatCount = repeatCount;
				return this;
			}

			public ITask build() {
				assert resultRun != null : "resultRun can not be null";
				assert repeatCount == 0 : "repeatCount can not be less than 1";
				return tickRun == null ?
					new BaseTask(resultRun, removedRun, removedTick, repeatCount) :
					new TickTask(tickRun, removedRun, resultRun, removedTick, repeatCount);
			}
		}
	}
}
