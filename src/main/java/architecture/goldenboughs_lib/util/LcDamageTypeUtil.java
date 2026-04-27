package architecture.goldenboughs_lib.util;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.init.LibDataComponents;
import architecture.imaginarycraft.core.capability.item.IItemLcDamageType;
import architecture.imaginarycraft.init.ModCapabilitys;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class LcDamageTypeUtil {
	/**
	 * 获取伤害物品
	 */
	@Nullable
	public static ItemStack getDamageItemStack(@NotNull DamageSource damageSource) {
		return damageSource.getWeaponItem();
	}

	@Nullable
	public static LcDamageType getLcDamageType(ItemStack itemStack) {
		IItemLcDamageType colorDamageTypeItem = getLcDamageTypeCapability(itemStack);
		if (colorDamageTypeItem != null) {
			return colorDamageTypeItem.getLcDamageType(itemStack);
		}

		if (itemStack.getItem() instanceof IItemLcDamageType iItemLcDamageType) {
			return iItemLcDamageType.getLcDamageType(itemStack);
		}

		return LcDamageType.PHYSICS;
	}

	@Nullable
	public static IItemLcDamageType getLcDamageTypeCapability(ItemStack itemStack) {
		return itemStack.getCapability(ModCapabilitys.LC_DAMAGE_TYPE_ITEM);
	}

	@NotNull
	public static Set<LcDamageType> getCanCauseLcDamageTypes(ItemStack itemStack) {
		if (itemStack.has(LibDataComponents.LC_DAMAGE_TYPE)) {
			LcDamageType.Component component = itemStack.get(LibDataComponents.LC_DAMAGE_TYPE);
			return component == null ? Set.of() : component.canCauseLcDamageTypes();
		}
		if (itemStack.getItem() instanceof IItemLcDamageType iItemLcDamageType) {
			return iItemLcDamageType.getCanCauseLcDamageTypes(itemStack);
		}
		return Set.of();
	}
}
