package architecture.goldenboughs_lib.common.payload.toc;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter.LcDamageScreenFilterLayer;
import architecture.goldenboughs_lib.common.payload.api.ToClientPayload;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.util.PayloadUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record PlayerDamagePayload(
	Optional<@Nullable LcDamageType> lcDamageType,
	float damage
) implements ToClientPayload {
	public static final Type<PlayerDamagePayload> TYPE = new Type<>(GoldenBoughsLib.modRl("player_damage_payload"));

	public static final StreamCodec<ByteBuf, PlayerDamagePayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.optional(LcDamageType.STREAM_CODEC), PlayerDamagePayload::lcDamageType,
		ByteBufCodecs.FLOAT, PlayerDamagePayload::damage,
		PlayerDamagePayload::new
	);

	public PlayerDamagePayload(@Nullable LcDamageType lcDamageType, float damage) {
		this(Optional.ofNullable(lcDamageType), damage);
	}

	public static void send(ServerPlayer player, LcDamageType lcDamageType, float newDamage) {
		PayloadUtil.sendToPlayerClient(player, new PlayerDamagePayload(lcDamageType, newDamage));
	}

	@Override
	public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@Override
	public void work(Player player) {
		LcDamageScreenFilterLayer.INSTANCE.addFilter(lcDamageType().orElse(null));
	}
}

