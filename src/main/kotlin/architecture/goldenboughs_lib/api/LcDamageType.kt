package architecture.goldenboughs_lib.api

import architecture.goldenboughs_lib.client.LibFontIcon
import architecture.goldenboughs_lib.core.GoldenBoughsLib
import architecture.goldenboughs_lib.init.LibAttributes
import architecture.goldenboughs_lib.init.LibDamageSources.createDamage
import architecture.goldenboughs_lib.init.LibDamageTypes
import architecture.goldenboughs_lib.init.tag.LibDamageTypeTags
import architecture.goldenboughs_lib.util.ColorUtil
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.level.Level
import org.jetbrains.annotations.Contract
import java.util.*

/**
 * 脑叶伤害类型
 */
enum class LcDamageType(
	val index: Int,
	val damageName: String,
	val charIcon: LibFontIcon,
	val char8xIcon: LibFontIcon,
	/**
	 * 对应的易伤属性
	 */
	val vulnerable: Holder<Attribute>,
	/**
	 * 对应的防御属性
	 */
	val defense: Holder<Attribute>,
	val damageType: ResourceKey<DamageType>,
	colour: String
) : ColourText, StringRepresentable {
	/**
	 * 物理
	 */
	PHYSICS(
		0,
		"physics",
		LibFontIcon.PHYSICS,
		LibFontIcon.PHYSICS_8X,
		LibAttributes.PHYSICS_VULNERABLE,
		LibAttributes.PHYSICS_DEFENSE,
		LibDamageTypes.PHYSICS,
		"#ff0000"
	),

	/**
	 * 精神
	 */
	SPIRIT(
		1,
		"spirit",
		LibFontIcon.SPIRIT,
		LibFontIcon.SPIRIT_8X,
		LibAttributes.SPIRIT_VULNERABLE,
		LibAttributes.SPIRIT_DEFENSE,
		LibDamageTypes.SPIRIT,
		"#ffffff"
	),

	/**
	 * 侵蚀
	 *
	 *
	 * 同时造成物理和精神伤害
	 */
	EROSION(
		2,
		"erosion",
		LibFontIcon.EROSION,
		LibFontIcon.EROSION_8X,
		LibAttributes.EROSION_VULNERABLE,
		LibAttributes.EROSION_DEFENSE,
		LibDamageTypes.EROSION,
		"#8a2be2"
	),

	/**
	 * 灵魂
	 *
	 *
	 * 伤害计算参考 [architecture.goldenboughs_lib.eventexecute.LcDamageEventExecutes.theSoulDamage]
	 */
	THE_SOUL(
		3,
		"the_soul",
		LibFontIcon.THE_SOUL,
		LibFontIcon.THE_SOUL_8X,
		LibAttributes.THE_SOUL_VULNERABLE,
		LibAttributes.THE_SOUL_DEFENSE,
		LibDamageTypes.THE_SOUL,
		"#00ffff"
	), ;

	override val colourValue: Int = ColorUtil.rgbColor(colour)
	override val colourName: String = damageName
	override val colourText: String = colour

	@Contract("_ -> new")
	fun getDamageSources(causingEntity: Entity): DamageSource {
		return createDamage(damageType, causingEntity)
	}

	@Contract("_, _ -> new")
	fun getDamageSources(directEntity: Entity, causingEntity: Entity): DamageSource {
		return createDamage(damageType, directEntity, causingEntity)
	}

	@Contract("_ -> new")
	fun getNoSourceDamageSources(entity: Entity): DamageSource {
		return getNoSourceDamageSources(entity.level())
	}

	@Contract("_ -> new")
	fun getNoSourceDamageSources(level: Level): DamageSource {
		return getNoSourceDamageSources(level.registryAccess())
	}

	@Contract("_ -> new")
	fun getNoSourceDamageSources(registryAccess: RegistryAccess): DamageSource {
		return DamageSource(
			registryAccess.registryOrThrow(Registries.DAMAGE_TYPE)
				.getHolderOrThrow(damageType), null, null
		)
	}

	@Contract(pure = true)
	override fun getSerializedName(): String {
		return GoldenBoughsLib.modRlText(damageName)
	}

	companion object {
		@JvmField
		val CODEC: Codec<LcDamageType> = StringRepresentable.fromEnum<LcDamageType> { entries.toTypedArray() }
			.validate { result: LcDamageType -> DataResult.success(result) }

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, LcDamageType> = ByteBufCodecs.idMapper<LcDamageType>(
			ByIdMap.continuous<LcDamageType>(
				LcDamageType::index, entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.WRAP
			), LcDamageType::index
		)

		@JvmStatic
		fun byName(name: String): LcDamageType {
			return Arrays.stream<LcDamageType>(entries.toTypedArray()).filter { d -> d.damageName == name }.findFirst()
				.orElse(null)
		}

		/**
		 * 根据[Holder]获取对应的[LcDamageType]
		 *
		 * @param damageType 伤害类型
		 * @return 伤害类型对应的伤害类型，返回NULL则绕过LC伤害系统
		 */
		@JvmStatic
		fun byDamageType(damageType: Holder<DamageType>): LcDamageType? {
			return when {
				damageType.`is`(LibDamageTypeTags.BYPASS_LC) -> null
				damageType.`is`(LibDamageTypeTags.PHYSICS) -> PHYSICS
				damageType.`is`(LibDamageTypeTags.SPIRIT) -> SPIRIT
				damageType.`is`(LibDamageTypeTags.EROSION) -> EROSION
				damageType.`is`(LibDamageTypeTags.THE_SOUL) -> THE_SOUL

				// 默认为物理伤害
				else -> PHYSICS
			}
		}
	}

	@JvmRecord
	data class Component(val lcDamageType: LcDamageType?, val canCauseLcDamageTypes: Set<LcDamageType>) {
		constructor(lcDamageType: LcDamageType) : this(lcDamageType, mutableSetOf<LcDamageType>(lcDamageType))

		constructor(lcDamageType: LcDamageType, vararg canCauseLcDamageTypes: LcDamageType) : this(
			lcDamageType, mutableSetOf(*canCauseLcDamageTypes)
		)

		companion object {
			@JvmField
			val SET_CODEC: Codec<Set<LcDamageType>> = Codec.list(LcDamageType.CODEC)
				.xmap(Sets::newHashSet, Lists::newArrayList)

			@JvmField
			val CODEC: Codec<Component> = RecordCodecBuilder.create { instance ->
				instance.group(
					LcDamageType.CODEC.fieldOf("lc_damage_type").forGetter(Component::lcDamageType),
					SET_CODEC.fieldOf("can_cause_lc_damage_types").forGetter(Component::canCauseLcDamageTypes)
				).apply(instance, ::Component)
			}

			@JvmField
			val SET_STREAM_CODEC: StreamCodec<ByteBuf, Set<LcDamageType>> = ByteBufCodecs.collection(
				::HashSet, LcDamageType.STREAM_CODEC
			)

			@JvmField
			val STREAM_CODEC: StreamCodec<ByteBuf, Component> = StreamCodec.composite(
				LcDamageType.STREAM_CODEC, Component::lcDamageType,
				SET_STREAM_CODEC, Component::canCauseLcDamageTypes,
				::Component
			)
		}
	}
}
