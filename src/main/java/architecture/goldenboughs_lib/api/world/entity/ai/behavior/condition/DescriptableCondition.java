package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import org.jetbrains.annotations.Nullable;

/**
 * 可描述条件抽象类
 */
public abstract class DescriptableCondition implements ConditionBT {
	private String description;

	public ConditionBT setConDesc(String description) {
		this.description = description;
		return this;
	}

	@Override
	public boolean check() {
		return false;
	}

	@Override
	public @Nullable String getDesc() {
		return description;
	}
}
