package architecture.goldenboughs_lib.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.UUIDUtil
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.entity.EntityAccess
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors

class UUIDFilterUtil {
	private val blacklist: MutableSet<UUID>
	private val whitelist: MutableSet<UUID>

	private constructor() {
		this.blacklist = HashSet()
		this.whitelist = HashSet()
	}

	private constructor(blacklist: MutableCollection<UUID>, whitelist: MutableCollection<UUID>) {
		this.blacklist = HashSet(blacklist)
		this.whitelist = HashSet(blacklist)
	}

	fun addBlack(vararg uuids: UUID): UUIDFilterUtil {
		blacklist.addAll(uuids)
		return this
	}

	fun addWhite(vararg uuids: UUID): UUIDFilterUtil {
		whitelist.addAll(uuids)
		return this
	}

	fun addBlack(vararg entitys: EntityAccess): UUIDFilterUtil {
		blacklist.addAll(
			Arrays.stream(entitys).map(EntityAccess::getUUID)
				.toList()
		)
		return this
	}

	fun addWhite(vararg entitys: EntityAccess): UUIDFilterUtil {
		whitelist.addAll(
			Arrays.stream(entitys).map(EntityAccess::getUUID)
				.toList()
		)
		return this
	}

	fun addBlackUUIDs(uuids: MutableCollection<UUID>): UUIDFilterUtil {
		blacklist.addAll(uuids)
		return this
	}

	fun addWhiteUUIDs(uuids: MutableCollection<UUID>): UUIDFilterUtil {
		whitelist.addAll(uuids)
		return this
	}

	fun addBlackEntitys(entitys: MutableCollection<EntityAccess>): UUIDFilterUtil {
		blacklist.addAll(entitys.stream().map(EntityAccess::getUUID).toList())
		return this
	}

	fun addWhiteEntitys(entitys: MutableCollection<EntityAccess>): UUIDFilterUtil {
		whitelist.addAll(entitys.stream().map(EntityAccess::getUUID).toList())
		return this
	}

	fun removeUUIDBlacks(vararg uuids: UUID): UUIDFilterUtil {
		listOf(*uuids).forEach(Consumer { o: UUID -> blacklist.remove(o) })
		return this
	}

	fun removeUUIDWhites(vararg uuids: UUID): UUIDFilterUtil {
		listOf(*uuids).forEach(Consumer { o: UUID -> whitelist.remove(o) })
		return this
	}

	fun removeEntityBlacks(vararg entitys: EntityAccess): UUIDFilterUtil {
		listOf(*entitys)
			.forEach(Consumer { entity: EntityAccess -> blacklist.remove(entity.uuid) })
		return this
	}

	fun removeEntityWhites(vararg entitys: EntityAccess): UUIDFilterUtil {
		listOf(*entitys)
			.forEach(Consumer { entity: EntityAccess -> whitelist.remove(entity.uuid) })
		return this
	}

	fun removeUUIDBlacks(uuids: MutableCollection<UUID>): UUIDFilterUtil {
		uuids.forEach(Consumer { o: UUID -> blacklist.remove(o) })
		return this
	}

	fun removeUUIDWhites(uuids: MutableCollection<UUID>): UUIDFilterUtil {
		uuids.forEach(Consumer { o: UUID -> whitelist.remove(o) })
		return this
	}

	fun removeEntityBlacks(entitys: MutableCollection<EntityAccess>): UUIDFilterUtil {
		entitys.forEach(Consumer { entity: EntityAccess -> blacklist.remove(entity.uuid) })
		return this
	}

	fun removeEntityWhites(entitys: MutableCollection<EntityAccess>): UUIDFilterUtil {
		entitys.forEach(Consumer { entity: EntityAccess -> whitelist.remove(entity.uuid) })
		return this
	}

	fun clearBlacklist(): UUIDFilterUtil {
		blacklist.clear()
		return this
	}

	fun clearWhitelist(): UUIDFilterUtil {
		whitelist.clear()
		return this
	}

	fun getBlacklist(): Set<UUID> {
		return blacklist
	}

	fun getWhitelist(): Set<UUID> {
		return whitelist
	}

	fun copy(): UUIDFilterUtil {
		val copy = UUIDFilterUtil()
		copy.blacklist.addAll(blacklist)
		copy.whitelist.addAll(whitelist)
		return copy
	}

	fun <T : EntityAccess> filter(entitys: Iterable<T>): Set<T> {
		val entityHashSet: MutableSet<T> = HashSet()
		for (entity in entitys) {
			if (filter(entity)) {
				entityHashSet.add(entity)
			}
		}
		return entityHashSet
	}

	fun <T : EntityAccess> filter(entity: T): Boolean {
		return filter(entity.uuid)
	}

	/**
	 * 过滤
	 * 
	 * 
	 * 白名单优先级大于黑名单
	 * 
	 * @param uuid 要判断的uuid
	 * @return 是否通过
	 */
	fun filter(uuid: UUID): Boolean {
		if (whitelist.isEmpty() && blacklist.isEmpty()) {
			return true
		}
		return whitelist.contains(uuid) || !blacklist.contains(uuid)
	}

	fun <T : EntityAccess> filter(entitys: MutableCollection<T>): Set<T> {
		return entitys.stream().filter(Predicate { entity -> this.filter(entity) }).collect(Collectors.toSet())
	}

	fun filterPlayers(level: Level): Set<Player> {
		return level.players().stream().filter { entity -> this.filter(entity) }.collect(Collectors.toSet())
	}

	companion object {
		val CODEC: Codec<UUIDFilterUtil> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					UUIDUtil.CODEC.listOf().fieldOf("blacklist").forGetter { it.getBlacklist().stream().toList() },
					UUIDUtil.CODEC.listOf().fieldOf("whitelist").forGetter { it.getWhitelist().stream().toList() }
				).apply(instance, ::UUIDFilterUtil)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, UUIDFilterUtil> =
			StreamCodec.composite(
				UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()),
				{ it.getBlacklist().stream().toList() },
				UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()),
				{ it.getWhitelist().stream().toList() },
				::UUIDFilterUtil
			)

		fun create(): UUIDFilterUtil {
			return UUIDFilterUtil()
		}
	}
}
