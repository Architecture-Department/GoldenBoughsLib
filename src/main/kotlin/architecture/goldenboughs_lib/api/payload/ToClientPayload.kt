package architecture.goldenboughs_lib.api.payload

import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToClientPayload : ToServerAndClientPayload {

	fun work(context: IPayloadContext, player: Player)

	override fun toClient(context: IPayloadContext, player: AbstractClientPlayer) {
		work(context, player)
	}

	override fun toServer(context: IPayloadContext, player: ServerPlayer) {
		throw IllegalStateException("This payload is not meant to be sent to the server")
	}
}
