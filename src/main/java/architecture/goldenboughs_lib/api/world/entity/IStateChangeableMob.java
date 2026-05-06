package architecture.goldenboughs_lib.api.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

/**
 * 受伤或再次生成时自动切换状态
 */
public interface IStateChangeableMob {

	/**
	 * 当受伤或生成时触发
	 */
	void changeState();

	default int getStage() {
		return getSelf().getEntityData().get(get_DATA_STATUS_STATUS());
	}

	default void setStage(int stage) {
		getSelf().getEntityData().set(get_DATA_STATUS_STATUS(), stage);
	}

	EntityDataAccessor<Integer> get_DATA_STATUS_STATUS();

	private Entity getSelf() {
		return (Entity) this;
	}

	default void syncStatus(int status) {
		getSelf().getEntityData().set(get_DATA_STATUS_STATUS(), status);
	}

}
