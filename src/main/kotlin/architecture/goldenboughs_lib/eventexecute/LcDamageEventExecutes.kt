package architecture.goldenboughs_lib.eventexecute

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.api.LcLevel
import architecture.goldenboughs_lib.api.LcLevel.Companion.byLevel
import architecture.goldenboughs_lib.config.LibConfig
import architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry
import architecture.goldenboughs_lib.util.LcLevelUtil.getDamageMultiple
import architecture.goldenboughs_lib.util.LcLevelUtil.getLevel
import net.minecraft.core.Holder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.common.damagesource.DamageContainer
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import kotlin.math.min

// TODO 精神伤害之类的不能被原版防御处理
object LcDamageEventExecutes {
	const val VULNERABILITY_DECELERATE_THRESHOLD: Double = 1.2

	/**
	 * 根据目标护甲等级与攻击者等级计算伤害减免倍率
	 */
	fun levelReduction(entity: LivingEntity, damageContainer: DamageContainer, damageSource: DamageSource) {
		val attackerLevel = damageSource.`goldenboughs_lib$getLcDamageLevel`() ?: return

		var armorLcLaval = 0 // 盔甲等级总和
		var armorNumber = 0 // 护甲数量
		var voidLcLevelArmorNumber = 0 // 无等级护甲数量

		// 遍历所有护甲槽位，统计护甲等级和数量
		for (armorItemStack in entity.armorAndBodyArmorSlots) {
			if (armorItemStack == null || armorItemStack.isEmpty) {
				continue
			}
			// 获取盔甲等级
			val level = armorItemStack.getLevel()
			if (level == null) {
				voidLcLevelArmorNumber++
			} else {
				armorLcLaval += level.levelValue
			}
			armorNumber++
		}

		if (armorNumber == 0) {
			damageContainer.newDamage = ontologyLevelCalculate(
				entity,
				attackerLevel,
				damageContainer.originalDamage
			)
			return
		}

		// 所有护甲都无等级时设置特殊标记值
		if (voidLcLevelArmorNumber == armorNumber) {
			armorLcLaval = -1
		}

		// 计算平均护甲等级
		if (armorLcLaval != -1) {
			armorLcLaval /= armorNumber
		}

		damageContainer.newDamage = damageContainer.originalDamage * getDamageMultiple(
			byLevel(armorLcLaval),
			attackerLevel
		)
	}

	/**
	 * 本体计算
	 */
	private fun ontologyLevelCalculate(entity: LivingEntity, attackerLevel: LcLevel?, damage: Float): Float {
		return damage * getDamageMultiple(entity.getLevel(), attackerLevel)
	}

	/**
	 * 计算易伤属性对最终伤害的加成效果
	 */
	fun vulnerableReduction(entity: LivingEntity, damageContainer: DamageContainer, damageSource: DamageSource) {
		val lcDamageType = damageSource.`goldenboughs_lib$getLcDamageType`()
		var newDamage = damageContainer.newDamage

		// 检查是否存在自定义伤害类型
		if (lcDamageType != null) {
			// 获取实体的易伤属性值并应用到伤害计算
			val vulnerable: Holder<Attribute> = lcDamageType.vulnerable
			val attributeInstance = entity.getAttribute(vulnerable)
			newDamage *= attributeInstance?.getValue()?.toFloat() ?: vulnerable.value().defaultValue.toFloat()
		}

		damageContainer.newDamage = newDamage
	}

	/**
	 * 灵魂伤害计算
	 */
	fun theSoulDamage(
		damage: Float,
		attackedEntity: LivingEntity?,
		sourceEntity: Entity?,
		damageSource: DamageSource?
	): Float {
		if (attackedEntity is Player) {
			return damage * attackedEntity.maxHealth
		}
		return damage
	}

	/**
	 * 护盾处理
	 */
	fun absorptionShield(event: LivingDamageEvent.Pre, attackedEntity: LivingEntity, lcDamageType: LcDamageType?) {
		if (attackedEntity.level().isClientSide) {
			return
		}

		for (entry in LibAbsorptionShieldsRegistry.shields) {
			val effect = attackedEntity.getEffect(entry.effect) ?: continue

			if (lcDamageType != null && lcDamageType.damageType.location() != entry.damageTypeTag) {
				continue
			}

			val current = attackedEntity.getData(entry.attachment.get()) //护盾量
			if (current <= 0) continue

			val original = event.newDamage //伤害量
			if (original <= 0) continue
			val absorbed = min(current, original)
			val remaining = original - absorbed //剩余伤害

			val newAmount = current - absorbed //新护盾量
			attackedEntity.setData(entry.attachment.get(), newAmount) //保存护盾量

			if (newAmount <= 0) {
				attackedEntity.removeEffect(entry.effect)
				if (attackedEntity is Player) {
					entry.playShieldBreakSound(attackedEntity) // 只对该玩家播放
				}
				if (LibConfig.SERVER.enableShieldDamageImmunity.isTrue) {
					event.newDamage = 0f
					continue  //碎盾抗一下(只抗对应伤害)
				}
			}

			if (remaining <= 0) {
				event.newDamage = 0f
			} else {
				event.newDamage = remaining
			}
		}
	}
}
