package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * 跳跃攻击动作 - 实体向目标方向跳跃并发动攻击
 * <p>包含水平速度和垂直附加速度的控制</p>
 */
public class JumpAttackAction extends BTNode {
	final Mob mob;
	final float horizonPower;
	final float jumpAdditionSpeed;

	public JumpAttackAction(Mob mob, float horizonPower) {
		this(mob, horizonPower, 0);
	}

	public JumpAttackAction(Mob mob, float horizonPower, float jumpAdditionSpeed) {
		this.mob = mob;
		this.horizonPower = horizonPower;
		this.jumpAdditionSpeed = jumpAdditionSpeed;
	}

	@Override
	public BTStatus execute() {
		if (this.mob.getTarget() == null) {
			return BTStatus.FAILURE;
		}
		if (!this.mob.onGround()) {
			return BTStatus.RUNNING;
		}

		Vec3 targetPos = this.mob.getTarget().position();
		Vec3 horizonDir = targetPos.subtract(this.mob.position()).multiply(1, 0, 1).normalize();
		this.mob.jumpFromGround();
		this.mob.addDeltaMovement(horizonDir.scale(horizonPower));
		this.mob.addDeltaMovement(new Vec3(0, jumpAdditionSpeed, 0));
		return BTStatus.SUCCESS;
	}
}
