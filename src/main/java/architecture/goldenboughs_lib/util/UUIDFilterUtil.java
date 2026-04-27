package architecture.goldenboughs_lib.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;

import java.util.*;
import java.util.stream.Collectors;

public final class UUIDFilterUtil {
	public static final Codec<UUIDFilterUtil> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		UUIDUtil.CODEC.listOf().fieldOf("blacklist").forGetter((util) -> util.getBlacklist().stream().toList()),
		UUIDUtil.CODEC.listOf().fieldOf("whitelist").forGetter((util) -> util.getWhitelist().stream().toList())
	).apply(instance, UUIDFilterUtil::new));

	public static final StreamCodec<ByteBuf, UUIDFilterUtil> STREAM_CODEC = StreamCodec.composite(
		UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), (util) -> util.getBlacklist().stream().toList(),
		UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), (util) -> util.getWhitelist().stream().toList(),
		UUIDFilterUtil::new);

	private final Set<UUID> blacklist;
	private final Set<UUID> whitelist;

	private UUIDFilterUtil() {
		this.blacklist = new HashSet<>();
		this.whitelist = new HashSet<>();
	}

	private UUIDFilterUtil(Collection<UUID> blacklist, Collection<UUID> whitelist) {
		this.blacklist = new HashSet<>(blacklist);
		this.whitelist = new HashSet<>(blacklist);
	}

	public static UUIDFilterUtil create() {
		return new UUIDFilterUtil();
	}

	public UUIDFilterUtil addBlack(UUID... uuids) {
		blacklist.addAll(Arrays.asList(uuids));
		return this;
	}

	public UUIDFilterUtil addWhite(UUID... uuids) {
		whitelist.addAll(Arrays.asList(uuids));
		return this;
	}

	public UUIDFilterUtil addBlack(EntityAccess... entitys) {
		blacklist.addAll(Arrays.stream(entitys).map(EntityAccess::getUUID).toList());
		return this;
	}

	public UUIDFilterUtil addWhite(EntityAccess... entitys) {
		whitelist.addAll(Arrays.stream(entitys).map(EntityAccess::getUUID).toList());
		return this;
	}

	public UUIDFilterUtil addBlackUUIDs(Collection<UUID> uuids) {
		blacklist.addAll(uuids);
		return this;
	}

	public UUIDFilterUtil addWhiteUUIDs(Collection<UUID> uuids) {
		whitelist.addAll(uuids);
		return this;
	}

	public UUIDFilterUtil addBlackEntitys(Collection<EntityAccess> entitys) {
		blacklist.addAll(entitys.stream().map(EntityAccess::getUUID).toList());
		return this;
	}

	public UUIDFilterUtil addWhiteEntitys(Collection<EntityAccess> entitys) {
		whitelist.addAll(entitys.stream().map(EntityAccess::getUUID).toList());
		return this;
	}

	public UUIDFilterUtil removeUUIDBlacks(UUID... uuids) {
		Arrays.asList(uuids).forEach(blacklist::remove);
		return this;
	}

	public UUIDFilterUtil removeUUIDWhites(UUID... uuids) {
		Arrays.asList(uuids).forEach(whitelist::remove);
		return this;
	}

	public UUIDFilterUtil removeEntityBlacks(EntityAccess... entitys) {
		Arrays.asList(entitys).forEach(entity -> blacklist.remove(entity.getUUID()));
		return this;
	}

	public UUIDFilterUtil removeEntityWhites(EntityAccess... entitys) {
		Arrays.asList(entitys).forEach(entity -> whitelist.remove(entity.getUUID()));
		return this;
	}

	public UUIDFilterUtil removeUUIDBlacks(Collection<UUID> uuids) {
		uuids.forEach(blacklist::remove);
		return this;
	}

	public UUIDFilterUtil removeUUIDWhites(Collection<UUID> uuids) {
		uuids.forEach(whitelist::remove);
		return this;
	}

	public UUIDFilterUtil removeEntityBlacks(Collection<EntityAccess> entitys) {
		entitys.forEach(entity -> blacklist.remove(entity.getUUID()));
		return this;
	}

	public UUIDFilterUtil removeEntityWhites(Collection<EntityAccess> entitys) {
		entitys.forEach(entity -> whitelist.remove(entity.getUUID()));
		return this;
	}

	public UUIDFilterUtil clearBlacklist() {
		blacklist.clear();
		return this;
	}

	public UUIDFilterUtil clearWhitelist() {
		whitelist.clear();
		return this;
	}

	public Set<UUID> getBlacklist() {
		return Collections.unmodifiableSet(blacklist);
	}

	public Set<UUID> getWhitelist() {
		return Collections.unmodifiableSet(whitelist);
	}

	public UUIDFilterUtil copy() {
		UUIDFilterUtil copy = new UUIDFilterUtil();
		copy.blacklist.addAll(blacklist);
		copy.whitelist.addAll(whitelist);
		return copy;
	}

	public <T extends EntityAccess> Set<T> filter(Iterable<T> entitys) {
		Set<T> entityHashSet = new HashSet<>();
		for (T entity : entitys) {
			if (filter(entity)) {
				entityHashSet.add(entity);
			}
		}
		return entityHashSet;
	}

	public <T extends EntityAccess> boolean filter(T entity) {
		return filter(entity.getUUID());
	}

	/**
	 * 过滤
	 * <p>
	 * 白名单优先级大于黑名单
	 *
	 * @param uuid 要判断的uuid
	 * @return 是否通过
	 */
	public boolean filter(UUID uuid) {
		if (whitelist.isEmpty() && blacklist.isEmpty()) {
			return true;
		}
		return whitelist.contains(uuid) || !blacklist.contains(uuid);
	}

	public <T extends EntityAccess> Set<T> filter(Collection<T> entitys) {
		return entitys.stream().filter(this::filter).collect(Collectors.toSet());
	}

	public Set<Player> filterPlayers(Level level) {
		return level.players().stream().filter(this::filter).collect(Collectors.toSet());
	}
}
