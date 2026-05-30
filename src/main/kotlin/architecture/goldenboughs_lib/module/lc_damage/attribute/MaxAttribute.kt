package architecture.goldenboughs_lib.module.lc_damage.attribute

import net.minecraft.world.entity.ai.attributes.Attribute
import kotlin.math.min

class MaxAttribute(
	descriptionId: String,
	defaultValue: Double,
	maxValue: Double
) :
	Attribute(descriptionId, defaultValue) {
	val maxValue: Double

	init {
		assert(defaultValue < maxValue) { "Max value [$maxValue] must be greater than default value [$defaultValue]" }
		this.maxValue = maxValue
	}

	override fun sanitizeValue(value: Double): Double {
		return min(value, this.maxValue)
	}
}
