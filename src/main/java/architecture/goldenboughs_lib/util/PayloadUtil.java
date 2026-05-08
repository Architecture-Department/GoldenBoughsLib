package architecture.goldenboughs_lib.util;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public final class PayloadUtil {
	/**
	 * 发送玩家数据包（服务端到客户端）
	 */
	public static void sendToPlayerClient(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload, CustomPacketPayload... customPacketPayloads) {
		PacketDistributor.sendToPlayer(serverPlayer, customPacketPayload, customPacketPayloads);
	}

	/**
	 * 发送玩家数据包（客户端到服务端）
	 */
	public static void sendToPlayerServer(CustomPacketPayload customPacketPayload, CustomPacketPayload... customPacketPayloads) {
		PacketDistributor.sendToServer(customPacketPayload, customPacketPayloads);
	}

	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload customPacketPayload, CustomPacketPayload... customPacketPayloads) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, customPacketPayload, customPacketPayloads);
	}
}
