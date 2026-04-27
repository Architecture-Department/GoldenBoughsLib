package architecture.goldenboughs_lib.eventexecute;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.util.LcLevelUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;

// TODO 精神伤害之类的不能被原版防御处理
public final class LcDamageEventExecutes {
	public static final double VULNERABILITY_DECELERATE_THRESHOLD = 1.2;

	/**
	 * 根据目标护甲等级与攻击者等级计算伤害减免倍率
	 */
	public static void levelReduction(LivingEntity entity, DamageContainer damageContainer, DamageSource damageSource) {
		LcLevel attackerLevel = damageSource.goldenboughs_lib$getLcDamageLevel();
		if (attackerLevel == null) {
			return;
		}

		int armorLcLaval = 0; // 盔甲等级总和
		int armorNumber = 0; // 护甲数量
		int voidLcLevelArmorNumber = 0; // 无等级护甲数量

		// 遍历所有护甲槽位，统计护甲等级和数量
		for (final ItemStack armorItemStack : entity.getArmorAndBodyArmorSlots()) {
			if (armorItemStack == null || armorItemStack.isEmpty()) {
				continue;
			}
			// 获取盔甲等级
			@Nullable LcLevel level = LcLevelUtil.getLevel(armorItemStack);
			if (level == null) {
				voidLcLevelArmorNumber++;
			} else {
				armorLcLaval += level.getLevelValue();
			}
			armorNumber++;
		}

		if (armorNumber == 0) {
			damageContainer.setNewDamage(ontologyLevelCalculate(entity, attackerLevel, damageContainer.getOriginalDamage()));
			return;
		}

		// 所有护甲都无等级时设置特殊标记值
		if (voidLcLevelArmorNumber == armorNumber) {
			armorLcLaval = -1;
		}

		// 计算平均护甲等级
		if (armorLcLaval != -1) {
			armorLcLaval /= armorNumber;
		}

		damageContainer.setNewDamage(damageContainer.getOriginalDamage() * LcLevelUtil.getDamageMultiple(LcLevel.byLevel(armorLcLaval), attackerLevel));
	}

	/**
	 * 本体计算
	 */
	private static float ontologyLevelCalculate(final LivingEntity entity, @Nullable final LcLevel attackerLevel, final float damage) {
		return damage * LcLevelUtil.getDamageMultiple(LcLevelUtil.getLevel(entity), attackerLevel);
	}

	/**
	 * 计算易伤属性对最终伤害的加成效果
	 */
	public static void vulnerableReduction(LivingEntity entity, DamageContainer damageContainer, DamageSource damageSource) {
		LcDamageType lcDamageType = damageSource.goldenboughs_lib$getLcDamageType();
		float newDamage = damageContainer.getNewDamage();

		// 检查是否存在自定义伤害类型
		if (lcDamageType != null) {
			// 获取实体的易伤属性值并应用到伤害计算
			Holder<Attribute> vulnerable = lcDamageType.getVulnerable();
			AttributeInstance attributeInstance = entity.getAttribute(vulnerable);
			if (attributeInstance != null) {
				newDamage *= (float) attributeInstance.getValue();
			} else {
				newDamage *= (float) vulnerable.value().getDefaultValue();
			}
		}

		damageContainer.setNewDamage(newDamage);
	}

	/**
	 * 灵魂伤害计算
	 */
	public static float theSoulDamage(float damage, LivingEntity attackedEntity, @Nullable Entity sourceEntity, DamageSource damageSource) {
//    damage /= 100;
//    float maxHealth = 0;
//	  @Nullable LcLevel attackedLevel = LcLevelUtil.getLevel(attackedEntity);
//	  @Nullable LcLevel attackerLevel = damageSource.goldenboughs_lib$getLcDamageLevel();
//    if (sourceEntity instanceof LivingEntity living) {
//      maxHealth = (float) living.getAttributeValue(Attributes.MAX_HEALTH);
//    }
//
//    // 如果未获取到最大生命值，则使用默认值20
//    if (maxHealth == 0) {
//      maxHealth = 20;
//    }
//    // TODO 重新调整灵魂伤害算法
//    // 根据伤害等级差异计算最终伤害
//    return damage * (maxHealth / 5) * LcLevelUtil.getDamageMultiple(attackedLevel, attackerLevel);
		if (attackedEntity instanceof Player) {
			return damage * attackedEntity.getMaxHealth();
		}
		return damage;
	}

	/**
	 * 护盾处理
	 */
	public static void absorptionShield(LivingDamageEvent.Pre event, LivingEntity attackedEntity, LcDamageType lcDamageType) {
		if (attackedEntity.level().isClientSide) {
			return;
		}

		for (var entry : architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry.getAll()) {
			MobEffectInstance effect = attackedEntity.getEffect(entry.effect());

			if (effect == null) continue;
			if (lcDamageType != null && !lcDamageType.getDamageTypeResourceKey().location().equals(entry.damageTypeTag())) {
				continue;
			}

			float current = attackedEntity.getData(entry.attachment().get());//护盾量
			if (current <= 0) continue;

			float original = event.getNewDamage();//伤害量
			if (original <= 0) continue;
			float absorbed = Math.min(current, original);
			float remaining = original - absorbed;//剩余伤害

			float newAmount = current - absorbed;//新护盾量
			attackedEntity.setData(entry.attachment().get(), newAmount);//保存护盾量

			if (newAmount <= 0) {
				attackedEntity.removeEffect(entry.effect());
				if (attackedEntity instanceof Player player) {
					entry.playShieldBreakSound(player);      // 只对该玩家播放
				}
				if (architecture.goldenboughs_lib.config.LibConfig.SERVER.enableShieldDamageImmunity.isTrue()) {
					event.setNewDamage(0);
					continue;//碎盾抗一下(只抗对应伤害)
				}
			}

			if (remaining <= 0) {
				event.setNewDamage(0);
			} else {
				event.setNewDamage(remaining);
			}
		}
	}
}
