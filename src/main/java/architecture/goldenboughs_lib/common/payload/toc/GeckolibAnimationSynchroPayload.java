package architecture.goldenboughs_lib.common.payload.toc;

import architecture.goldenboughs_lib.common.payload.api.ToClientPayload;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.mixin.geckolib.AnimationControllerAccessorMixin;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.util.GeckoLibUtil;

public record GeckolibAnimationSynchroPayload(
	String syncableId,
	long instanceId,
	String controllerName,
	String animName
) implements ToClientPayload {
	public static final Type<GeckolibAnimationSynchroPayload> TYPE = new Type<>(GoldenBoughsLib.modRl("curio_animation_synchro"));
	public static final StreamCodec<ByteBuf, GeckolibAnimationSynchroPayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, GeckolibAnimationSynchroPayload::syncableId,
		ByteBufCodecs.VAR_LONG, GeckolibAnimationSynchroPayload::instanceId,
		ByteBufCodecs.STRING_UTF8, GeckolibAnimationSynchroPayload::controllerName,
		ByteBufCodecs.STRING_UTF8, GeckolibAnimationSynchroPayload::animName,
		GeckolibAnimationSynchroPayload::new);

	public GeckolibAnimationSynchroPayload(GeoAnimatable animatable, long instanceId, String controllerName, String animName) {
		this(GeckoLibUtil.getSyncedSingletonAnimatableId(animatable), instanceId, controllerName, animName);
	}

	@Override
	public void work(Player player) {
		GeoAnimatable animatable = GeckoLibUtil.getSyncedAnimatable(this.syncableId);

		if (animatable != null) {
			var animatableInstanceCache = animatable.getAnimatableInstanceCache();
			var managerForId = animatableInstanceCache.getManagerForId(this.instanceId);
			var animationControllers = managerForId.getAnimationControllers();
			var controller = animationControllers.get(this.controllerName);
			controller.setAnimation(((AnimationControllerAccessorMixin) controller).goldenboughs_lib$getTriggerableAnimations().get(this.animName));
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
