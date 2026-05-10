package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.Mob
import java.util.function.Predicate

/**
 * 实体数据值条件
 */
class EntityDataCondition<T>(
	val mob: Mob,
	val dataAccessor: EntityDataAccessor<T>,
	val predicate: Predicate<T>
) : AbstractConditionLeaf() where T : Any {
	override fun check(): Boolean {
		return predicate.test(mob.getEntityData().get(dataAccessor))
	}
}
