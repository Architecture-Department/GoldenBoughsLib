package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

/**
 * 射击动作 - 实体向目标发射投射物
 * 
 * 抽象类，子类需实现具体的射击逻辑 [.shoot]
 * 
 * @param <T> 实体类型，必须是 [Mob] 的子类
</T> */
abstract class ShootAction<T : Mob>(
	protected val mob: T
) : BTNode() {
	override fun execute(): BTStatus {
		val target = mob.target ?: return BTStatus.FAILURE
		this.shoot(target)
		return BTStatus.SUCCESS
	}

	protected abstract fun shoot(target: LivingEntity?)
}
