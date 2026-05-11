package architecture.goldenboughs_lib.api

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.function.BiFunction

object ModByteBufCodecs {
	@JvmStatic
	fun <B : ByteBuf, T : Map.Entry<K, V>, K, V> entry(
		entry: BiFunction<K, V, out T>,
		key: StreamCodec<in B, K>,
		value: StreamCodec<in B, V>
	): StreamCodec<in B, T> {
		return object : StreamCodec<B, T> {
			override fun decode(buf: B): T {
				return entry.apply(key.decode(buf), value.decode(buf))
			}

			@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "TYPE_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
			override fun encode(buf: B, pair: T) {
				key.encode(buf, pair.key)
				value.encode(buf, pair.value)
			}
		}
	}
}
