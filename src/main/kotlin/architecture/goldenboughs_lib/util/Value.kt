package architecture.goldenboughs_lib.util

data class Value<T>(
	val oldValue: T,
	var newValue: T
) {
	companion object {
		@JvmStatic
		fun <T> of(value: T): Value<T> {
			return Value(value, value)
		}
	}
}