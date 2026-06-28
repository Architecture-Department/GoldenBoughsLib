package architecture.goldenboughs_lib.api.payload

import net.minecraft.client.player.AbstractClientPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToClientPayload : ToPayload {

	fun work(context: IPayloadContext, player: AbstractClientPlayer)
	override fun handle(context: IPayloadContext) {
		context.enqueueWork { work(context) }
			.exceptionally { e -> null }
	}

	override fun work(context: IPayloadContext) {
		val player = context.player()
		if (player is AbstractClientPlayer) {
			work(context, player)
		}
	}
}
