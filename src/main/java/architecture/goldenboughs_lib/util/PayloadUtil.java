package architecture.goldenboughs_lib.util;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class PayloadUtil {
	/**
	 * 发送玩家数据包（服务端到客户端）
	 */
	public static void sendToClient(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload) {
		PacketDistributor.sendToPlayer(serverPlayer, customPacketPayload);
	}

	/**
	 * 发送玩家数据包（客户端到服务端）
	 */
	public static void sendToServer(CustomPacketPayload customPacketPayload) {
		PacketDistributor.sendToServer(customPacketPayload);
	}
}
