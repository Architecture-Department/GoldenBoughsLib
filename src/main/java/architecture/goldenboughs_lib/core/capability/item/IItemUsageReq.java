package architecture.goldenboughs_lib.core.capability.item;

import architecture.goldenboughs_lib.common.components.ItemVirtueUsageReq;
import architecture.goldenboughs_lib.init.LibDataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 带有需求的物品 如果不满足要求则触发
 * <p>
 * 一般捆绑{@link ItemVirtueUsageReq}或{@link LibDataComponents#ITEM_VIRTUE_USAGE_REQ}
 */
public interface IItemUsageReq {
	/**
	 * 使用物品时触发
	 */
	default void useImpede(ItemStack itemStack, Level level, LivingEntity entity) {

	}

	/**
	 * 攻击时触发
	 */
	default void attackImpede(ItemStack itemStack, Level level, LivingEntity entity) {

	}

	/**
	 * 在手上时触发
	 */
	default void onTheHandImpede(ItemStack itemStack, Level level, LivingEntity entity) {

	}

	/**
	 * 物品在背包时里触发
	 */
	default void inTheBackpackImpede(ItemStack itemStack, Level level, LivingEntity entity) {

	}

	/**
	 * 在装备里时触发，如盔甲，饰品
	 */
	default void equipmentImpede(ItemStack itemStack, Level level, LivingEntity entity) {

	}
}
