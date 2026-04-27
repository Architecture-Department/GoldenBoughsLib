package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.api.world.entity.ai.motion.DashComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * 飞向目标动作 - 实体径直向目标移动
 * <p>使用 {@link DashComponent} 实现飞行移动，支持 Y 轴偏移</p>
 */
public class FlyTowardTargetAction extends BTNode {
	final Mob mob;
	final DashComponent component;
	final float speed;
	final float offsetY;

	public FlyTowardTargetAction(Mob mob, float speed) {
		this(mob, speed, 0);
	}

	public FlyTowardTargetAction(Mob mob, float speed, float offsetY) {
		this.mob = mob;
		component = new DashComponent(mob);
		this.speed = speed;
		this.offsetY = offsetY;
	}

	@Override
	public BTStatus execute() {
		LivingEntity target = mob.getTarget();
		if (target == null) {
			return BTStatus.FAILURE;
		}
		Vec3 dir = target.position().subtract(mob.position()).add(0, offsetY, 0);
		component.setDirection(dir);
		component.uniformMove(this.speed);
		return BTStatus.RUNNING;
	}
}
