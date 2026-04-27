package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.Mob;

/**
 * 设置无物理效果动作 - 控制实体是否受物理碰撞影响
 * <p>设置实体的 noPhysics 标志，用于穿透方块或其他实体</p>
 */
public class SetNoPhysicsAction extends BTNode {
	final Mob mob;
	final boolean isNoPhysics;

	public SetNoPhysicsAction(Mob mob, boolean isNoPhysics) {
		this.mob = mob;
		this.isNoPhysics = isNoPhysics;
	}

	@Override
	public BTStatus execute() {
		this.mob.noPhysics = this.isNoPhysics;
		return BTStatus.SUCCESS;
	}
}
