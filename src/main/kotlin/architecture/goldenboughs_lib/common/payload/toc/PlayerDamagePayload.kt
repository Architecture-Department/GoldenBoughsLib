package architecture.goldenboughs_lib.common.payload.toc

import architecture.goldenboughs_lib.api.LcDamageType
import architecture.goldenboughs_lib.api.payload.ToClientPayload
import architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter.LcDamageScreenFilterLayer
import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.util.PayloadUtil.sendToPlayerClient
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.*

@JvmRecord
data class PlayerDamagePayload(
	@JvmField val lcDamageType: Optional<LcDamageType>,
	@JvmField val damage: Float
) : ToClientPayload {
	constructor(lcDamageType: LcDamageType, damage: Float) : this(
		Optional.ofNullable(lcDamageType),
		damage
	)

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	override fun work(context: IPayloadContext, player: Player) {
		LcDamageScreenFilterLayer.INSTANCE.addFilter(this.lcDamageType.orElse(null))
	}

	companion object {
		@JvmField
		val TYPE: CustomPacketPayload.Type<PlayerDamagePayload> =
			CustomPacketPayload.Type(Lib.modRl("player_damage_payload"))

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, PlayerDamagePayload> =
			StreamCodec.composite(
				ByteBufCodecs.optional(LcDamageType.STREAM_CODEC),
				PlayerDamagePayload::lcDamageType,
				ByteBufCodecs.FLOAT,
				PlayerDamagePayload::damage,
				::PlayerDamagePayload
			)

		@JvmStatic
		fun send(player: ServerPlayer, lcDamageType: LcDamageType, newDamage: Float) {
			sendToPlayerClient(player, PlayerDamagePayload(lcDamageType, newDamage))
		}
	}
}

