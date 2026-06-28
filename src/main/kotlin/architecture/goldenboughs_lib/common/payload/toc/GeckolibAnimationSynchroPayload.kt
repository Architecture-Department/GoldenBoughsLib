package architecture.goldenboughs_lib.common.payload.toc

import architecture.goldenboughs_lib.api.payload.ToClientPayload
import architecture.goldenboughs_lib.util.LibUtil
import architecture.goldenboughs_lib.util.getTriggerableAnimations
import io.netty.buffer.ByteBuf
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.util.GeckoLibUtil

@JvmRecord
data class GeckolibAnimationSynchroPayload(
	@JvmField val syncableId: String,
	@JvmField val instanceId: Long,
	@JvmField val controllerName: String,
	@JvmField val animName: String
) : ToClientPayload {
	constructor(animatable: GeoAnimatable, instanceId: Long, controllerName: String, animName: String) : this(
		GeckoLibUtil.getSyncedSingletonAnimatableId(animatable),
		instanceId,
		controllerName,
		animName
	)

	override fun work(context: IPayloadContext, player: AbstractClientPlayer) {
		val animatable = GeckoLibUtil.getSyncedAnimatable(this.syncableId)

		if (animatable != null) {
			val animatableInstanceCache = animatable.animatableInstanceCache
			val managerForId: AnimatableManager<GeoAnimatable> = animatableInstanceCache.getManagerForId(this.instanceId)
			val animationControllers = managerForId.getAnimationControllers()
			val controller: AnimationController<GeoAnimatable>? = animationControllers[this.controllerName]
			controller?.setAnimation(
				controller.getTriggerableAnimations()[this.animName]
			)
		}
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	companion object {
		@JvmField
		val TYPE: CustomPacketPayload.Type<GeckolibAnimationSynchroPayload> =
			CustomPacketPayload.Type<GeckolibAnimationSynchroPayload>(LibUtil.modRl("curio_animation_synchro"))

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, GeckolibAnimationSynchroPayload> =
			StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, GeckolibAnimationSynchroPayload::syncableId,
				ByteBufCodecs.VAR_LONG, GeckolibAnimationSynchroPayload::instanceId,
				ByteBufCodecs.STRING_UTF8, GeckolibAnimationSynchroPayload::controllerName,
				ByteBufCodecs.STRING_UTF8, GeckolibAnimationSynchroPayload::animName,
				::GeckolibAnimationSynchroPayload
			)
	}
}
