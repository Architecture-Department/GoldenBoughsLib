package architecture.goldenboughs_lib.mixed;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.api.NoMixinException;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IDamageSource {
	static IDamageSource of(DamageSource obj) {
		return obj;
	}

	/**
	 * 设置武器物品
	 *
	 * @param itemStack 武器物品
	 */
	default void goldenboughs_lib$setWeaponItem(ItemStack itemStack) {
		throw new NoMixinException();
	}

	/**
	 * 获取LC伤害类型
	 *
	 * @return LC伤害类型，如果返回空则不受到抗性影响
	 */
	@Nullable
	default LcDamageType goldenboughs_lib$getLcDamageType() {
		throw new NoMixinException();
	}

	/**
	 * 设置LC伤害类型
	 *
	 * @param type LC伤害类型
	 */
	default void goldenboughs_lib$setLcDamageType(LcDamageType type) {
		throw new NoMixinException();
	}

	/**
	 * 获取LC伤害等级
	 *
	 * @return LC伤害等级，如果返回空则不受到等级影响
	 */
	@Nullable
	default LcLevel goldenboughs_lib$getLcDamageLevel() {
		throw new NoMixinException();
	}

	/**
	 * 设置LC伤害等级
	 *
	 * @param pmLevel LC伤害等级
	 */
	default void goldenboughs_lib$setDamageLevel(@Nullable LcLevel pmLevel) {
		throw new NoMixinException();
	}
}
