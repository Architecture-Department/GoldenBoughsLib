package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.util.client.ClientLibUtil
import io.netty.buffer.ByteBuf
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.Contract
import org.joml.Vector3d
import org.joml.Vector3fc
import java.util.*

object LibUtil {
	const val ID: String = "goldenboughs_lib"
	const val NAME: String = "GoldenBoughsLib"

	@JvmField
	val LOGGER: Logger = LogManager.getLogger(ID)

	@JvmField
	val OPTIONAL_RESOURCE_LOCATION_STREAM_CODEC: StreamCodec<ByteBuf, Optional<ResourceLocation>> =
		ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC)

	@JvmField
	val COMPONENT_SERIALIZATION_STREAM_CODEC_LIST: StreamCodec<RegistryFriendlyByteBuf, List<Component>> =
		ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list())

	@JvmField
	val ITEM_DISPLAY_CONTEXT_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ItemDisplayContext> =
		ByteBufCodecs.fromCodecWithRegistries(ItemDisplayContext.CODEC)

	@JvmField
	val COMPOUND_TAG_CODEC: StreamCodec<ByteBuf, CompoundTag> =
		ByteBufCodecs.fromCodec(CompoundTag.CODEC)

	@JvmField
	val LIST_COMPOUND_TAG_CODEC: StreamCodec<ByteBuf, List<CompoundTag>> =
		COMPOUND_TAG_CODEC.apply(ByteBufCodecs.list())

	@JvmField
	val MAP_RESOURCE_LOCATION_COMPOUND_TAG_CODEC: StreamCodec<ByteBuf, Map<ResourceLocation, CompoundTag>> =
		ByteBufCodecs.map({ HashMap() }, ResourceLocation.STREAM_CODEC, COMPOUND_TAG_CODEC)

	@JvmStatic
	fun isClientSingleplayer(): Boolean =
		if (FMLEnvironment.dist.isClient) ClientLibUtil.isClientSingleplayer()
		else false

	@JvmStatic
	fun getTextureLight(path: ResourceLocation): ResourceLocation {
		return getTextureLight(path.toString())
	}

	@JvmStatic
	fun getTextureLight(path: String): ResourceLocation {
		return rlOf(path.substring(0, path.lastIndexOf(".png")) + "_glowmask.png")
	}

	@JvmStatic
	fun rlOf(namespace: String, path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(namespace, path)
	}

	@JvmStatic
	fun rlOf(location: String): ResourceLocation {
		return ResourceLocation.parse(location)
	}

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

fun Vector3fc.toVector3d(): Vector3d = get(Vector3d())
