package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * 射击动作 - 实体向目标发射投射物
 * <p>抽象类，子类需实现具体的射击逻辑 {@link #shoot(LivingEntity)}</p>
 *
 * @param <T> 实体类型，必须是 {@link Mob} 的子类
 */
public abstract class ShootAction<T extends Mob> extends BTNode {

	protected final T mob;

	public ShootAction(T mob) {
		this.mob = mob;
	}

	@Override
	public BTStatus execute() {
		LivingEntity target = mob.getTarget();
		if (target == null) {
			return BTStatus.FAILURE;
		}
		this.shoot(target);
		return BTStatus.SUCCESS;
	}

	protected abstract void shoot(LivingEntity target);

}
