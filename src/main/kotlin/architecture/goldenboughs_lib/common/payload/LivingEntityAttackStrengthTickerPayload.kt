package architecture.goldenboughs_lib.common.payload

import architecture.goldenboughs_lib.api.payload.ToServerAndClientPayload
import architecture.goldenboughs_lib.util.LibUtil
import architecture.goldenboughs_lib.util.setAttackStrengthTicker
import io.netty.buffer.ByteBuf
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext

@JvmRecord
data class LivingEntityAttackStrengthTickerPayload(
	@JvmField val attackStrengthTicker: Int
) : ToServerAndClientPayload {
	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	override fun toServer(context: IPayloadContext, serverPlayer: ServerPlayer) {
		to(serverPlayer)
	}

	override fun toClient(context: IPayloadContext, clientPlayer: AbstractClientPlayer) {
		to(clientPlayer)
	}

	fun to(player: Player) {
		player.setAttackStrengthTicker(this.attackStrengthTicker)
	}

	companion object {
		@JvmField
		val TYPE: CustomPacketPayload.Type<LivingEntityAttackStrengthTickerPayload> =
			CustomPacketPayload.Type(LibUtil.modRl("living_entity_attack_strength_ticker_payload"))

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, LivingEntityAttackStrengthTickerPayload> =
			StreamCodec.composite(
				ByteBufCodecs.INT, LivingEntityAttackStrengthTickerPayload::attackStrengthTicker,
				::LivingEntityAttackStrengthTickerPayload
			)
	}
}
