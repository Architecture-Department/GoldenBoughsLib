package architecture.goldenboughs_lib.events.registry

import architecture.goldenboughs_lib.api.LcLevel
import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibAttributes
import architecture.goldenboughs_lib.init.LibEntityTypes
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.attributes.DefaultAttributes
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import java.util.function.Consumer

@EventBusSubscriber(modid = LibConstants.ID)
object EntityAttributeRegistry {
	/**
	 * 分级到血量倍数的映射表
	 */
	@JvmStatic
	private val LEVEL_MAP: Map<LcLevel, Double> = mapOf(
		LcLevel.ZAYIN to 0.01,
		LcLevel.TETH to 1.25,
		LcLevel.HE to 1.5,
		LcLevel.WAW to 1.75,
		LcLevel.ALEPH to 2.0
	)

	// TODO 重写编写
	/**
	 * 实体类型到等级的映射表（直接从CapabilityRegistry复制，避免事件时序问题）
	 * 用于maxHealthMultiples(1.0)时直接读取等级，无需调用LcLevelUtil.getLevel()
	 */
	@JvmStatic
	private val ENTITY_LEVEL: MutableMap<EntityType<*>, LcLevel?> = HashMap()

	init {
		ENTITY_LEVEL[EntityType.WITHER] = LcLevel.ALEPH
		ENTITY_LEVEL[EntityType.ENDER_DRAGON] = LcLevel.ALEPH
		ENTITY_LEVEL[EntityType.WARDEN] = LcLevel.ALEPH
		ENTITY_LEVEL[EntityType.RAVAGER] = LcLevel.WAW
		ENTITY_LEVEL[EntityType.ELDER_GUARDIAN] = LcLevel.WAW
		ENTITY_LEVEL[EntityType.IRON_GOLEM] = LcLevel.WAW
		ENTITY_LEVEL[EntityType.WITHER_SKELETON] = LcLevel.HE
		ENTITY_LEVEL[EntityType.WITCH] = LcLevel.HE
		ENTITY_LEVEL[EntityType.VINDICATOR] = LcLevel.HE
		ENTITY_LEVEL[EntityType.EVOKER] = LcLevel.HE
		ENTITY_LEVEL[EntityType.ZOGLIN] = LcLevel.HE
		ENTITY_LEVEL[EntityType.SHULKER] = LcLevel.HE
		ENTITY_LEVEL[EntityType.PIGLIN_BRUTE] = LcLevel.HE
		ENTITY_LEVEL[EntityType.HOGLIN] = LcLevel.HE
		ENTITY_LEVEL[EntityType.GHAST] = LcLevel.HE
		ENTITY_LEVEL[EntityType.ENDERMAN] = LcLevel.HE
		ENTITY_LEVEL[EntityType.GUARDIAN] = LcLevel.HE
		ENTITY_LEVEL[EntityType.CAVE_SPIDER] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.SPIDER] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.PIGLIN] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.PILLAGER] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.VEX] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.SILVERFISH] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.ENDERMITE] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.PHANTOM] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.MAGMA_CUBE] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.HUSK] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.CREEPER] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.BREEZE] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.DROWNED] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.ZOMBIFIED_PIGLIN] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.ZOMBIE] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.STRAY] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.SKELETON] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.BOGGED] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.BLAZE] = LcLevel.TETH
		ENTITY_LEVEL[EntityType.SLIME] = LcLevel.TETH
	}

	/**
	 * 注册实体属性
	 */
	@SubscribeEvent
	fun entityAttributeCreation(event: EntityAttributeCreationEvent) {
		event.put(LibEntityTypes.STAFF_CORPSE.get(), Mob.createMobAttributes().build())
	}

	/**
	 * 添加或修改属性 等级在[CapabilityRegistry]类注册
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun registry(event: EntityAttributeModificationEvent) {
		// 对所有实体打入基础属性
		event.types.forEach(Consumer { entityType: EntityType<out LivingEntity> ->
			lcAttributesVulnerable(
				event,
				entityType
			)
		})
		vanilla(event)
		addPlayerAttributes(event, EntityType.PLAYER)
	}

	private fun vanilla(event: EntityAttributeModificationEvent) {
		// 抗性（前 4 个）+ 血量（最后 1 个，1表示默认）
		Builder(EntityType.WARDEN)
			.physicsVulnerable(0.6)
			.spiritVulnerable(1.2)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(0.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.ENDER_DRAGON)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.5)
			.erosionVulnerable(0.5)
			.theSoulVulnerable(0.5)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.WITHER)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.7)
			.erosionVulnerable(-1.0)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.IRON_GOLEM)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.6)
			.erosionVulnerable(1.5)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.ELDER_GUARDIAN)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.8)
			.erosionVulnerable(0.9)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.RAVAGER)
			.physicsVulnerable(0.5)
			.spiritVulnerable(1.0)
			.erosionVulnerable(1.5)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.GUARDIAN)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.8)
			.erosionVulnerable(0.9)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.ENDERMAN)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.5)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.5)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.GHAST)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.5)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.5)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.HOGLIN)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.1)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.PIGLIN_BRUTE)
			.physicsVulnerable(0.6)
			.spiritVulnerable(1.3)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.SHULKER)
			.physicsVulnerable(0.2)
			.spiritVulnerable(1.5)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.ZOGLIN)
			.physicsVulnerable(0.5)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.EVOKER)
			.physicsVulnerable(1.0)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.VINDICATOR)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.WITCH)
			.physicsVulnerable(1.2)
			.spiritVulnerable(1.1)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.WITHER_SKELETON)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.8)
			.erosionVulnerable(-1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.BLAZE)
			.physicsVulnerable(0.7)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.BOGGED)
			.physicsVulnerable(1.0)
			.spiritVulnerable(0.5)
			.erosionVulnerable(0.7)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.SKELETON)
			.physicsVulnerable(0.9)
			.spiritVulnerable(0.6)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.STRAY)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.6)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.ZOMBIE)
			.physicsVulnerable(0.7)
			.spiritVulnerable(0.8)
			.erosionVulnerable(0.9)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.ZOMBIFIED_PIGLIN)
			.physicsVulnerable(0.6)
			.spiritVulnerable(0.7)
			.erosionVulnerable(0.5)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.DROWNED)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.BREEZE)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.CREEPER)
			.physicsVulnerable(1.2)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.HUSK)
			.physicsVulnerable(0.6)
			.spiritVulnerable(0.6)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.MAGMA_CUBE)
			.physicsVulnerable(0.4)
			.spiritVulnerable(0.6)
			.erosionVulnerable(1.4)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.PHANTOM)
			.physicsVulnerable(0.6)
			.spiritVulnerable(1.0)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.ENDERMITE)
			.physicsVulnerable(0.9)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.1)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.SILVERFISH)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.VEX)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.3)
			.erosionVulnerable(1.1)
			.theSoulVulnerable(1.5)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.PILLAGER)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.PIGLIN)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.SPIDER)
			.physicsVulnerable(0.7)
			.spiritVulnerable(1.1)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.CAVE_SPIDER)
			.physicsVulnerable(0.7)
			.spiritVulnerable(1.1)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
		Builder(EntityType.SLIME)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.7)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event)
	}

	/**
	 * 脑叶属性抗性
	 */
	private fun lcAttributesVulnerable(
		event: EntityAttributeModificationEvent,
		entityType: EntityType<out LivingEntity>
	) {
		if (!event.has(entityType, LibAttributes.PHYSICS_VULNERABLE)) {
			event.add(entityType, LibAttributes.PHYSICS_VULNERABLE)
		}
		if (!event.has(entityType, LibAttributes.SPIRIT_VULNERABLE)) {
			event.add(entityType, LibAttributes.SPIRIT_VULNERABLE)
		}
		if (!event.has(entityType, LibAttributes.EROSION_VULNERABLE)) {
			event.add(entityType, LibAttributes.EROSION_VULNERABLE)
		}
		if (!event.has(entityType, LibAttributes.THE_SOUL_VULNERABLE)) {
			event.add(entityType, LibAttributes.THE_SOUL_VULNERABLE)
		}
	}

	/**
	 * 添加玩家属性
	 */
	private fun addPlayerAttributes(event: EntityAttributeModificationEvent, entityType: EntityType<out Player>) {
		event.add(entityType, LibAttributes.MAX_RATIONALITY)
		event.add(entityType, LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME)
		event.add(entityType, LibAttributes.RATIONALITY_RECOVERY_AMOUNT)

		event.add(entityType, LibAttributes.INTELLIGENCE_DEPARTMENT_ACTIVATION)

		event.add(entityType, LibAttributes.PHYSICS_VULNERABLE)
		event.add(entityType, LibAttributes.SPIRIT_VULNERABLE)
		event.add(entityType, LibAttributes.EROSION_VULNERABLE)
		event.add(entityType, LibAttributes.THE_SOUL_VULNERABLE)

		event.add(entityType, LibAttributes.FORTITUDE_POINTS)
		event.add(entityType, LibAttributes.PRUDENCE_POINTS)
		event.add(entityType, LibAttributes.TEMPERANCE_POINTS)
		event.add(entityType, LibAttributes.JUSTICE_POINTS)

		event.add(entityType, LibAttributes.ATTACK_SPEED_MAIN_HAND)
		event.add(entityType, LibAttributes.ATTACK_SPEED_OFF_HAND)
	}

	/**
	 * 获取原始最大生命值(可拓展)
	 */
	private fun getOriginalMaxHealth(entityType: EntityType<out LivingEntity>): Double {
		// 1. 获取该实体类型的默认属性供应器（AttributeSupplier）
		val supplier = DefaultAttributes.getSupplier(entityType) ?: return 20.0 // 默认回退值（大多数生物的默认值）
		val attributeMap = AttributeMap(supplier)
		val healthAttr = attributeMap.getInstance(Attributes.MAX_HEALTH) ?: return 20.0
		return healthAttr.baseValue
	}

	class Builder(private var entityType: EntityType<out LivingEntity>) {
		private var attributes: MutableMap<Holder<Attribute>, Double> = HashMap()

		/**
		 * 物理易伤
		 */
		fun physicsVulnerable(value: Double): Builder {
			return addAttributes(LibAttributes.PHYSICS_VULNERABLE, value)
		}

		fun addAttributes(attributeHolder: Holder<Attribute>, value: Double): Builder {
			attributes[attributeHolder] = value
			return this
		}

		/**
		 * 精神易伤
		 */
		fun spiritVulnerable(value: Double): Builder {
			return addAttributes(LibAttributes.SPIRIT_VULNERABLE, value)
		}

		/**
		 * 侵蚀易伤
		 */
		fun erosionVulnerable(value: Double): Builder {
			return addAttributes(LibAttributes.EROSION_VULNERABLE, value)
		}

		/**
		 * 灵魂易伤
		 */
		fun theSoulVulnerable(value: Double): Builder {
			return addAttributes(LibAttributes.THE_SOUL_VULNERABLE, value)
		}

		/**
		 * 最大生命值倍数
		 * 当 value == 1.0 时，根据实体分级自动计算血量倍数
		 */
		fun maxHealthMultiples(value: Double): Builder {
			val level = ENTITY_LEVEL.getOrDefault(entityType, LcLevel.ZAYIN)
			return if (value == 1.0) this.addAttributes(
				Attributes.MAX_HEALTH,
				getOriginalMaxHealth(entityType) * LEVEL_MAP[level]!!
			) else this.addAttributes(
				Attributes.MAX_HEALTH, getOriginalMaxHealth(entityType) * value
			)
		}

		/**
		 * 最大生命值
		 */
		fun maxHealth(value: Double): Builder {
			return this.addAttributes(Attributes.MAX_HEALTH, value)
		}

		fun build(event: EntityAttributeModificationEvent) {
			// 设置抗性属性
			attributes.forEach { (key: Holder<Attribute>, value: Double) -> event.add(entityType, key, value) }
		}
	}
}
