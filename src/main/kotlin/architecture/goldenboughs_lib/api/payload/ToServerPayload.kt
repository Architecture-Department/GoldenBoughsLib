package architecture.goldenboughs_lib.api.payload

import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToServerPayload : ToServerAndClientPayload {
	override fun work(context: IPayloadContext) {
		val player = context.player()
		if (player !is ServerPlayer) return
		work(player)
	}

	fun work(player: ServerPlayer)

	override fun toServer(serverPlayer: ServerPlayer) {
		work(serverPlayer)
	}

	override fun toClient(clientPlayer: AbstractClientPlayer) {
	}
}
