package architecture.goldenboughs_lib.api.payload

import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToServerAndClientPayload : CustomPacketPayload {
	fun handle(context: IPayloadContext) {
		context.enqueueWork { work(context) }
			.exceptionally { e -> null }
	}

	fun work(context: IPayloadContext) {
		val player = context.player()
		if (player is AbstractClientPlayer) {
			toClient(context, player)
		} else if (player is ServerPlayer) {
			toServer(context, player)
		}
	}

	fun toServer(context: IPayloadContext, player: ServerPlayer) {
	}

	fun toClient(context: IPayloadContext, player: AbstractClientPlayer) {
	}
}
