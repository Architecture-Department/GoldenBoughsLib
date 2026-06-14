package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.api.DelayTaskHolder
import architecture.goldenboughs_lib.module.virtue.api.VirtueFortitude
import architecture.goldenboughs_lib.module.virtue.api.VirtueJustice
import architecture.goldenboughs_lib.module.virtue.api.VirtuePrudence
import architecture.goldenboughs_lib.module.virtue.api.VirtueTemperance
import architecture.goldenboughs_lib.util.LibUtil.modRegister
import com.mojang.serialization.Codec
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import org.jetbrains.annotations.Contract
import java.util.function.Function
import java.util.function.Supplier

object LibAttachmentTypes {
	@JvmField
	val REGISTRY: DeferredRegister<AttachmentType<*>> =
		modRegister(NeoForgeRegistries.ATTACHMENT_TYPES)

	@JvmField
	val DELAY_TASK_HOLDER: DeferredHolder<AttachmentType<*>, AttachmentType<DelayTaskHolder>> = register(
		"delay_task_holder", AttachmentType.builder(::DelayTaskHolder)
	)

	/**
	 * 枪械蓄力值（主手）
	 */
	@JvmField
	val GUN_CHARGE_UP_TICK_MAIN_HAND: DeferredHolder<AttachmentType<*>, AttachmentType<Int>> =
		registerPlayer("gun_charge_up_tick_main_hand", { 0 }, {
			it.sync(ByteBufCodecs.INT)
		})

	/**
	 * 枪械蓄力值（副手）
	 */
	@JvmField
	val GUN_CHARGE_UP_TICK_OFF_HAND: DeferredHolder<AttachmentType<*>, AttachmentType<Int>> =
		registerPlayer("gun_charge_up_tick_off_hand", { 0 }, {
			it.sync(ByteBufCodecs.INT)
		})

	/**
	 * 枪械是否可以攻击（主手）
	 */
	@JvmField
	val IS_GUN_ATTACK_MAIN_HAND: DeferredHolder<AttachmentType<*>, AttachmentType<Boolean>> =
		registerPlayer("is_gun_left_key_attack_main_hand", { true }, {
			it.sync(ByteBufCodecs.BOOL)
		})

	/**
	 * 枪械是否可以攻击（副手）
	 */
	@JvmField
	val IS_GUN_ATTACK_OFF_HAND: DeferredHolder<AttachmentType<*>, AttachmentType<Boolean>> =
		registerPlayer("is_gun_left_key_attack_off_hand", { true }, {
			it.sync(ByteBufCodecs.BOOL)
		})

	// TODO 待实装 魔弹使用数量
	/**
	 * 魔弹使用数量
	 */
	@JvmField
	val MAGIC_BULLET_USAGE_QUANTITY: DeferredHolder<AttachmentType<*>, AttachmentType<Int>> =
		registerPlayer("magic_bullet_usage_quantity", { 0 }, {
			it.sync(ByteBufCodecs.INT).serialize(Codec.INT).copyOnDeath()
		})

	/**
	 * 理智值
	 */
	@JvmField
	val RATIONALITY: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> = registerPlayer("rationality", { 0f }, {
		it.serialize(Codec.FLOAT).sync(ByteBufCodecs.FLOAT)
	})

	/**
	 * 理智值暂停恢复tick
	 */
	@JvmField
	val RATIONALITY_PAUSE_RECOVERY_TICK: DeferredHolder<AttachmentType<*>, AttachmentType<Int>> =
		registerPlayer("rationality_pause_recovery_tick", { 0 }, {
			it.serialize(Codec.INT).sync(ByteBufCodecs.INT)
		})

	/** 四徳 */
	/**
	 * 勇气
	 */
	@JvmField
	val FORTITUDE: DeferredHolder<AttachmentType<*>, AttachmentType<VirtueFortitude>> =
		registerPlayer("fortitude", { holder: Player -> VirtueFortitude(holder) }, {
			it.serialize(VirtueFortitude.Serialize()).sync(VirtueFortitude.Sync()).copyOnDeath()
		})

	/**
	 * 谨慎
	 */
	@JvmField
	val PRUDENCE: DeferredHolder<AttachmentType<*>, AttachmentType<VirtuePrudence>> =
		registerPlayer("prudence", { holder: Player -> VirtuePrudence(holder) }, {
			it.serialize(VirtuePrudence.Serialize()).sync(VirtuePrudence.Sync()).copyOnDeath()
		})

	/**
	 * 自律
	 */
	@JvmField
	val TEMPERANCE: DeferredHolder<AttachmentType<*>, AttachmentType<VirtueTemperance>> =
		registerPlayer("temperance", { holder: Player -> VirtueTemperance(holder) }, {
			it.serialize(VirtueTemperance.Serialize()).sync(VirtueTemperance.Sync()).copyOnDeath()
		})

	/**
	 * 正义
	 */
	@JvmField
	val JUSTICE: DeferredHolder<AttachmentType<*>, AttachmentType<VirtueJustice>> =
		registerPlayer("justice", { holder: Player -> VirtueJustice(holder) }, {
			it.serialize(VirtueJustice.Serialize()).sync(VirtueJustice.Sync()).copyOnDeath()
		})

	/**
	 * 四色护盾量
	 */
	@JvmField
	val PHYSIC_DAMAGE_ABSORPTION_AMOUNT: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> =
		registerEntity("physic_damage_absorption_amount", { 0f }, {
			it.serialize(Codec.FLOAT).sync(ByteBufCodecs.FLOAT)
		})

	@JvmField
	val SPIRIT_DAMAGE_ABSORPTION_AMOUNT: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> =
		registerEntity("spirit_damage_absorption_amount", { 0f }, {
			it.serialize(Codec.FLOAT).sync(ByteBufCodecs.FLOAT)
		})

	@JvmField
	val EROSION_DAMAGE_ABSORPTION_AMOUNT: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> =
		registerEntity("erosion_damage_absorption_amount", { 0f }, {
			it.serialize(Codec.FLOAT).sync(ByteBufCodecs.FLOAT)
		})

	@JvmField
	val SOUL_DAMAGE_ABSORPTION_AMOUNT: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> =
		registerEntity("soul_damage_absorption_amount", { 0f }, {
			it.serialize(Codec.FLOAT).sync(ByteBufCodecs.FLOAT)
		})


	private fun <T> registerPlayer(
		name: String, defaultValue: Function<Player, T>
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return registerPlayer(name, defaultValue) { builder: AttachmentType.Builder<T> -> builder }
	}

	private fun <T> registerPlayer(
		name: String,
		defaultValue: Function<Player, T>,
		builder: Function<AttachmentType.Builder<T>, AttachmentType.Builder<T>>
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return register<T>(name) {
			builder.apply(AttachmentType.builder<T> { holder: IAttachmentHolder ->
				instanceofPlayer<T>(
					defaultValue, holder, name
				)
			}).build()
		}
	}

	private fun <T> register(
		name: String, builder: Supplier<AttachmentType<T>>
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return REGISTRY.register<AttachmentType<T>>(name, builder)
	}

	private fun <T> registerEntity(
		name: String, defaultValue: Function<Entity, T>
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return registerEntity(name, defaultValue) { builder: AttachmentType.Builder<T> -> builder }
	}

	private fun <T> registerEntity(
		name: String,
		defaultValue: Function<Entity, T>,
		builder: Function<AttachmentType.Builder<T>, AttachmentType.Builder<T>>
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return register<T>(name) {
			builder.apply(AttachmentType.builder<T> { holder: IAttachmentHolder ->
				instanceofEntity<T>(
					defaultValue, holder, name
				)
			}).build()
		}
	}

	@Contract("_, null, _ -> fail")
	private fun <T> instanceofPlayer(
		defaultValue: Function<Player, T>, holder: IAttachmentHolder, name: String
	): T {
		assert(holder is Player) { "$name can only be attached to a player" }
		return defaultValue.apply(holder as Player)
	}

	@Contract("_, null, _ -> fail")
	private fun <T> instanceofEntity(
		defaultValue: Function<Entity, T>, holder: IAttachmentHolder, name: String
	): T {
		assert(holder is Entity) { "$name can only be attached to an entity" }
		return defaultValue.apply(holder as Entity)
	}

	private fun <T> register(
		name: String, builder: AttachmentType.Builder<T>
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return register<T>(name) { builder.build() }
	}
}
