package architecture.goldenboughs_lib.common.payload

import architecture.goldenboughs_lib.api.payload.ToServerAndClientPayload
import architecture.goldenboughs_lib.core.GoldenBoughsLib
import architecture.goldenboughs_lib.mixin.world.entity.LivingEntityAccessorMixin
import io.netty.buffer.ByteBuf
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

@JvmRecord
data class LivingEntityAttackStrengthTickerPayload(
	@JvmField val attackStrengthTicker: Int
) : ToServerAndClientPayload {
	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	override fun toServer(serverPlayer: ServerPlayer) {
		to(serverPlayer)
	}

	fun to(player: Player) {
		(player as LivingEntityAccessorMixin).`goldenboughs_lib$setAttackStrengthTicker`(this.attackStrengthTicker)
	}

	override fun toClient(clientPlayer: AbstractClientPlayer) {
		to(clientPlayer)
	}

	companion object {
		@JvmField
		val TYPE: CustomPacketPayload.Type<LivingEntityAttackStrengthTickerPayload> =
			CustomPacketPayload.Type(GoldenBoughsLib.modRl("living_entity_attack_strength_ticker_payload"))

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, LivingEntityAttackStrengthTickerPayload> =
			StreamCodec.composite(
				ByteBufCodecs.INT, LivingEntityAttackStrengthTickerPayload::attackStrengthTicker,
				::LivingEntityAttackStrengthTickerPayload
			)
	}
}
