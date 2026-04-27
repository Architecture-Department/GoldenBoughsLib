package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * 向前跳跃动作 - 实体向当前朝向方向跳跃
 * <p>包含水平速度和垂直附加速度的控制，不依赖目标</p>
 */
public class JumpForwardAction extends BTNode {
	final Mob mob;
	final float horizonPower;
	final float jumpAdditionSpeed;

	public JumpForwardAction(Mob mob, float horizonPower) {
		this(mob, horizonPower, 0);
	}

	public JumpForwardAction(Mob mob, float horizonPower, float jumpAdditionSpeed) {
		this.mob = mob;
		this.horizonPower = horizonPower;
		this.jumpAdditionSpeed = jumpAdditionSpeed;
	}

	@Override
	public BTStatus execute() {
		if (!this.mob.onGround()) {
			return BTStatus.RUNNING;
		}
		Vec3 horizonDir = this.mob.getForward().multiply(1, 0, 1).normalize();
		this.mob.jumpFromGround();
		this.mob.addDeltaMovement(horizonDir.scale(horizonPower));
		this.mob.addDeltaMovement(new Vec3(0, jumpAdditionSpeed, 0));
		return BTStatus.SUCCESS;
	}
}
