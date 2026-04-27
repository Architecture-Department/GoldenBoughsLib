package architecture.goldenboughs_lib.api.world.entity;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTRoot;
import net.minecraft.world.entity.Mob;

public interface IBehaviorTreeMob<T extends Mob> {
	BTRoot<T> createBehaviorTree();
}
