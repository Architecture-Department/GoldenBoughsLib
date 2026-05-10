package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.util.DefaultRandomPos
import net.minecraft.world.phys.Vec3
import java.util.*

/**
 * 移动到位置动作 - 实体使用寻路系统移动到指定坐标
 * 
 * 支持设置目标点和移动速度，到达后返回成功
 */
class MoveToPosAction(
	private val mob: PathfinderMob,
	private val speed: Double
) : BTNode() {
	private var targetX = 0.0
	private var targetY = 0.0
	private var targetZ = 0.0

	init {
		setFlags(EnumSet.of(Flag.MOVE))
	}

	fun setTarget(x: Double, y: Double, z: Double): MoveToPosAction {
		this.targetX = x
		this.targetY = y
		this.targetZ = z
		return this
	}

	override fun execute(): BTStatus? {
		if (mob.getNavigation().isDone) {
			mob.getNavigation().moveTo(targetX, targetY, targetZ, speed)
		}

		if (mob.getNavigation().isDone) {
			return BTStatus.SUCCESS
		}

		return BTStatus.RUNNING
	}

	override fun stop() {
		mob.getNavigation().stop()
		super.stop()
	}

	protected val position: Vec3?
		get() = DefaultRandomPos.getPos(this.mob, 10, 7)
}
