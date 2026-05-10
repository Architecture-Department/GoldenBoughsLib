package architecture.goldenboughs_lib.api

import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ArmorItem
import net.neoforged.neoforge.registries.DeferredItem
import java.util.*

data class Armors<T : ArmorItem?>(
	val chestplate: DeferredItem<T>?,
	val leggings: DeferredItem<T>?,
	val boots: DeferredItem<T>?
) : Iterable<DeferredItem<T>?> {
	val map: Map<EquipmentSlot, DeferredItem<T>?>
	val set: Set<DeferredItem<T>?>

	/**
	 * @param chestplate 胸
	 * @param leggings   腿
	 * @param boots      脚
	 */
	init {
		val map = HashMap<EquipmentSlot, DeferredItem<T>?>(3)
		map[EquipmentSlot.CHEST] = chestplate
		map[EquipmentSlot.LEGS] = leggings
		map[EquipmentSlot.FEET] = boots
		this.map = Collections.unmodifiableMap(map)

		val set = HashSet<DeferredItem<T>?>(3)
		set.add(chestplate)
		set.add(leggings)
		set.add(boots)

		this.set = Collections.unmodifiableSet(set)
	}

	override fun iterator(): Iterator<DeferredItem<T>?> {
		return this.set.iterator()
	}

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other == null || other.javaClass != this.javaClass) return false
		val that = other as Armors<*>
		return this.chestplate == that.chestplate &&
			this.leggings == that.leggings &&
			this.boots == that.boots
	}

	override fun hashCode(): Int {
		return Objects.hash(chestplate, leggings, boots)
	}

	override fun toString(): String {
		return "Armors[" +
			"chestplate=" + chestplate + ", " +
			"leggings=" + leggings + ", " +
			"boots=" + boots + ']'
	}
}