package architecture.goldenboughs_lib.common.payload;

import architecture.goldenboughs_lib.common.payload.api.ToServerAndClientPayload;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.mixin.world.entity.LivingEntityAccessorMixin;
import com.mojang.datafixers.types.Type;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record LivingEntityAttackStrengthTickerPayload(
	int attackStrengthTicker
) implements ToServerAndClientPayload {
	public static final Type<LivingEntityAttackStrengthTickerPayload> TYPE = new Type<>(GoldenBoughsLib.modRl("living_entity_attack_strength_ticker_payload"));
	public static final StreamCodec<ByteBuf, LivingEntityAttackStrengthTickerPayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, LivingEntityAttackStrengthTickerPayload::attackStrengthTicker,
		LivingEntityAttackStrengthTickerPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@Override
	public void toServer(ServerPlayer serverPlayer) {
		to(serverPlayer);
	}

	public void to(Player player) {
		((LivingEntityAccessorMixin) player).goldenboughs_lib$setAttackStrengthTicker(attackStrengthTicker());
	}

	@Override
	public void toClient(AbstractClientPlayer clientPlayer) {
		to(clientPlayer);
	}
}
