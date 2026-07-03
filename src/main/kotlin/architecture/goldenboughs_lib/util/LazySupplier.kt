package architecture.goldenboughs_lib.util

import java.util.function.Supplier

class LazySupplier<T>(private val factory: () -> T?) : Supplier<T?> {
	var init: Boolean = false; private set
	private var cached: T? = null

	override fun get(): T? {
		if (!init) init()
		return cached
	}

	fun init() {
		cached = factory()
		init = true
	}
}
