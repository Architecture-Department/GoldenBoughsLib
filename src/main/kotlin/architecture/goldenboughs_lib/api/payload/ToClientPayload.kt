package architecture.goldenboughs_lib.api.payload

import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToClientPayload : ToServerAndClientPayload {
	override fun work(context: IPayloadContext) {
		if (context.player().isLocalPlayer) {
			work(context.player())
		}
	}

	fun work(player: Player)

	override fun toClient(clientPlayer: AbstractClientPlayer) {
		work(clientPlayer)
	}

	override fun toServer(serverPlayer: ServerPlayer) {
	}
}
