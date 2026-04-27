package architecture.goldenboughs_lib.common.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;

public class MinAttribute extends Attribute {
	private final double minValue;

	public MinAttribute(final String descriptionId, final double defaultValue, final double minValue) {
		super(descriptionId, defaultValue);

		assert defaultValue >= minValue : "Min value [" + minValue + "] must be less than default value [" + defaultValue + "]";
		this.minValue = minValue;
	}

	public double getMinValue() {
		return minValue;
	}

	@Override
	public double sanitizeValue(final double value) {
		return Math.max(this.minValue, value);
	}
}
