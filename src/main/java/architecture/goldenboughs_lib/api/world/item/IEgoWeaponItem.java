package architecture.goldenboughs_lib.api.world.item;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.core.capability.item.IItemLcDamageType;
import architecture.goldenboughs_lib.core.capability.item.IItemUsageReq;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 所有E.G.O武器都应该继承这个
 */
public interface IEgoWeaponItem extends IEgoItem, IItemUsageReq, IItemLcDamageType {
	static Item.Properties add(Item.Properties properties, Builder<?> builder) {
		return IEgoItem.add(properties.attributes(builder.getItemAttributeModifiers()), builder);
	}

	/**
	 * 武器属性构造器
	 */
	class Builder<T extends Builder<T>> extends IEgoItem.Builder<T> {
		public @Nullable LcDamageType lcDamageType;
		public Set<LcDamageType> canCauseLcDamageTypes = Set.of();
		public float weaponDamage;

		public T damage(float weaponDamageValue) {
			this.weaponDamage = weaponDamageValue;
			return self();
		}

		public T meleeLcDamageType(LcDamageType meleeLcDamageType, Set<LcDamageType> lcDamageTypesThatCanBeCaused) {
			this.lcDamageType = meleeLcDamageType;
			this.canCauseLcDamageTypes = lcDamageTypesThatCanBeCaused;
			return self();
		}

		public T meleeLcDamageType(LcDamageType meleeLcDamageType) {
			this.lcDamageType = meleeLcDamageType;
			this.canCauseLcDamageTypes = Set.of(meleeLcDamageType);
			return self();
		}

		public T meleeLcDamageType(LcDamageType meleeLcDamageType, LcDamageType... lcDamageTypesThatCanBeCaused) {
			this.lcDamageType = meleeLcDamageType;
			this.canCauseLcDamageTypes = Set.of(lcDamageTypesThatCanBeCaused);
			return self();
		}

		public ItemAttributeModifiers getItemAttributeModifiers() {
			return ItemAttributeModifiers.builder().build();
		}
	}
}
