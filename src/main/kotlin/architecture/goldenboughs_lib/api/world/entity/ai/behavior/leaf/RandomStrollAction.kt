package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.util.Mth
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.util.AirRandomPos
import net.minecraft.world.phys.Vec3
import java.util.*

/**
 * 空中随机游走动作 - 飞行生物在空中随机移动
 * 
 * 使用 [AirRandomPos] 计算目标位置，按固定间隔重新选择路径点
 */
class RandomStrollAction(
	protected val mob: PathfinderMob,
	protected val speedModifier: Double,
	protected val _interval: Int
) : BTNode() {
	protected var interval: Int = 0

	init {
		this.setFlags(EnumSet.of(Flag.MOVE))
	}

	override fun execute(): BTStatus {
		if (++this.interval > _interval) {
			return BTStatus.SUCCESS
		}

		return BTStatus.RUNNING
	}

	override fun start() {
		super.start()
		this.interval = this.mob.getRandom().nextInt(this._interval / 4)
		val vec3 = this.position
		if (vec3 != null) {
			this.mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier)
		}
	}

	protected val position: Vec3?
		get() = AirRandomPos.getPosTowards(
			mob,
			10,
			5,
			1,
			mob.blockPosition().bottomCenter,
			(Mth.PI * 0.1f).toDouble()
		)

	override fun stop() {
		this.interval = 0
		this.mob.getNavigation().stop()
		super.stop()
	}
}
