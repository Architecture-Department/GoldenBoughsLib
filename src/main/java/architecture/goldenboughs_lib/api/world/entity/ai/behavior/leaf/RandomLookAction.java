package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.util.EntityUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * 随机环顾动作 - 实体随机看向周围某个方向
 * <p>在实体周围 1 格半径的球面上随机选择观察点</p>
 */
public class RandomLookAction extends BTNode {

	final Mob mob;
	Vec3 pos;

	public RandomLookAction(Mob mob) {
		this.mob = mob;
	}

	@Override
	public void start() {
		super.start();
		pos = mob.getEyePosition().add(EntityUtil.sphere(1, mob.getRandom().nextFloat() * 6.28f, (float) Math.PI * 0.5f));
	}

	@Override
	public BTStatus execute() {
		mob.lookAt(EntityAnchorArgument.Anchor.EYES, pos);
		mob.getLookControl().setLookAt(pos);

		return BTStatus.RUNNING;
	}

}
