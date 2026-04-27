package architecture.goldenboughs_lib.core.capability.item;

import architecture.goldenboughs_lib.api.LcDamageType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 该类主要针对物品的近战攻击
 * <p>
 * 如果你可以直接返回{@link DamageSource}请使用{@link IDamageSource#imaginarycraft$setLcDamageType(LcDamageType)}
 */
public interface IItemLcDamageType {
	/**
	 * 获取物品当前的伤害类型
	 */
	@Nullable
	LcDamageType getLcDamageType(ItemStack stack);

	/**
	 * 获取可以造成的伤害类型 一般用于描述
	 */
	@NotNull
	default Set<LcDamageType> getCanCauseLcDamageTypes(final ItemStack stack) {
		return Set.of();
	}
}
