package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.util.LandRandomPos
import net.minecraft.world.phys.Vec3

/**
 * 地面随机游走动作 - 在地面上随机选择一个位置移动
 * 
 * 使用 [LandRandomPos] 计算目标位置，适用于陆地生物
 */
class LandRandomStrollAction(
	mob: PathfinderMob,
	speedModifier: Double,
	interval: Int
) : RandomStrollAction(mob, speedModifier, interval) {

	override val position: Vec3?
		get() = LandRandomPos.getPos(this.mob, 15, 7)
}
