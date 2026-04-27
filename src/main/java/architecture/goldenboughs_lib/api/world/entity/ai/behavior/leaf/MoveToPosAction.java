package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

/**
 * 移动到位置动作 - 实体使用寻路系统移动到指定坐标
 * <p>支持设置目标点和移动速度，到达后返回成功</p>
 */
public class MoveToPosAction extends BTNode {
	private final PathfinderMob mob;
	private final double speed;
	private double targetX, targetY, targetZ;

	public MoveToPosAction(PathfinderMob mob, double speed) {
		this.mob = mob;
		this.speed = speed;
		setFlags(EnumSet.of(Flag.MOVE));
	}

	public MoveToPosAction setTarget(double x, double y, double z) {
		this.targetX = x;
		this.targetY = y;
		this.targetZ = z;
		return this;
	}

	@Override
	public BTStatus execute() {
		if (mob.getNavigation().isDone()) {
			mob.getNavigation().moveTo(targetX, targetY, targetZ, speed);
		}

		if (mob.getNavigation().isDone()) {
			return BTStatus.SUCCESS;
		}

		return BTStatus.RUNNING;
	}

	@Override
	public void stop() {
		mob.getNavigation().stop();
		super.stop();
	}

	@Nullable
	protected Vec3 getPosition() {
		return DefaultRandomPos.getPos(this.mob, 10, 7);
	}
}
