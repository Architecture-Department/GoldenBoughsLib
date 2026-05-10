package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import net.minecraft.util.RandomSource

/**
 * 随机时长等待动作 - 在指定范围内随机等待一定时间
 * 
 * 继承自 [WaitAction]，每次重启时会重新随机化等待时长
 */
class RandomWaitAction(
	minInclusive: Int,
	maxInclusive: Int,
	random: RandomSource
) : WaitAction(minInclusive) {
	private val waitTicksApplier: Runnable

	init {
		require(maxInclusive > minInclusive) { "max must greater than min" }
		this.waitTicksApplier = Runnable { this.waitTicks = random.nextIntBetweenInclusive(minInclusive, maxInclusive) }
		waitTicksApplier.run()
	}

	override fun cleanup() {
		super.cleanup()
		waitTicksApplier.run()
	}
}
