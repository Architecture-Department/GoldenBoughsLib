package architecture.goldenboughs_lib.api.world.entity

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTRoot
import net.minecraft.world.entity.Mob

interface IBehaviorTreeMob<T : Mob> {
	fun createBehaviorTree(): BTRoot<T>
}
