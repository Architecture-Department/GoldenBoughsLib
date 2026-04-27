package architecture.goldenboughs_lib.common.payload.api;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ToClientPayload extends ToServerAndClientPayload {
	@Override
	default void work(IPayloadContext context) {
		if (context.player().isLocalPlayer()) {
			work(context.player());
		}
	}

	void work(Player player);

	@Override
	default void toClient(AbstractClientPlayer player) {
		work(player);
	}

	@Override
	default void toServer(ServerPlayer serverPlayer) {
	}
}
