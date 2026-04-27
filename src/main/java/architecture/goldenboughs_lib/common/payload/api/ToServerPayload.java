package architecture.goldenboughs_lib.common.payload.api;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ToServerPayload extends ToServerAndClientPayload {
	@Override
	default void work(IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player) {
			work(player);
		}
	}

	void work(ServerPlayer player);

	@Override
	default void toServer(ServerPlayer serverPlayer) {
		work(serverPlayer);
	}

	@Override
	default void toClient(AbstractClientPlayer player) {
	}
}
