package architecture.goldenboughs_lib.common.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;

public class MaxAttribute extends Attribute {
	private final double maxValue;

	public MaxAttribute(final String descriptionId, final double defaultValue, final double maxValue) {
		super(descriptionId, defaultValue);
		assert defaultValue < maxValue : "Max value [" + maxValue + "] must be greater than default value [" + defaultValue + "]";
		this.maxValue = maxValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	@Override
	public double sanitizeValue(final double value) {
		return Math.min(value, this.maxValue);
	}
}
