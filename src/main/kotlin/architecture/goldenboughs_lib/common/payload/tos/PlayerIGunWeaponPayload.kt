package architecture.goldenboughs_lib.common.payload.tos

import architecture.goldenboughs_lib.api.payload.ToServerPayload
import architecture.goldenboughs_lib.api.world.item.IGunWeapon
import architecture.goldenboughs_lib.core.GoldenBoughsLib
import architecture.goldenboughs_lib.util.PayloadUtil.sendToPlayerServer
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand

// TODO 替换成史诗战斗的控制系统
@JvmRecord
data class PlayerIGunWeaponPayload(
	@JvmField val operation: Int
) : ToServerPayload {
	/**
	 * @param hand  0b1:主手 0b0:副手
	 * @param sim   0b1:瞄准 0b0:不瞄准
	 * @param shoot 0b1:射击 0b0:不射击
	 */
	constructor(hand: InteractionHand, sim: Boolean, shoot: Boolean) : this(
		(setHand(hand) or setSim(sim) or setShoot(
			shoot
		))
	)

	/**
	 * @param isMainHand 0b1:主手 0b0:副手
	 * @param sim        0b1:瞄准 0b0:不瞄准
	 * @param shoot      0b1:射击 0b0:不射击
	 */
	constructor(
		isMainHand: Boolean,
		sim: Boolean,
		shoot: Boolean
	) : this(((if (isMainHand) 1 else 0) or setSim(sim) or setShoot(shoot)))

	val isSim: Boolean
		/**
		 * 瞄准
		 */
		get() = (operation and 2) shr 1 == 1

	/**
	 * 发送到服务端
	 */
	override fun work(player: ServerPlayer) {
		val hand = this.hand
		val itemStack = player.getItemInHand(hand)
		val item = itemStack.item
		if (item !is IGunWeapon) return

		if (item.isGunAim(player, itemStack) && this.isAimShoot) {
			item.gunAimShoot(player, itemStack, hand)
			return
		}

		if (this.isShoot) {
			item.gunShoot(player, itemStack, hand)
		}
	}

	val hand: InteractionHand
		get() = if ((operation and 1) == 1) InteractionHand.MAIN_HAND else InteractionHand.OFF_HAND

	val isShoot: Boolean
		/**
		 * 射击
		 */
		get() = (operation and 4) shr 2 == 1

	val isAimShoot: Boolean
		/**
		 * 瞄准射击
		 */
		get() = (operation and 6) shr 1 == 3

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	companion object {
		@JvmField
		val TYPE: CustomPacketPayload.Type<PlayerIGunWeaponPayload> =
			CustomPacketPayload.Type(GoldenBoughsLib.modRl("player_gun_weapon_payload"))

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, PlayerIGunWeaponPayload> =
			StreamCodec.composite(
				ByteBufCodecs.INT, PlayerIGunWeaponPayload::operation,
				::PlayerIGunWeaponPayload
			)

		private fun setHand(hand: InteractionHand): Int {
			return if (hand == InteractionHand.MAIN_HAND) 1 else 0
		}

		private fun setShoot(shoot: Boolean): Int {
			return (if (shoot) 4 else 0)
		}

		private fun setSim(sim: Boolean): Int {
			return (if (sim) 2 else 0)
		}

		@JvmStatic
		fun send(usedItemHand: InteractionHand, sim: Boolean, shoot: Boolean) {
			sendToPlayerServer(PlayerIGunWeaponPayload(usedItemHand, sim, shoot))
		}
	}
}

