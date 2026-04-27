package architecture.goldenboughs_lib.api;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 *
 */
public final class Armors<T extends ArmorItem> implements Iterable<DeferredItem<T>> {
	private final @Nullable DeferredItem<T> chestplate;
	private final @Nullable DeferredItem<T> leggings;
	private final @Nullable DeferredItem<T> boots;
	private final Map<EquipmentSlot, @Nullable DeferredItem<T>> map;
	private final Set<@Nullable DeferredItem<T>> set;

	/**
	 * @param chestplate 胸
	 * @param leggings   腿
	 * @param boots      脚
	 */
	public Armors(
		@Nullable DeferredItem<T> chestplate,
		@Nullable DeferredItem<T> leggings,
		@Nullable DeferredItem<T> boots
	) {
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;

		Map<EquipmentSlot, @Nullable DeferredItem<T>> map = new HashMap<>(3);
		map.put(EquipmentSlot.CHEST, chestplate);
		map.put(EquipmentSlot.LEGS, leggings);
		map.put(EquipmentSlot.FEET, boots);
		this.map = Collections.unmodifiableMap(map);

		Set<@Nullable DeferredItem<T>> set = new HashSet<>(3);
		set.add(chestplate);
		set.add(leggings);
		set.add(boots);

		this.set = Collections.unmodifiableSet(set);
	}

	@Override
	public @NotNull Iterator<DeferredItem<T>> iterator() {
		return getSet().iterator();
	}

	public @NotNull Set<@Nullable DeferredItem<T>> getSet() {
		return set;
	}

	public @NotNull Map<EquipmentSlot, @Nullable DeferredItem<T>> getMap() {
		return map;
	}

	public @Nullable DeferredItem<T> chestplate() {
		return chestplate;
	}

	public @Nullable DeferredItem<T> leggings() {
		return leggings;
	}

	public @Nullable DeferredItem<T> boots() {
		return boots;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Armors<?>) obj;
		return Objects.equals(this.chestplate, that.chestplate) &&
			Objects.equals(this.leggings, that.leggings) &&
			Objects.equals(this.boots, that.boots);
	}

	@Override
	public int hashCode() {
		return Objects.hash(chestplate, leggings, boots);
	}

	@Override
	public String toString() {
		return "Armors[" +
			"chestplate=" + chestplate + ", " +
			"leggings=" + leggings + ", " +
			"boots=" + boots + ']';
	}

}