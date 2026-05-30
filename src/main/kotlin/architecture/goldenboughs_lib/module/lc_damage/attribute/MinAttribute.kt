package architecture.goldenboughs_lib.module.lc_damage.attribute

import net.minecraft.world.entity.ai.attributes.Attribute
import kotlin.math.max

class MinAttribute(
	descriptionId: String,
	defaultValue: Double,
	minValue: Double
) :
	Attribute(descriptionId, defaultValue) {
	val minValue: Double

	init {
		assert(defaultValue >= minValue) { "Min value [$minValue] must be less than default value [$defaultValue]" }
		this.minValue = minValue
	}

	override fun sanitizeValue(value: Double): Double {
		return max(this.minValue, value)
	}
}
