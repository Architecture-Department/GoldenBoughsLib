package architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard

import com.mojang.serialization.Codec

/**
 * 
 * 黑板变量键类型
 * 
 * 已定义常见的黑板变量键类型，也可以自定义新的键类型
 * 
 * @param key   唯一标识
 * @param codec 编解码器
 * @param <V>   值类型
</V> */
@JvmRecord
data class KeyType<V>(
	val key: String,
	val codec: Codec<V>?
) where V : Any {
	override fun toString(): String {
		return key
	}

	override fun equals(o: Any?): Boolean {
		if (o == null || javaClass != o.javaClass) return false
		val keyType = o as KeyType<*>
		return key == keyType.key
	}

	init {
		ALL_KEYS[key] = this
	}

	companion object {
		@JvmField
		val STAGE: KeyType<Int> = of("stage", Codec.INT)

		@JvmField
		val TIME: KeyType<Int> = of("time", Codec.INT)

		@JvmField
		val IS_RUNNING: KeyType<Boolean> = of("is_running", Codec.BOOL)

		@JvmStatic
		private val ALL_KEYS: MutableMap<String, KeyType<*>> = HashMap()


		fun <V> of(key: String): KeyType<V> where V : Any {
			return KeyType(key, null)
		}

		fun <V> of(key: String, codec: Codec<V>): KeyType<V> where V : Any {
			return KeyType(key, codec)
		}

		fun <V> getById(key: String): KeyType<V> where V : Any {
			return ALL_KEYS[key] as? KeyType<V> ?: throw IllegalArgumentException("No key found for $key")
		}
	}
}
