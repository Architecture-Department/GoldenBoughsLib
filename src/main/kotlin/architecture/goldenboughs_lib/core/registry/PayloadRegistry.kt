package architecture.goldenboughs_lib.core.registry

import architecture.goldenboughs_lib.api.payload.ToClientPayload
import architecture.goldenboughs_lib.api.payload.ToServerAndClientPayload
import architecture.goldenboughs_lib.api.payload.ToServerPayload
import architecture.goldenboughs_lib.common.payload.LivingEntityAttackStrengthTickerPayload
import architecture.goldenboughs_lib.common.payload.toc.GeckolibAnimationSynchroPayload
import architecture.goldenboughs_lib.common.payload.toc.PlayerDamagePayload
import architecture.goldenboughs_lib.common.payload.tos.PlayerIGunWeaponPayload
import architecture.goldenboughs_lib.core.GoldenBoughsLib
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler
import net.neoforged.neoforge.network.registration.PayloadRegistrar

@EventBusSubscriber(modid = GoldenBoughsLib.ID)
object PayloadRegistry {
	@SubscribeEvent
	fun register(event: RegisterPayloadHandlersEvent) {
		val registrar = event.registrar("1.0")
		// 接收来自服务端和客户端的数据 发送到 客户端和服务端
		playToServerAndClient(
			registrar,
			LivingEntityAttackStrengthTickerPayload.TYPE,
			LivingEntityAttackStrengthTickerPayload.STREAM_CODEC
		)

		// 接收来自服务端的数据 发送到 客户端
		playToClient(
			registrar,
			PlayerDamagePayload.TYPE,
			PlayerDamagePayload.STREAM_CODEC
		)
		playToClient(
			registrar,
			GeckolibAnimationSynchroPayload.TYPE,
			GeckolibAnimationSynchroPayload.STREAM_CODEC
		)

		// 接收来自客户端的数据 发送到 服务端
		playToServer(
			registrar,
			PlayerIGunWeaponPayload.TYPE,
			PlayerIGunWeaponPayload.STREAM_CODEC
		)
		GoldenBoughsLib.LOGGER.info("Registering payloads finish")
	}

	private fun <T : ToServerAndClientPayload> playToServerAndClient(
		registrar: PayloadRegistrar,
		type: CustomPacketPayload.Type<T>,
		reader: StreamCodec<in RegistryFriendlyByteBuf, T>
	): PayloadRegistrar {
		return registrar.playBidirectional(
			type, reader,
			DirectionalPayloadHandler(
				ToServerAndClientPayload::handle,
				ToServerAndClientPayload::handle
			)
		)
	}

	private fun <T : ToServerPayload> playToServer(
		registrar: PayloadRegistrar,
		type: CustomPacketPayload.Type<T>,
		reader: StreamCodec<in RegistryFriendlyByteBuf, T>
	): PayloadRegistrar {
		return registrar.playToServer<T>(
			type,
			reader,
			ToServerPayload::handle
		)
	}

	private fun <T : ToClientPayload> playToClient(
		registrar: PayloadRegistrar,
		type: CustomPacketPayload.Type<T>,
		reader: StreamCodec<in RegistryFriendlyByteBuf, T>
	): PayloadRegistrar {
		return registrar.playToClient<T>(
			type,
			reader,
			ToClientPayload::handle
		)
	}
}
