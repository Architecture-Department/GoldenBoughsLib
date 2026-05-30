package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.util.LibUtil.rlOf
import io.netty.buffer.ByteBuf
import net.minecraft.core.Registry
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.Contract
import java.util.*

object LibConstants {
	const val ID: String = "goldenboughs_lib"
	const val NAME: String = "GoldenBoughsLib"

	@JvmField
	val LOGGER: Logger = LogManager.getLogger(ID)

	@JvmField
	val OPTIONAL_RESOURCE_LOCATION_STREAM_CODEC: StreamCodec<ByteBuf, Optional<ResourceLocation>> =
		ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC)

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
