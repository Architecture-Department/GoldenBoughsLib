package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import architecture.goldenboughs_lib.api.world.entity.ai.motion.DashComponent;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * 冲刺动作 - 实体向正前方快速冲刺
 * <p>使用 {@link DashComponent} 实现匀速移动，并保持朝向目标方向</p>
 */
public class DashAction extends BTNode {
	final Mob mob;
	final DashComponent component;
	float speed;
	Vec3 targetPos;

	public DashAction(Mob mob, float speed) {
		this.mob = mob;
		this.component = new DashComponent(mob);
		this.speed = speed;

	}

	@Override
	public void start() {
		super.start();
		this.component.setDirection(mob.getForward());
		targetPos = mob.position().add(mob.getForward().normalize().scale(200));
	}

	@Override
	public BTStatus execute() {
		this.component.uniformMove(speed);
		this.mob.getLookControl().setLookAt(targetPos);
		this.mob.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos);
		return BTStatus.RUNNING;
	}
}
