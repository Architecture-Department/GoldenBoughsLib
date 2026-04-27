package architecture.goldenboughs_lib.common.payload.api;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ToServerAndClientPayload extends CustomPacketPayload {
	default void handle(IPayloadContext context) {
		context.enqueueWork(() -> work(context)).exceptionally(e -> null);
	}

	default void work(IPayloadContext context) {
		Player player = context.player();
		if (player instanceof AbstractClientPlayer abstractClientPlayer) {
			toClient(abstractClientPlayer);
		} else if (player instanceof ServerPlayer serverPlayer) {
			toServer(serverPlayer);
		}
	}

	void toServer(ServerPlayer serverPlayer);

	void toClient(AbstractClientPlayer clientPlayer);
}
