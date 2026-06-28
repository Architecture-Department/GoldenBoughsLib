package architecture.goldenboughs_lib.api.payload

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ToPayload : CustomPacketPayload {
	fun handle(context: IPayloadContext) {
		context.enqueueWork { work(context) }.exceptionally { e -> null }
	}

	fun work(context: IPayloadContext) {}
}