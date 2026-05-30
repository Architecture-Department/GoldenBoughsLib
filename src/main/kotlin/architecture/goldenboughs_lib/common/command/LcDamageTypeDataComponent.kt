package architecture.goldenboughs_lib.common.command

import architecture.goldenboughs_lib.module.lc_damage.api.LcDamageType
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import kotlin.jvm.optionals.getOrNull

@JvmRecord
data class LcDamageTypeDataComponent(
	val lcDamageType: LcDamageType?,
	val canCauseLcDamageTypes: Set<LcDamageType>
) {
	companion object {
		@JvmField
		val SET_CODEC: Codec<Set<LcDamageType>> = Codec.list(LcDamageType.CODEC)
			.xmap(Sets::newHashSet, Lists::newArrayList)

		@JvmField
		val CODEC: Codec<LcDamageTypeDataComponent> = RecordCodecBuilder.create { instance ->
			instance.group(
				LcDamageType.CODEC.optionalFieldOf("lc_damage_type")
					.forGetter(LcDamageTypeDataComponent::getOptionalLcDamageType),
				SET_CODEC.fieldOf("can_cause_lc_damage_types").forGetter(LcDamageTypeDataComponent::canCauseLcDamageTypes)
			).apply(instance, ::LcDamageTypeDataComponent)
		}

		@JvmField
		val SET_STREAM_CODEC: StreamCodec<ByteBuf, Set<LcDamageType>> = ByteBufCodecs.collection(
			::HashSet, LcDamageType.STREAM_CODEC
		)

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, LcDamageTypeDataComponent> = StreamCodec.composite(
			ByteBufCodecs.optional(LcDamageType.STREAM_CODEC), LcDamageTypeDataComponent::getOptionalLcDamageType,
			SET_STREAM_CODEC, LcDamageTypeDataComponent::canCauseLcDamageTypes,
			::LcDamageTypeDataComponent
		)
	}

	constructor(lcDamageType: LcDamageType) :
		this(lcDamageType, mutableSetOf(lcDamageType))

	constructor(lcDamageType: Optional<LcDamageType>, canCauseLcDamageTypes: Set<LcDamageType>) :
		this(lcDamageType.getOrNull(), canCauseLcDamageTypes)

	constructor(lcDamageType: LcDamageType?, vararg canCauseLcDamageTypes: LcDamageType) :
		this(lcDamageType, mutableSetOf(*canCauseLcDamageTypes))

	fun getOptionalLcDamageType(): Optional<LcDamageType> {
		return Optional.ofNullable(lcDamageType)
	}
}