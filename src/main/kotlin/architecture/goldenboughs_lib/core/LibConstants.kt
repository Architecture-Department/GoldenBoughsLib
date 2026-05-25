package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.util.LibUtil.rlOf
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.Contract

object LibConstants {
	const val ID: String = "goldenboughs_lib"
	const val NAME: String = "GoldenBoughsLib"

	@JvmField
	val LOGGER: Logger = LogManager.getLogger(ID)

	// 例如道具类的物品等没有特殊分类的物品，例如：安吉拉的图书馆
	@JvmField
	val EGO: MutableSet<DeferredItem<out Item>> = HashSet()

	@JvmStatic
	@Contract("_ -> new")
	fun modRl(name: String): ResourceLocation {
		return rlOf(ID, name)
	}

	@JvmStatic
	@Contract(pure = true)
	fun modRlText(name: String): String {
		return "$ID:$name"
	}

	@JvmStatic
	fun <T> modRegister(registry: Registry<T>): DeferredRegister<T> {
		return DeferredRegister.create<T>(registry, ID)
	}

	@JvmStatic
	fun <T> modRegister(registry: ResourceKey<Registry<T>>): DeferredRegister<T> {
		return DeferredRegister.create<T>(registry, ID)
	}
}
