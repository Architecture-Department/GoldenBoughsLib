package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * 地面随机游走动作 - 在地面上随机选择一个位置移动
 * <p>使用 {@link LandRandomPos} 计算目标位置，适用于陆地生物</p>
 */
public class LandRandomStrollAction extends RandomStrollAction {
	public LandRandomStrollAction(PathfinderMob mob, double speedModifier, int interval) {
		super(mob, speedModifier, interval);
	}

	@Nullable
	protected Vec3 getPosition() {
		return LandRandomPos.getPos(this.mob, 15, 7);
	}
}
