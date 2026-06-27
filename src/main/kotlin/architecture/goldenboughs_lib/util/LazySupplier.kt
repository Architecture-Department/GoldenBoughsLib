package architecture.goldenboughs_lib.util

import java.util.function.Supplier

class LazySupplier<T>(private val factory: () -> T) : Supplier<T> {
	private lateinit var cached: Any

	override fun get(): T {
		if (!::cached.isInitialized) {
			cached = factory()!!
		}

		return cached as T
	}
}
