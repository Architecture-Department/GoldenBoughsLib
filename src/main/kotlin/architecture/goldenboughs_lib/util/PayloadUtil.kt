package architecture.goldenboughs_lib.util

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.PacketDistributor

object PayloadUtil {
	/**
	 * 发送玩家数据包（服务端到客户端）
	 */
	@JvmStatic
	fun sendToPlayerClient(
		serverPlayer: ServerPlayer,
		customPacketPayload: CustomPacketPayload,
		vararg customPacketPayloads: CustomPacketPayload?
	) {
		PacketDistributor.sendToPlayer(serverPlayer, customPacketPayload, *customPacketPayloads)
	}

	/**
	 * 发送玩家数据包（客户端到服务端）
	 */
	@JvmStatic
	fun sendToPlayerServer(
		customPacketPayload: CustomPacketPayload,
		vararg customPacketPayloads: CustomPacketPayload?
	) {
		PacketDistributor.sendToServer(customPacketPayload, *customPacketPayloads)
	}

	@JvmStatic
	fun sendToPlayersTrackingEntityAndSelf(
		entity: Entity,
		customPacketPayload: CustomPacketPayload,
		vararg customPacketPayloads: CustomPacketPayload?
	) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, customPacketPayload, *customPacketPayloads)
	}
}
