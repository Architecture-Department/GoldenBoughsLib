package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.common.components.ItemVirtueUsageReq;
import architecture.goldenboughs_lib.core.Lib;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class LibDataComponentTypes {
	public static final DeferredRegister<DataComponentType<?>> REGISTRY = Lib.modRegister(BuiltInRegistries.DATA_COMPONENT_TYPE);

	public static final Supplier<DataComponentType<LcDamageType.Component>> LC_DAMAGE_TYPE = register("lobotomy_corporation_damage_type",
		LcDamageType.Component.CODEC, LcDamageType.Component.STREAM_CODEC, true);

	public static final Supplier<DataComponentType<Boolean>> MODE_BOOLEAN = recordBoolean("mode_boolean", true);
	/**
	 * 是否正在受到抑制器的影响属性
	 */
	public static final Supplier<DataComponentType<Boolean>> IS_RESTRAIN = recordBoolean("is_restrain", true);
	/**
	 * 物品四德属性能力使用要求
	 */
	public static final Supplier<DataComponentType<ItemVirtueUsageReq>> ITEM_VIRTUE_USAGE_REQ = register("item_virtue_usage_req",
		ItemVirtueUsageReq.CODEC, ItemVirtueUsageReq.STREAM_CODEC, true);

	private static Supplier<DataComponentType<Boolean>> recordBoolean(String name, boolean isCacheEncoding) {
		return register(name, Codec.BOOL, ByteBufCodecs.BOOL, isCacheEncoding);
	}

	private static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean isCacheEncoding) {
		return register(name, builder -> {
			builder.persistent(codec).networkSynchronized(streamCodec);
			if (isCacheEncoding) {
				builder.cacheEncoding();
			}
			return builder;
		});
	}

	private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
		return register(name, () -> builder.apply(DataComponentType.builder()).build());
	}

	private static <B extends DataComponentType<?>> DeferredHolder<DataComponentType<?>, B> register(String name, Supplier<? extends B> builder) {
		return LibDataComponentTypes.REGISTRY.register("data_components." + name, builder);
	}

	private static Supplier<DataComponentType<String>> recordString(String name, boolean isCacheEncoding) {
		return register(name, Codec.STRING, ByteBufCodecs.STRING_UTF8, isCacheEncoding);
	}
}
