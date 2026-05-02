package architecture.goldenboughs_lib.core.registry;

import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibAttributes;
import architecture.goldenboughs_lib.init.LibEntityTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = GoldenBoughsLib.ID)
public final class EntityAttributeRegistry {
	/**
	 * 注册实体属性
	 */
	@SubscribeEvent
	public static void entityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(LibEntityTypes.STAFF_CORPSE.get(), Mob.createMobAttributes().build());
	}

	// TODO 重写编写
	/**
	 * 分级到血量倍数的映射表
	 */
	private static final Map<LcLevel, Double> LEVEL_MAP = Map.of(
		LcLevel.ZAYIN, 0.01,
		LcLevel.TETH, 1.25,
		LcLevel.HE, 1.5,
		LcLevel.WAW, 1.75,
		LcLevel.ALEPH, 2.0
	);

	/**
	 * 实体类型到等级的映射表（直接从CapabilityRegistry复制，避免事件时序问题）
	 * 用于maxHealthMultiples(1.0)时直接读取等级，无需调用LcLevelUtil.getLevel()
	 */
	private static final Map<EntityType<?>, LcLevel> ENTITY_LEVEL = new HashMap<>();

	static {
		ENTITY_LEVEL.put(EntityType.WITHER, LcLevel.ALEPH);
		ENTITY_LEVEL.put(EntityType.ENDER_DRAGON, LcLevel.ALEPH);
		ENTITY_LEVEL.put(EntityType.WARDEN, LcLevel.ALEPH);
		ENTITY_LEVEL.put(EntityType.RAVAGER, LcLevel.WAW);
		ENTITY_LEVEL.put(EntityType.ELDER_GUARDIAN, LcLevel.WAW);
		ENTITY_LEVEL.put(EntityType.IRON_GOLEM, LcLevel.WAW);
		ENTITY_LEVEL.put(EntityType.WITHER_SKELETON, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.WITCH, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.VINDICATOR, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.EVOKER, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.ZOGLIN, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.SHULKER, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.PIGLIN_BRUTE, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.HOGLIN, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.GHAST, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.ENDERMAN, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.GUARDIAN, LcLevel.HE);
		ENTITY_LEVEL.put(EntityType.CAVE_SPIDER, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.SPIDER, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.PIGLIN, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.PILLAGER, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.VEX, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.SILVERFISH, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.ENDERMITE, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.PHANTOM, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.MAGMA_CUBE, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.HUSK, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.CREEPER, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.BREEZE, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.DROWNED, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.ZOMBIFIED_PIGLIN, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.ZOMBIE, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.STRAY, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.SKELETON, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.BOGGED, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.BLAZE, LcLevel.TETH);
		ENTITY_LEVEL.put(EntityType.SLIME, LcLevel.TETH);
	}

	/**
	 * 添加或修改属性 等级在{@link CapabilityRegistry}类注册
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registry(EntityAttributeModificationEvent event) {
		// 对所有实体打入基础属性
		event.getTypes().forEach(entityType -> lcAttributesVulnerable(event, entityType));
		vanilla(event);
		addPlayerAttributes(event, EntityType.PLAYER);
	}

	private static void vanilla(EntityAttributeModificationEvent event) {
		// 抗性（前 4 个）+ 血量（最后 1 个，1表示默认）
		Builder.of().entityType(EntityType.WARDEN)
			.physicsVulnerable(0.6)
			.spiritVulnerable(1.2)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(0.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.ENDER_DRAGON)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.5)
			.erosionVulnerable(0.5)
			.theSoulVulnerable(0.5)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.WITHER)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.7)
			.erosionVulnerable(-1.0)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.IRON_GOLEM)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.6)
			.erosionVulnerable(1.5)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.ELDER_GUARDIAN)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.8)
			.erosionVulnerable(0.9)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.RAVAGER)
			.physicsVulnerable(0.5)
			.spiritVulnerable(1.0)
			.erosionVulnerable(1.5)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.GUARDIAN)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.8)
			.erosionVulnerable(0.9)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.ENDERMAN)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.5)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.5)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.GHAST)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.5)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.5)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.HOGLIN)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.1)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.PIGLIN_BRUTE)
			.physicsVulnerable(0.6)
			.spiritVulnerable(1.3)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.SHULKER)
			.physicsVulnerable(0.2)
			.spiritVulnerable(1.5)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.ZOGLIN)
			.physicsVulnerable(0.5)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.EVOKER)
			.physicsVulnerable(1.0)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.VINDICATOR)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.WITCH)
			.physicsVulnerable(1.2)
			.spiritVulnerable(1.1)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.WITHER_SKELETON)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.8)
			.erosionVulnerable(-1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.BLAZE)
			.physicsVulnerable(0.7)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.BOGGED)
			.physicsVulnerable(1.0)
			.spiritVulnerable(0.5)
			.erosionVulnerable(0.7)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.SKELETON)
			.physicsVulnerable(0.9)
			.spiritVulnerable(0.6)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.STRAY)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.6)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.0)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.ZOMBIE)
			.physicsVulnerable(0.7)
			.spiritVulnerable(0.8)
			.erosionVulnerable(0.9)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.ZOMBIFIED_PIGLIN)
			.physicsVulnerable(0.6)
			.spiritVulnerable(0.7)
			.erosionVulnerable(0.5)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.DROWNED)
			.physicsVulnerable(0.8)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.BREEZE)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.CREEPER)
			.physicsVulnerable(1.2)
			.spiritVulnerable(0.8)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.HUSK)
			.physicsVulnerable(0.6)
			.spiritVulnerable(0.6)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.MAGMA_CUBE)
			.physicsVulnerable(0.4)
			.spiritVulnerable(0.6)
			.erosionVulnerable(1.4)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.PHANTOM)
			.physicsVulnerable(0.6)
			.spiritVulnerable(1.0)
			.erosionVulnerable(0.8)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.ENDERMITE)
			.physicsVulnerable(0.9)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.1)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.SILVERFISH)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.VEX)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.3)
			.erosionVulnerable(1.1)
			.theSoulVulnerable(1.5)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.PILLAGER)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.3)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.PIGLIN)
			.physicsVulnerable(0.8)
			.spiritVulnerable(1.2)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.2)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.SPIDER)
			.physicsVulnerable(0.7)
			.spiritVulnerable(1.1)
			.erosionVulnerable(1.3)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.CAVE_SPIDER)
			.physicsVulnerable(0.7)
			.spiritVulnerable(1.1)
			.erosionVulnerable(1.0)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
		Builder.of().entityType(EntityType.SLIME)
			.physicsVulnerable(0.5)
			.spiritVulnerable(0.7)
			.erosionVulnerable(1.2)
			.theSoulVulnerable(1.1)
			.maxHealthMultiples(1.0)
			.build(event);
	}

	/**
	 * 脑叶属性抗性
	 */
	private static void lcAttributesVulnerable(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType) {
		if (!event.has(entityType, LibAttributes.PHYSICS_VULNERABLE)) {
			event.add(entityType, LibAttributes.PHYSICS_VULNERABLE);
		}
		if (!event.has(entityType, LibAttributes.SPIRIT_VULNERABLE)) {
			event.add(entityType, LibAttributes.SPIRIT_VULNERABLE);
		}
		if (!event.has(entityType, LibAttributes.EROSION_VULNERABLE)) {
			event.add(entityType, LibAttributes.EROSION_VULNERABLE);
		}
		if (!event.has(entityType, LibAttributes.THE_SOUL_VULNERABLE)) {
			event.add(entityType, LibAttributes.THE_SOUL_VULNERABLE);
		}
	}

	/**
	 * 添加玩家属性
	 */
	private static void addPlayerAttributes(EntityAttributeModificationEvent event, EntityType<? extends Player> entityType) {
		event.add(entityType, LibAttributes.MAX_RATIONALITY);
		event.add(entityType, LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME);
		event.add(entityType, LibAttributes.RATIONALITY_RECOVERY_AMOUNT);

		event.add(entityType, LibAttributes.INTELLIGENCE_DEPARTMENT_ACTIVATION);

		event.add(entityType, LibAttributes.PHYSICS_VULNERABLE);
		event.add(entityType, LibAttributes.SPIRIT_VULNERABLE);
		event.add(entityType, LibAttributes.EROSION_VULNERABLE);
		event.add(entityType, LibAttributes.THE_SOUL_VULNERABLE);

		event.add(entityType, LibAttributes.FORTITUDE_POINTS);
		event.add(entityType, LibAttributes.PRUDENCE_POINTS);
		event.add(entityType, LibAttributes.TEMPERANCE_POINTS);
		event.add(entityType, LibAttributes.JUSTICE_POINTS);

		event.add(entityType, LibAttributes.ATTACK_SPEED_MAIN_HAND);
		event.add(entityType, LibAttributes.ATTACK_SPEED_OFF_HAND);
	}

	/**
	 * 获取原始最大生命值(可拓展)
	 */
	private static double getOriginalMaxHealth(EntityType<? extends LivingEntity> entityType) {
		// 1. 获取该实体类型的默认属性供应器（AttributeSupplier）
		AttributeSupplier supplier = DefaultAttributes.getSupplier(entityType);
		if (supplier == null) {
			return 20.0; // 默认回退值（大多数生物的默认值）
		}
		AttributeMap attributeMap = new AttributeMap(supplier);
		AttributeInstance healthAttr = attributeMap.getInstance(Attributes.MAX_HEALTH);
		if (healthAttr == null) {
			return 20.0;
		}
		return healthAttr.getBaseValue();
	}

	public static class Builder {
		private EntityType<? extends LivingEntity> entityType;
		private Map<Holder<Attribute>, Double> attributes;

		public static Builder of() {
			return new Builder();
		}

		/**
		 * 设置实体类型
		 */
		public Builder entityType(EntityType<? extends LivingEntity> entityType) {
			this.entityType = entityType;
			return this;
		}

		/**
		 * 物理易伤
		 */
		public Builder physicsVulnerable(double value) {
			return addAttributes(LibAttributes.PHYSICS_VULNERABLE, value);
		}

		public Builder addAttributes(Holder<Attribute> attributeHolder, double value) {
			if (attributes == null) {
				attributes = new HashMap<>();
			}
			attributes.put(attributeHolder, value);
			return this;
		}

		/**
		 * 精神易伤
		 */
		public Builder spiritVulnerable(double value) {
			return addAttributes(LibAttributes.SPIRIT_VULNERABLE, value);
		}

		/**
		 * 侵蚀易伤
		 */
		public Builder erosionVulnerable(double value) {
			return addAttributes(LibAttributes.EROSION_VULNERABLE, value);
		}

		/**
		 * 灵魂易伤
		 */
		public Builder theSoulVulnerable(double value) {
			return addAttributes(LibAttributes.THE_SOUL_VULNERABLE, value);
		}

		/**
		 * 最大生命值倍数
		 * 当 value == 1.0 时，根据实体分级自动计算血量倍数
		 */
		public Builder maxHealthMultiples(double value) {
			LcLevel level = ENTITY_LEVEL.getOrDefault(entityType, LcLevel.ZAYIN);
			return value == 1.0 ? this.addAttributes(Attributes.MAX_HEALTH, getOriginalMaxHealth(entityType) * LEVEL_MAP.get(level)) : this.addAttributes(Attributes.MAX_HEALTH, getOriginalMaxHealth(entityType) * value);
		}

		/**
		 * 最大生命值
		 */
		public Builder maxHealth(double value) {
			return this.addAttributes(Attributes.MAX_HEALTH, value);
		}

		public void build(EntityAttributeModificationEvent event) {
			// 设置抗性属性
			attributes.forEach((key, value) -> event.add(entityType, key, value));
		}
	}
}
