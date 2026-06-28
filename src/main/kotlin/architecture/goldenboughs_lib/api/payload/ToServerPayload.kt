package architecture.goldenboughs_lib.api.payload

import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToServerPayload : ToPayload {

	fun work(context: IPayloadContext, player: ServerPlayer)

	override fun handle(context: IPayloadContext) {
		context.enqueueWork { work(context) }
			.exceptionally { e -> null }
	}

	override fun work(context: IPayloadContext) {
		val player = context.player()
		if (player is ServerPlayer) {
			work(context, player)
		}
	}
}
