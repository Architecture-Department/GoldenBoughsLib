package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.common.data_component.ItemVirtueUsageReq
import architecture.goldenboughs_lib.core.Lib.modRegister
import com.mojang.serialization.Codec
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier
import java.util.function.UnaryOperator

object LibDataComponentTypes {
	@JvmField
	val REGISTRY: DeferredRegister<DataComponentType<*>> =
		modRegister(BuiltInRegistries.DATA_COMPONENT_TYPE)

	@JvmField
	val LC_DAMAGE_TYPE: Supplier<DataComponentType<LcDamageType.Component>> = register(
		"lobotomy_corporation_damage_type", LcDamageType.Component.CODEC, LcDamageType.Component.STREAM_CODEC, true
	)

	@JvmField
	val MODE_BOOLEAN: Supplier<DataComponentType<Boolean>> =
		recordBoolean("mode_boolean", true)

	/**
	 * 是否正在受到抑制器的影响属性
	 */
	@JvmField
	val IS_RESTRAIN: Supplier<DataComponentType<Boolean>> =
		recordBoolean("is_restrain", true)

	/**
	 * 物品四德属性能力使用要求
	 */
	@JvmField
	val ITEM_VIRTUE_USAGE_REQ: Supplier<DataComponentType<ItemVirtueUsageReq>> = register(
		"item_virtue_usage_req", ItemVirtueUsageReq.CODEC, ItemVirtueUsageReq.STREAM_CODEC, true
	)

	private fun recordBoolean(name: String, isCacheEncoding: Boolean): Supplier<DataComponentType<Boolean>> {
		return register(name, Codec.BOOL, ByteBufCodecs.BOOL, isCacheEncoding)
	}

	private fun <T> register(
		name: String, codec: Codec<T>, streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>, isCacheEncoding: Boolean
	): Supplier<DataComponentType<T>> {
		return register(name) { builder ->
			builder.persistent(codec).networkSynchronized(streamCodec)
			if (isCacheEncoding) {
				builder.cacheEncoding()
			}
			builder
		}
	}

	private fun <T> register(
		name: String, builder: UnaryOperator<DataComponentType.Builder<T>>
	): Supplier<DataComponentType<T>> {
		return register<DataComponentType<T>>(
			name, Supplier { builder.apply(DataComponentType.builder<T>()).build() })
	}

	private fun <B : DataComponentType<*>> register(
		name: String, builder: Supplier<out B>
	): DeferredHolder<DataComponentType<*>, B> {
		return REGISTRY.register("data_components.$name", builder)
	}

	private fun recordString(name: String, isCacheEncoding: Boolean): Supplier<DataComponentType<String>> {
		return register(name, Codec.STRING, ByteBufCodecs.STRING_UTF8, isCacheEncoding)
	}
}
