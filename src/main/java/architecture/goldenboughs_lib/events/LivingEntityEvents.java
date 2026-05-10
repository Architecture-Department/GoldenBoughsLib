package architecture.goldenboughs_lib.events;

import architecture.goldenboughs_lib.api.DelayTaskHolder;
import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.common.payload.toc.PlayerDamagePayload;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.eventexecute.LcDamageEventExecutes;
import architecture.goldenboughs_lib.init.LibAttachments;
import architecture.goldenboughs_lib.util.GunWeaponUtil;
import architecture.goldenboughs_lib.util.LcLevelUtil;
import architecture.goldenboughs_lib.util.ParticleUtil;
import architecture.goldenboughs_lib.util.RationalityUtil;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN;

@EventBusSubscriber(modid = GoldenBoughsLib.ID)
public final class LivingEntityEvents {
	/**
	 * 恢复事件
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void entityHealEvent(LivingHealEvent event) {
		float amount = event.getAmount();
		LivingEntity entity = event.getEntity();

		if (amount > 0) {
			ParticleUtil.createDamageTextParticles(entity, ParticleUtil.getText(amount), false, true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void livingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
		LivingEntity entity = event.getEntity();
		EquipmentSlot slot = event.getSlot();

		if (entity.isAlive()) {
			DelayTaskHolder delayTaskHolder = entity.getExistingDataOrNull(LibAttachments.DELAY_TASK_HOLDER);
			if (delayTaskHolder != null && !ItemStack.isSameItem(event.getFrom(), event.getTo())) {
				delayTaskHolder.removeTask(slot);
			}

			if (slot.getType() == EquipmentSlot.Type.HAND) {
				if (entity instanceof Player player) {
					boolean isHandUsed = slot == EquipmentSlot.MAINHAND;
					GunWeaponUtil.setIsAttack(player, true, isHandUsed);
					GunWeaponUtil.resetChargeUp(player, isHandUsed);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void tickPre(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		if (entity.isAlive()) {
			DelayTaskHolder timingRun = entity.getExistingDataOrNull(LibAttachments.DELAY_TASK_HOLDER);
			if (timingRun != null) {
				timingRun.tick();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void livingSwapItemsEvent(LivingSwapItemsEvent.Hands event) {
		LivingEntity livingEntity = event.getEntity();
		if (livingEntity.isAlive()) {
			ItemStack itemSwappedToMainHand = event.getItemSwappedToMainHand();
			ItemStack itemSwappedToOffHand = event.getItemSwappedToOffHand();
			DelayTaskHolder delayTaskHolder = livingEntity.getExistingDataOrNull(LibAttachments.DELAY_TASK_HOLDER);

			if (!itemSwappedToMainHand.getItem().shouldCauseBlockBreakReset(itemSwappedToMainHand, itemSwappedToOffHand)) {
				if (delayTaskHolder != null) {
					delayTaskHolder.removeTask(InteractionHand.MAIN_HAND);
				}
				if (livingEntity instanceof Player player) {
					GunWeaponUtil.setIsAttack(player, true, true);
					GunWeaponUtil.resetChargeUp(player, true);
				}
			}

			if (!itemSwappedToOffHand.getItem().shouldCauseBlockBreakReset(itemSwappedToOffHand, itemSwappedToMainHand)) {
				if (delayTaskHolder != null) {
					delayTaskHolder.removeTask(InteractionHand.OFF_HAND);
				}
				if (livingEntity instanceof Player player) {
					GunWeaponUtil.setIsAttack(player, true, false);
					GunWeaponUtil.resetChargeUp(player, false);
				}
			}
		}
	}

	/**
	 * 即将受到伤害但还没处理
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void livingIncomingDamageEvent(LivingIncomingDamageEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.level() instanceof ServerLevel) {
			DamageSource damageSource = event.getSource();
			DamageContainer damageContainer = event.getContainer();

			LcDamageEventExecutes.levelReduction(entity, damageContainer, damageSource);
			LcDamageEventExecutes.vulnerableReduction(entity, damageContainer, damageSource);
		}
	}

	/**
	 * 处理护甲受伤事件，根据伤害类型和护甲等级计算最终伤害值
	 *
	 * @param event 护甲受伤事件，包含伤害源、装备槽位和伤害信息，以及护盾获得
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void armorHurtEvent(ArmorHurtEvent event) {
		DamageSource damageSource = event.getDamageSource();
		LcDamageType lcDamageType = damageSource.goldenboughs_lib$getLcDamageType();
		Holder<Attribute> vulnerable = lcDamageType == null ? null : lcDamageType.getVulnerable();
		Holder<Attribute> defense = lcDamageType == null ? null : lcDamageType.getDefense();
		LcLevel lcLevel = damageSource.goldenboughs_lib$getLcDamageLevel();
		// 遍历所有装备槽位的护甲物品
		event.getArmorMap().forEach((slot, armorEntry) -> {
			LcLevel armorLevel = LcLevelUtil.getLevel(armorEntry.armorItemStack);
			AtomicBoolean hasResistance = new AtomicBoolean(false);
			// 检查护甲是否具有对该伤害类型的抗性属性
			armorEntry.armorItemStack.getAttributeModifiers().forEach(slot, (attribute, modifier) -> {
				if (lcDamageType != null) {
					if (attribute == vulnerable || attribute == defense) {
						hasResistance.set(true);
					}
				}
			});
			// 根据物品等级来减少物品受到的伤害
			float reducedDamage = armorEntry.originalDamage * LcLevelUtil.getDamageMultiple(lcLevel, armorLevel);
			float resistanceMultiplier = hasResistance.get() ? 1.2f : 1f;
			armorEntry.newDamage = reducedDamage * resistanceMultiplier;
		});
	}

	/**
	 * 处理伤害效果
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void dealingWithDamageEffects(LivingDamageEvent.Pre event) {
		LivingEntity attackedEntity = event.getEntity();
		DamageSource damageSource = event.getSource();
		Entity sourceDirectEntity = damageSource.getDirectEntity();
		Entity sourceCausingEntity = damageSource.getEntity();
		LcDamageType lcDamageType = damageSource.goldenboughs_lib$getLcDamageType();

		//护盾处理
		LcDamageEventExecutes.absorptionShield(event, attackedEntity, lcDamageType);

		if (lcDamageType == LcDamageType.THE_SOUL) {
			// 处理灵魂伤害转换成对应比例的生命值
			event.setNewDamage(LcDamageEventExecutes.theSoulDamage(event.getNewDamage(), attackedEntity, sourceDirectEntity == null ? sourceCausingEntity : sourceDirectEntity, damageSource));
		}

		// 如果低于0则恢复生命值
		if (event.getNewDamage() < 0) {
			// 恢复理智
			if (attackedEntity instanceof Player player &&
				(lcDamageType == LcDamageType.SPIRIT || lcDamageType == LcDamageType.EROSION)
			) {
				RationalityUtil.modifyRationalityValue(player, event.getOriginalDamage(), true);
				ParticleUtil.createDamageTextParticles(player, event.getOriginalDamage(), true, true);
			}

			// 恢复血量
			float healed = Math.abs(event.getOriginalDamage());
			attackedEntity.heal(healed);
			ParticleUtil.createDamageTextParticles(attackedEntity, healed, false, true);

			// 最后修改伤害为0表示不造成伤害
			event.getContainer().setPostAttackInvulnerabilityTicks(0);
			event.setNewDamage(0);
			return;
		}

		if (event.getNewDamage() > 0) {
			// 修改理智
			if (attackedEntity instanceof Player player && (lcDamageType == LcDamageType.SPIRIT || lcDamageType == LcDamageType.EROSION)) {
				RationalityUtil.modifyRationalityValue(player, -event.getNewDamage(), true, lcDamageType == LcDamageType.SPIRIT);
				if (lcDamageType == LcDamageType.SPIRIT) {
					event.getContainer().setPostAttackInvulnerabilityTicks(0);
					event.setNewDamage(0);
					return;
				}
			}

			event.setNewDamage(event.getNewDamage());
		}
	}

	/**
	 * 已应用伤害至实体事件
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void appliedDamageToEntityEvent(LivingDamageEvent.Post event) {
		LivingEntity entity = event.getEntity();
		Level level = entity.level();

		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		DamageSource damageSource = event.getSource();
		float newDamage = event.getNewDamage();
		// 设置理智恢复计时
		if (entity instanceof Player player) {
			RationalityUtil.setRecoveryTick(player, 10 * 20);
		}

		// 低抗缓慢
		@Nullable LcDamageType lcDamageType = damageSource.goldenboughs_lib$getLcDamageType();
		if (lcDamageType != null) {
			AttributeInstance attributeInstance = entity.getAttribute(lcDamageType.getVulnerable());
			if (attributeInstance != null && attributeInstance.getValue() > LcDamageEventExecutes.VULNERABILITY_DECELERATE_THRESHOLD) {
				// TODO 替换成专属效果
				entity.addEffect(new MobEffectInstance(MOVEMENT_SLOWDOWN, 20, 2));
			}
		}

		// 生成粒子
		Holder<DamageType> damageType = damageSource.typeHolder();
		ParticleUtil.createDamageTextParticles(entity, damageType, lcDamageType, newDamage, false, false);

		// TODO 速度快的子弹无法正常显示位置

//    if (source.getDirectEntity() instanceof Projectile projectile) {
//      Vec3 position = source.getSourcePosition();
//      double x;
//      double y;
//      double z;
//      if (position != null) {
//        x = position.x;
//        y = position.y;
//        z = position.z;
//      } else {
//        Vec3 sourcePosition = projectile.position();
//        x = sourcePosition.x;
//        y = sourcePosition.y;
//        z = sourcePosition.z;
//      }
//      MutableComponent text = ParticleUtil.getText(newDamage, false);
//      ParticleUtil.randomDamageTextParticles(serverLevel, text, damageType, lcDamageType, false, false, x, y, z);
//    } else {
//      ParticleUtil.createDamageTextParticles(entity, damageType, lcDamageType, newDamage, false, false);
//    }

		if (entity instanceof ServerPlayer player) {
			PlayerDamagePayload.send(player, lcDamageType, newDamage);
		}
	}

	/**
	 * 效果获得事件
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void effectApplyEvent(MobEffectEvent.Added event) {
		LivingEntity entity = event.getEntity();
		if (entity.level().isClientSide) return;

		MobEffectInstance newEffect = event.getEffectInstance();
		for (var entry : architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry.getAll()) {
			if (newEffect.getEffect().getRegisteredName().equals(entry.effect().getRegisteredName())) {

				if (architecture.goldenboughs_lib.config.LibConfig.SERVER.enableMultiShield.isFalse() && entry.isShieldConflict()) {
					for (var oldEntry : architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry.getAll()) {
						if (!oldEntry.isShieldConflict() ||
							oldEntry.effect().getRegisteredName().equals(entry.effect().getRegisteredName()))
							continue;

						MobEffectInstance existing = entity.getEffect(oldEntry.effect());
						if (existing != null) {
							entity.removeEffect(oldEntry.effect());
						}
					}
				}

				int newAmp = newEffect.getAmplifier();
				float oldAmount = entity.getData(entry.attachment().get());
				float newAmount = entry.initialAmount().apply(newAmp, oldAmount);

				// 如果存在旧效果且等级低于新效果，则不更新
				MobEffectInstance oldEffect = event.getOldEffectInstance();
				if (oldEffect != null && oldEffect.getAmplifier() > newAmp) {
					return;
				}
				entity.setData(entry.attachment().get(), newAmount);
				break;
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		clearAmount(event.getEntity(), event.getEffectInstance());
	}

	// 效果移除/过期：清除吸收值
	private static void clearAmount(LivingEntity entity, MobEffectInstance effect) {
		if (entity.level().isClientSide) return;
		for (var entry : architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry.getAll()) {
			if (effect.getEffect() == entry.effect()) {
				entity.setData(entry.attachment().get(), 0.0f);
				break;
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		clearAmount(event.getEntity(), event.getEffectInstance());
	}
}
