package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import net.minecraft.util.RandomSource;

/**
 * 随机时长等待动作 - 在指定范围内随机等待一定时间
 * <p>继承自 {@link WaitAction}，每次重启时会重新随机化等待时长</p>
 */
public class RandomWaitAction extends WaitAction {
	private final Runnable waitTicksApplier;

	public RandomWaitAction(int minInclusive, int maxInclusive, RandomSource random) {
		super(minInclusive);
		if (maxInclusive <= minInclusive) {
			throw new IllegalArgumentException("max must greater than min");
		}
		this.waitTicksApplier = () -> this.waitTicks = random.nextIntBetweenInclusive(minInclusive, maxInclusive);
		waitTicksApplier.run();
	}

	@Override
	protected void cleanup() {
		super.cleanup();
		waitTicksApplier.run();
	}
}
