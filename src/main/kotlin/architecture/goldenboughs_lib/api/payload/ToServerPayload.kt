package architecture.goldenboughs_lib.api.payload

import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToServerPayload : ToServerAndClientPayload {

	fun work(context: IPayloadContext, player: ServerPlayer)

	override fun toServer(context: IPayloadContext, player: ServerPlayer) {
		work(context, player)
	}

	override fun toClient(context: IPayloadContext, player: AbstractClientPlayer) {
		throw IllegalStateException("This payload cannot be sent to the client")
	}
}
