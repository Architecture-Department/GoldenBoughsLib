package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.util.EntityUtil.sphere
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3

/**
 * 随机环顾动作 - 实体随机看向周围某个方向
 * 
 * 在实体周围 1 格半径的球面上随机选择观察点
 */
class RandomLookAction(val mob: Mob) : BTNode() {
	var pos: Vec3? = null

	override fun start() {
		super.start()
		pos = mob.eyePosition.add(sphere(1f, mob.getRandom().nextFloat() * 6.28f, Math.PI.toFloat() * 0.5f))
	}

	override fun execute(): BTStatus? {
		if (pos != null) {
			mob.lookAt(EntityAnchorArgument.Anchor.EYES, pos!!)
			mob.getLookControl().setLookAt(pos!!)
		}

		return BTStatus.RUNNING
	}
}
