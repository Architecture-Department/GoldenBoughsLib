package architecture.goldenboughs_lib.util

import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class LazySupplier<T>(
	val id: ResourceLocation,
	private val factory: () -> T?
) : Supplier<T?> {
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
