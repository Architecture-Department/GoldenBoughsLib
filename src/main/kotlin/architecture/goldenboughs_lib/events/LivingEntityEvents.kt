package architecture.goldenboughs_lib.events

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.common.entiy.corpse.StaffCorpse
import architecture.goldenboughs_lib.common.payload.toc.PlayerDamagePayload
import architecture.goldenboughs_lib.config.LibConfig
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.eventexecute.LcDamageEventExecutes
import architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry
import architecture.goldenboughs_lib.init.LibAttachmentTypes
import architecture.goldenboughs_lib.init.LibEntityTypes
import architecture.goldenboughs_lib.util.GunWeaponUtil.resetChargeUp
import architecture.goldenboughs_lib.util.GunWeaponUtil.setIsAttack
import architecture.goldenboughs_lib.util.LcLevelUtil.getDamageMultiple
import architecture.goldenboughs_lib.util.LcLevelUtil.getLevel
import architecture.goldenboughs_lib.util.ParticleUtil.createDamageTextParticles
import architecture.goldenboughs_lib.util.ParticleUtil.getText
import architecture.goldenboughs_lib.util.RationalityUtil.modifyRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.setRecoveryTick
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.*
import net.neoforged.neoforge.event.tick.EntityTickEvent
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

@EventBusSubscriber(modid = Lib.ID)
object LivingEntityEvents {
	/**
	 * 恢复事件
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun entityHealEvent(event: LivingHealEvent) {
		val amount = event.amount
		val entity = event.entity

		if (amount > 0) {
			createDamageTextParticles(entity, getText(amount), false, true)
		}
	}

	@SubscribeEvent
	fun livingDeath(event: LivingDeathEvent) {
		val entity = event.entity
		if (entity !is ServerPlayer) return

		val staffCorpse = StaffCorpse(LibEntityTypes.STAFF_CORPSE.get(), entity.level())
		staffCorpse.ownerUuid = entity.getUUID()
		staffCorpse.ownerName = entity.displayName!!.string
		val onFace: Boolean = entity.getRandom().nextBoolean()
		staffCorpse.isOnFace = onFace
		staffCorpse.setPos(entity.x, entity.y, entity.z)
		if (onFace) {
			staffCorpse.yRot = entity.yRot
		} else {
			staffCorpse.yRot = -entity.yRot
		}
		staffCorpse.setYHeadRot(staffCorpse.yRot)
		staffCorpse.setYBodyRot(staffCorpse.yRot)
		entity.level().addFreshEntity(staffCorpse)
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun livingEquipmentChangeEvent(event: LivingEquipmentChangeEvent) {
		val entity = event.entity
		val slot = event.slot

		if (entity.isAlive) {
			val delayTaskHolder = entity.getExistingDataOrNull(LibAttachmentTypes.DELAY_TASK_HOLDER)
			if (delayTaskHolder != null && !ItemStack.isSameItem(event.from, event.to)) {
				delayTaskHolder.removeTask(slot)
			}

			if (slot.type == EquipmentSlot.Type.HAND) {
				if (entity is Player) {
					val isHandUsed = slot == EquipmentSlot.MAINHAND
					setIsAttack(entity, true, isHandUsed)
					resetChargeUp(entity, isHandUsed)
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun tickPre(event: EntityTickEvent.Pre) {
		val entity = event.entity
		if (entity.isAlive) {
			val timingRun = entity.getExistingDataOrNull(LibAttachmentTypes.DELAY_TASK_HOLDER)
			timingRun?.tick()
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun livingSwapItemsEvent(event: LivingSwapItemsEvent.Hands) {
		val livingEntity = event.entity
		if (livingEntity.isAlive) {
			val itemSwappedToMainHand = event.itemSwappedToMainHand
			val itemSwappedToOffHand = event.itemSwappedToOffHand
			val delayTaskHolder = livingEntity.getExistingDataOrNull(LibAttachmentTypes.DELAY_TASK_HOLDER)

			if (!itemSwappedToMainHand.item
					.shouldCauseBlockBreakReset(itemSwappedToMainHand, itemSwappedToOffHand)
			) {
				delayTaskHolder?.removeTask(InteractionHand.MAIN_HAND)
				if (livingEntity is Player) {
					setIsAttack(livingEntity, true, true)
					resetChargeUp(livingEntity, true)
				}
			}

			if (!itemSwappedToOffHand.item
					.shouldCauseBlockBreakReset(itemSwappedToOffHand, itemSwappedToMainHand)
			) {
				delayTaskHolder?.removeTask(InteractionHand.OFF_HAND)
				if (livingEntity is Player) {
					setIsAttack(livingEntity, true, false)
					resetChargeUp(livingEntity, false)
				}
			}
		}
	}

	/**
	 * 即将受到伤害但还没处理
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun livingIncomingDamageEvent(event: LivingIncomingDamageEvent) {
		val entity = event.entity
		if (entity.level() is ServerLevel) {
			val damageSource = event.source
			val damageContainer = event.container

			LcDamageEventExecutes.levelReduction(entity, damageContainer, damageSource)
			LcDamageEventExecutes.vulnerableReduction(entity, damageContainer, damageSource)
		}
	}

	/**
	 * 处理护甲受伤事件，根据伤害类型和护甲等级计算最终伤害值
	 * 
	 * @param event 护甲受伤事件，包含伤害源、装备槽位和伤害信息，以及护盾获得
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun armorHurtEvent(event: ArmorHurtEvent) {
		val damageSource = event.damageSource
		val lcDamageType = damageSource.`goldenboughs_lib$getLcDamageType`()
		val vulnerable: Holder<Attribute>? = lcDamageType?.vulnerable
		val defense: Holder<Attribute>? = lcDamageType?.defense
		val lcLevel = damageSource.`goldenboughs_lib$getLcDamageLevel`()
		// 遍历所有装备槽位的护甲物品
		event.armorMap.forEach { (slot: EquipmentSlot, armorEntry: ArmorHurtEvent.ArmorEntry) ->
			val armorLevel = armorEntry.armorItemStack.getLevel()
			val hasResistance = AtomicBoolean(false)
			// 检查护甲是否具有对该伤害类型的抗性属性
			armorEntry.armorItemStack.getAttributeModifiers()
				.forEach(slot) { attribute: Holder<Attribute>, modifier: AttributeModifier ->
					if (lcDamageType != null) {
						if (attribute === vulnerable || attribute === defense) {
							hasResistance.set(true)
						}
					}
				}
			// 根据物品等级来减少物品受到的伤害
			val reducedDamage = armorEntry.originalDamage * getDamageMultiple(lcLevel, armorLevel)
			val resistanceMultiplier = if (hasResistance.get()) 1.2f else 1f
			armorEntry.newDamage = reducedDamage * resistanceMultiplier
		}
	}

	/**
	 * 处理伤害效果
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun dealingWithDamageEffects(event: LivingDamageEvent.Pre) {
		val attackedEntity = event.entity
		val damageSource = event.source
		val sourceDirectEntity = damageSource.directEntity
		val sourceCausingEntity = damageSource.entity
		val lcDamageType = damageSource.`goldenboughs_lib$getLcDamageType`()

		//护盾处理
		LcDamageEventExecutes.absorptionShield(event, attackedEntity, lcDamageType)

		if (lcDamageType == LcDamageType.THE_SOUL) {
			// 处理灵魂伤害转换成对应比例的生命值
			event.newDamage = LcDamageEventExecutes.theSoulDamage(
				event.newDamage,
				attackedEntity,
				sourceDirectEntity ?: sourceCausingEntity,
				damageSource
			)
		}

		// 如果低于0则恢复生命值
		if (event.newDamage < 0) {
			// 恢复理智
			if (attackedEntity is Player &&
				(lcDamageType == LcDamageType.SPIRIT || lcDamageType == LcDamageType.EROSION)
			) {
				attackedEntity.modifyRationalityValue(event.originalDamage, true)
				createDamageTextParticles(attackedEntity, event.originalDamage, true, true)
			}

			// 恢复血量
			val healed = abs(event.originalDamage)
			attackedEntity.heal(healed)
			createDamageTextParticles(attackedEntity, healed, false, true)

			// 最后修改伤害为0表示不造成伤害
			event.container.postAttackInvulnerabilityTicks = 0
			event.newDamage = 0f
			return
		}

		if (event.newDamage > 0) {
			// 修改理智
			if (attackedEntity is Player && (lcDamageType == LcDamageType.SPIRIT || lcDamageType == LcDamageType.EROSION)) {
				attackedEntity.modifyRationalityValue(-event.newDamage, true, lcDamageType == LcDamageType.SPIRIT)
				if (lcDamageType == LcDamageType.SPIRIT) {
					event.container.postAttackInvulnerabilityTicks = 0
					event.newDamage = 0f
					return
				}
			}

			event.newDamage = event.newDamage
		}
	}

	/**
	 * 已应用伤害至实体事件
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun appliedDamageToEntityEvent(event: LivingDamageEvent.Post) {
		val entity = event.entity
		val level = entity.level()

		if (level !is ServerLevel) {
			return
		}

		val damageSource = event.source
		val newDamage = event.newDamage
		// 设置理智恢复计时
		if (entity is Player) {
			entity.setRecoveryTick(10 * 20)
		}

		// 低抗缓慢
		val lcDamageType = damageSource.`goldenboughs_lib$getLcDamageType`()
		if (lcDamageType != null) {
			val attributeInstance = entity.getAttribute(lcDamageType.vulnerable)
			if (attributeInstance != null && attributeInstance.getValue() > LcDamageEventExecutes.VULNERABILITY_DECELERATE_THRESHOLD) {
				// TODO 替换成专属效果
				entity.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2))
			}
		}

		// 生成粒子
		val damageType = damageSource.typeHolder()
		createDamageTextParticles(entity, damageType, lcDamageType, newDamage, false, false)

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
		if (entity is ServerPlayer) {
			PlayerDamagePayload.send(entity, lcDamageType, newDamage)
		}
	}

	/**
	 * 效果获得事件
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun effectApplyEvent(event: MobEffectEvent.Added) {
		val entity = event.entity
		if (entity.level().isClientSide) return

		val newEffect: MobEffectInstance = event.getEffectInstance()!!
		for (entry in LibAbsorptionShieldsRegistry.shields) {
			if (newEffect.effect.registeredName == entry.effect.registeredName) {
				if (LibConfig.SERVER.enableMultiShield.isFalse && entry.isShieldConflict) {
					for (oldEntry in LibAbsorptionShieldsRegistry.shields) {
						if (!oldEntry.isShieldConflict ||
							oldEntry.effect.registeredName == entry.effect.registeredName
						) continue

						val existing = entity.getEffect(oldEntry.effect)
						if (existing != null) {
							entity.removeEffect(oldEntry.effect)
						}
					}
				}

				val newAmp = newEffect.amplifier
				val oldAmount = entity.getData(entry.attachment.get())
				val newAmount = entry.initialAmount.apply(newAmp, oldAmount)

				// 如果存在旧效果且等级低于新效果，则不更新
				val oldEffect = event.oldEffectInstance
				if (oldEffect != null && oldEffect.amplifier > newAmp) {
					return
				}
				entity.setData(entry.attachment.get(), newAmount)
				break
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onEffectRemoved(event: MobEffectEvent.Remove) {
		clearAmount(event.entity, event.getEffectInstance()!!)
	}

	// 效果移除/过期：清除吸收值
	private fun clearAmount(entity: LivingEntity, effect: MobEffectInstance) {
		if (entity.level().isClientSide) return
		for (entry in LibAbsorptionShieldsRegistry.shields) {
			if (effect.effect === entry.effect) {
				entity.setData(entry.attachment.get(), 0.0f)
				break
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onEffectExpired(event: MobEffectEvent.Expired) {
		clearAmount(event.entity, event.getEffectInstance()!!)
	}
}
