package architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT
import java.util.function.Predicate
import java.util.function.Supplier

/**
 * 黑板变量，共享行为树数据
 */
@AllOpe
class Blackboard {
	val data = HashMap<KeyType<*>, Any>()

	/**
	 * 设置值
	 */
	fun <V> put(key: KeyType<V>, value: V) where V : Any {
		this.data[key] = value
	}

	/**
	 * 移除值
	 */
	fun remove(key: KeyType<*>) {
		this.data.remove(key)
	}

	/**
	 * 存在键
	 */
	fun containsKey(key: KeyType<*>): Boolean {
		return this.data.containsKey(key)
	}

	/**
	 * 存在值
	 */
	fun <V> containsValue(key: KeyType<V>, valuePredicate: Predicate<V>): Boolean {
		val value = this.get<V>(key)
		return valuePredicate.test(value)
	}

	/**
	 * 获取值
	 */
	operator fun <V> get(key: KeyType<V>): V {
		return (this.data[key] as? V) ?: throw NullPointerException("Blackboard value is null")
	}

	@JvmRecord
	data class ContainsKey(
		val holder: IBlackboardHolder,
		val key: KeyType<*>
	) : ConditionBT {
		override fun check(): Boolean {
			return holder.blackboard.containsKey(key)
		}
	}

	@JvmRecord
	data class ContainsValue<V>(
		val holder: IBlackboardHolder,
		val key: KeyType<V>,
		val valuePredicate: Predicate<V>
	) : ConditionBT where V : Any {
		override fun check(): Boolean {
			return holder.blackboard.containsValue<V>(key, valuePredicate)
		}
	}

	class SetValue<V>(
		val holder: IBlackboardHolder,
		val key: KeyType<V>,
		val valueSupplier: Supplier<V>
	) : BTNode() where V : Any {
		override fun execute(): BTStatus {
			holder.blackboard.put(key, valueSupplier.get())
			return BTStatus.SUCCESS
		}
	}

	class RemoveValue<V>(
		val holder: IBlackboardHolder,
		val key: KeyType<V>
	) : BTNode() where V : Any {
		override fun execute(): BTStatus {
			holder.blackboard.remove(key)
			return BTStatus.SUCCESS
		}
	}

	companion object {
		/**
		 * 创建一个新的黑板
		 */
		fun create(): Blackboard {
			return Blackboard()
		}

		/**
		 * 不存在键的条件
		 */
		fun <V> notContainsKey(holder: IBlackboardHolder, key: KeyType<V>): ConditionBT {
			return ConditionBT.not(containsKey<V>(holder, key))
		}

		/**
		 * 存在键的条件
		 */
		fun <V> containsKey(holder: IBlackboardHolder, key: KeyType<V>): ConditionBT {
			return ContainsKey(holder, key)
		}

		/**
		 * 不存在值的条件
		 */
		fun <V> notContainsValue(
			holder: IBlackboardHolder,
			key: KeyType<V>,
			valuePredicate: Predicate<V>
		): ConditionBT where V : Any {
			return ConditionBT.not(containsValue(holder, key, valuePredicate))
		}

		/**
		 * 存在值的条件
		 */
		fun <V> containsValue(
			holder: IBlackboardHolder,
			key: KeyType<V>,
			valuePredicate: Predicate<V>
		): ConditionBT where V : Any {
			return ContainsValue(holder, key, valuePredicate)
		}

		/**
		 * 设置值的行为节点
		 */
		fun <V> setValue(holder: IBlackboardHolder, key: KeyType<V>, valueSupplier: Supplier<V>): BTNode where V : Any {
			return SetValue(holder, key, valueSupplier)
		}

		/**
		 * 移除值的行为节点
		 */
		fun <V> removeValue(holder: IBlackboardHolder, key: KeyType<V>): BTNode where V : Any {
			return RemoveValue(holder, key)
		}
	}
}
