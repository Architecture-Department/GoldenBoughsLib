package architecture.goldenboughs_lib.api.payload

import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.function.Function

interface ToServerAndClientPayload : CustomPacketPayload {
	fun handle(context: IPayloadContext) {
		context.enqueueWork { work(context) }
			.exceptionally(Function { e: Throwable -> null })
	}

	fun work(context: IPayloadContext) {
		val player = context.player()
		if (player is AbstractClientPlayer) {
			toClient(player)
		} else if (player is ServerPlayer) {
			toServer(player)
		}
	}

	fun toServer(serverPlayer: ServerPlayer)

	fun toClient(clientPlayer: AbstractClientPlayer)
}
