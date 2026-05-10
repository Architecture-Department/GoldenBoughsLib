package architecture.goldenboughs_lib.common.components

import architecture.goldenboughs_lib.api.virtue.VirtueRating
import architecture.goldenboughs_lib.api.virtue.VirtueRating.Companion.getRating
import architecture.goldenboughs_lib.api.virtue.VirtueType
import architecture.goldenboughs_lib.core.GoldenBoughsLib
import architecture.goldenboughs_lib.init.LibAttributes
import architecture.goldenboughs_lib.init.LibDataComponentTypes
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import net.minecraft.util.StringRepresentable.fromEnum
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer
import java.util.function.Function

/**
 * 物品四色属性能力使用要求提示
 *
 *
 * 当一个最后一个值为-1就是包括大于
 */
@JvmRecord
data class ItemVirtueUsageReq(
	val fortitude: List<UsageReq>,
	val prudence: List<UsageReq>,
	val temperance: List<UsageReq>,
	val justice: List<UsageReq>,
	val composite: List<UsageReq>
) : TooltipProvider {
	override fun addToTooltip(
		context: Item.TooltipContext, tooltipAdder: Consumer<Component?>, tooltipFlag: TooltipFlag
	) {
		if (this.isEmpty) {
			return
		}
		val player: Player? = Minecraft.getInstance().player
		tooltipAdder.accept(Component.translatable(USE_CONDITION).withColor(0xAAAAAA))

		val detailed = player != null && (player.isCreative && tooltipFlag.hasShiftDown() || player.getAttributeValue(
			LibAttributes.INTELLIGENCE_DEPARTMENT_ACTIVATION
		) >= 1)
		for (type in VirtueType.entries) {
			val component = analysisUsageReq(type, detailed) ?: continue

			tooltipAdder.accept(Component.literal(" ").append(component))
		}
	}

	/**
	 * 获取属性对应的使用要求
	 */
	fun analysisUsageReq(virtueType: VirtueType, isDetailed: Boolean): Component? {
		var isDetailed = isDetailed
		val list = getAttributeList(virtueType)
		if (list.isEmpty()) {
			return null
		}

		val mutableComponent = Component.translatable(virtueType.tooltipName)

		if (virtueType == VirtueType.COMPOSITE) {
			isDetailed = false
		} else {
			mutableComponent.withColor(virtueType.colourValue)
		}

		return Component.empty().append(mutableComponent).append(getUsageReqComponent(list, isDetailed))
	}

	private fun getAttributeList(attribute: VirtueType): List<UsageReq> {
		return when (attribute) {
			VirtueType.FORTITUDE -> this.fortitude
			VirtueType.PRUDENCE -> this.prudence
			VirtueType.TEMPERANCE -> this.temperance
			VirtueType.JUSTICE -> this.justice
			VirtueType.COMPOSITE -> this.composite
		}
	}

	/**
	 * 根据使用需求列表生成对应的文本组件
	 *
	 * @param list       使用需求列表
	 * @param isDetailed 是否显示详细信息（true时显示具体数值，false时显示等级名称）
	 * @return 包含使用需求描述的文本组件
	 */
	private fun getUsageReqComponent(list: List<UsageReq>, isDetailed: Boolean): MutableComponent {
		val component: MutableComponent
		val size = list.size
		val lastUsageReq: UsageReq = list[list.size - 1]
		val lastValue = lastUsageReq.value
		val `object`: Any = if (isDetailed) lastValue else getRating(lastValue).virtueName
		val type = lastUsageReq.type

		// 检查非法的使用需求类型组合
		require(!(size >= 2 && type == UsageReq.Type.NOT_TO_EXCEED)) { "Invalid UsageReq type: $type, list: $list" }

		// 处理单个使用需求的情况
		if (size == 1) {
			// 不超过
			if (type == UsageReq.Type.NOT_TO_EXCEED) {
				component = Component.translatable(NOT_TO_EXCEED, `object`)
				return component.withColor(0xAAAAAA)
			}
			// 不低于
			if (type == UsageReq.Type.NOT_LOWER_THAN) {
				component = Component.translatable(NOT_LOWER_THAN, `object`)
				return component.withColor(0xAAAAAA)
			}
		} else if (size == 2) {
			// 处理区间范围的情况
			if (type != UsageReq.Type.NOT_LOWER_THAN) {
				val value = list[1].value
				val object1: Any = if (isDetailed) value else getRating(value).virtueName
				component = Component.translatable(INTERVAL, `object`, object1)
				return component.withColor(0xAAAAAA)
			}
		}

		// 处理多个离散值的情况
		component = Component.translatable(REQUIREMENT)
		for (usageReq in list) {
			val value = usageReq.value
			val object1: Any = if (isDetailed) value else getRating(value).virtueName
			component.append(Component.literal(" $object1"))
		}
		return component.withColor(0xAAAAAA)
	}

	val isEmpty: Boolean
		get() = this.fortitude.isEmpty() &&
			this.prudence.isEmpty() &&
			this.temperance.isEmpty() &&
			this.justice.isEmpty() &&
			this.composite.isEmpty()

	class Builder {
		private val fortitude: MutableList<UsageReq> = ArrayList()
		private val prudence: MutableList<UsageReq> = ArrayList()
		private val temperance: MutableList<UsageReq> = ArrayList()
		private val justice: MutableList<UsageReq> = ArrayList()
		private val composite: MutableList<UsageReq> = ArrayList()

		/**
		 * 设置勇气属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		fun fortitude(value: VirtueRating): Builder {
			return updateList(fortitude, value.minValue, false)
		}

		/**
		 * 更新单个值列表
		 */
		private fun updateList(list: MutableList<UsageReq>, value: Int, isNotToExceed: Boolean): Builder {
			list.clear()
			list.add(UsageReq(value, if (isNotToExceed) UsageReq.Type.NOT_TO_EXCEED else UsageReq.Type.NOT_LOWER_THAN))
			return this
		}

		/**
		 * 设置谨慎属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		fun prudence(value: VirtueRating): Builder {
			return updateList(prudence, value.minValue, false)
		}

		/**
		 * 设置自律属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		fun temperance(value: VirtueRating): Builder {
			return updateList(temperance, value.minValue, false)
		}

		/**
		 * 设置正义属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		fun justice(value: VirtueRating): Builder {
			return updateList(justice, value.minValue, false)
		}

		/**
		 * 设置综合属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		fun composite(value: VirtueRating): Builder {
			return updateList(composite, value.minValue, false)
		}

		/**
		 * 设置勇气属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		fun fortitude(value: Int): Builder {
			return updateList(fortitude, value, false)
		}

		/**
		 * 设置谨慎属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		fun prudence(value: Int): Builder {
			return updateList(prudence, value, false)
		}

		/**
		 * 设置自律属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		fun temperance(value: Int): Builder {
			return updateList(temperance, value, false)
		}

		/**
		 * 设置正义属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		fun justice(value: Int): Builder {
			return updateList(justice, value, false)
		}

		/**
		 * 设置综合属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		fun composite(value: Int): Builder {
			return updateList(composite, value, false)
		}

		/**
		 * 构建品德使用要求对象
		 *
		 * @return 品德使用要求对象
		 */
		fun build(): ItemVirtueUsageReq {
			return ItemVirtueUsageReq(
				this.fortitude,
				this.prudence,
				this.temperance,
				this.justice,
				this.composite
			)
		}

		val isEmpty: Boolean
			/**
			 * 判断是否为空（没有任何属性要求）
			 *
			 * @return 如果没有设置任何属性要求则返回true，否则返回false
			 */
			get() = this.fortitude.isEmpty() && this.prudence.isEmpty() && this.temperance.isEmpty() && this.justice.isEmpty() && this.composite.isEmpty()

		/**
		 * 设置勇气属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun fortitude(value: Int, isNotToExceed: Boolean): Builder {
			return updateList(fortitude, value, isNotToExceed)
		}

		/**
		 * 设置勇气属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun fortitude(value: VirtueRating, isNotToExceed: Boolean): Builder {
			return updateList(fortitude, value.minValue, isNotToExceed)
		}

		/**
		 * 设置勇气属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		fun fortitude(min: Int, max: Int): Builder {
			return updateListRange(fortitude, min, max)
		}

		// 更新区间值列表
		private fun updateListRange(list: MutableList<UsageReq>, min: Int, max: Int): Builder {
			list.clear()
			list.add(UsageReq(min, UsageReq.Type.NOT_TO_EXCEED))
			list.add(UsageReq(max, UsageReq.Type.NOT_LOWER_THAN))
			return this
		}

		/**
		 * 设置勇气属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		fun fortitude(min: VirtueRating, max: VirtueRating): Builder {
			return updateListRange(fortitude, min.minValue, max.minValue)
		}

		/**
		 * 设置勇气属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		fun fortitudeOf(vararg values: Int): Builder {
			return updateListValues(fortitude, *values)
		}

		// 更新多个特定值列表（整数版本）
		private fun updateListValues(list: MutableList<UsageReq>, vararg values: Int): Builder {
			list.clear()
			for (value in values) {
				list.add(UsageReq(value, UsageReq.Type.EQUAL))
			}
			return this
		}

		/**
		 * 设置勇气属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		fun fortitudeOf(vararg values: VirtueRating): Builder {
			return updateListValues(fortitude, *values)
		}

		// 更新多个特定值列表（等级版本）
		private fun updateListValues(list: MutableList<UsageReq>, vararg values: VirtueRating): Builder {
			list.clear()
			for (value in values) {
				list.add(UsageReq(value.rating, UsageReq.Type.EQUAL))
			}
			return this
		}

		/**
		 * 设置谨慎属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun prudence(value: Int, isNotToExceed: Boolean): Builder {
			return updateList(prudence, value, isNotToExceed)
		}

		/**
		 * 设置谨慎属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun prudence(value: VirtueRating, isNotToExceed: Boolean): Builder {
			return updateList(prudence, value.minValue, isNotToExceed)
		}

		/**
		 * 设置谨慎属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		fun prudence(min: Int, max: Int): Builder {
			return updateListRange(prudence, min, max)
		}

		/**
		 * 设置谨慎属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		fun prudence(min: VirtueRating, max: VirtueRating): Builder {
			return updateListRange(prudence, min.minValue, max.minValue)
		}

		/**
		 * 设置谨慎属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		fun prudenceOf(vararg values: Int): Builder {
			return updateListValues(prudence, *values)
		}

		/**
		 * 设置谨慎属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		fun prudenceOf(vararg values: VirtueRating): Builder {
			return updateListValues(prudence, *values)
		}

		/**
		 * 设置自律属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun temperance(value: Int, isNotToExceed: Boolean): Builder {
			return updateList(temperance, value, isNotToExceed)
		}

		/**
		 * 设置自律属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun temperance(value: VirtueRating, isNotToExceed: Boolean): Builder {
			return updateList(temperance, value.minValue, isNotToExceed)
		}

		/**
		 * 设置自律属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		fun temperance(min: Int, max: Int): Builder {
			return updateListRange(temperance, min, max)
		}

		/**
		 * 设置自律属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		fun temperance(min: VirtueRating, max: VirtueRating): Builder {
			return updateListRange(temperance, min.minValue, max.minValue)
		}

		/**
		 * 设置自律属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		fun temperanceOf(vararg values: Int): Builder {
			return updateListValues(temperance, *values)
		}

		/**
		 * 设置自律属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		fun temperanceOf(vararg values: VirtueRating): Builder {
			return updateListValues(temperance, *values)
		}

		/**
		 * 设置正义属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun justice(value: Int, isNotToExceed: Boolean): Builder {
			return updateList(justice, value, isNotToExceed)
		}

		/**
		 * 设置正义属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun justice(value: VirtueRating, isNotToExceed: Boolean): Builder {
			return updateList(justice, value.minValue, isNotToExceed)
		}

		/**
		 * 设置正义属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		fun justice(min: Int, max: Int): Builder {
			return updateListRange(justice, min, max)
		}

		/**
		 * 设置正义属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		fun justice(min: VirtueRating, max: VirtueRating): Builder {
			return updateListRange(justice, min.minValue, max.minValue)
		}

		/**
		 * 设置正义属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		fun justiceOf(vararg values: Int): Builder {
			return updateListValues(justice, *values)
		}

		/**
		 * 设置正义属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		fun justiceOf(vararg values: VirtueRating): Builder {
			return updateListValues(justice, *values)
		}

		/**
		 * 设置综合属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun composite(value: Int, isNotToExceed: Boolean): Builder {
			return updateList(composite, value, isNotToExceed)
		}

		/**
		 * 设置综合属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		fun composite(value: VirtueRating, isNotToExceed: Boolean): Builder {
			return updateList(composite, value.minValue, isNotToExceed)
		}

		/**
		 * 设置综合属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		fun composite(min: Int, max: Int): Builder {
			return updateListRange(composite, min, max)
		}

		/**
		 * 设置综合属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		fun composite(min: VirtueRating, max: VirtueRating): Builder {
			return updateListRange(composite, min.minValue, max.minValue)
		}

		/**
		 * 设置综合属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		fun compositeOf(vararg values: Int): Builder {
			return updateListValues(composite, *values)
		}

		/**
		 * 设置综合属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		fun compositeOf(vararg values: VirtueRating): Builder {
			return updateListValues(composite, *values)
		}

		companion object {
			/**
			 * @param fortitude  勇气
			 * @param prudence   谨慎
			 * @param temperance 自律
			 * @param justice    正义
			 * @param composite  综合等级
			 */
			@JvmStatic
			fun of(
				fortitude: VirtueRating?,
				prudence: VirtueRating?,
				temperance: VirtueRating?,
				justice: VirtueRating?,
				composite: VirtueRating?
			): Builder {
				val builder = Builder()
				fortitude?.let { builder.fortitude(it) }
				prudence?.let { builder.prudence(it) }
				temperance?.let { builder.temperance(it) }
				justice?.let { builder.justice(it) }
				composite?.let { builder.composite(it) }
				return builder
			}

			/**
			 * @param fortitude  勇气
			 * @param prudence   谨慎
			 * @param temperance 自律
			 * @param justice    正义
			 * @param composite  综合等级
			 */
			@JvmStatic
			fun of(
				fortitude: Int = 0,
				prudence: Int = 0,
				temperance: Int = 0,
				justice: Int = 0,
				composite: Int = 0
			): Builder {
				val builder = Builder()
				if (fortitude != 0) {
					builder.fortitude(fortitude)
				}
				if (prudence != 0) {
					builder.prudence(prudence)
				}
				if (temperance != 0) {
					builder.temperance(temperance)
				}
				if (justice != 0) {
					builder.justice(justice)
				}
				if (composite != 0) {
					builder.composite(composite)
				}
				return builder
			}

			/**
			 * 将构建器添加到物品属性中
			 *
			 * @param properties 物品属性
			 * @param builder    品德使用要求构建器
			 */
			fun add(properties: Item.Properties, builder: Builder?) {
				if (builder == null || (builder.isEmpty)) {
					return
				}

				properties.component(LibDataComponentTypes.ITEM_VIRTUE_USAGE_REQ, builder.build())
			}
		}
	}

	@JvmRecord
	data class UsageReq(
		val value: Int,
		val type: Type
	) {
		enum class Type(
			val index: Int,
			private val usageReqName: String,
			val symbol: Char
		) : StringRepresentable {
			/**
			 * 无限制
			 */
			NONE(0, "none", ' '),

			/**
			 * 不超过
			 */
			NOT_TO_EXCEED(1, "not_to_exceed", ']'),

			/**
			 * 不低于
			 */
			NOT_LOWER_THAN(2, "not_lower_than", '['),

			/**
			 * 等于
			 */
			EQUAL(3, "equal", '='), ;

			override fun getSerializedName(): String {
				return usageReqName
			}

			companion object {
				@JvmField
				val CODEC: Codec<Type> = fromEnum { entries.toTypedArray() }
					.validate { result -> DataResult.success(result) }

				@JvmField
				val STREAM_CODEC: StreamCodec<ByteBuf, Type> = ByteBufCodecs.idMapper(
					ByIdMap.continuous(
						Type::index,
						entries.toTypedArray(),
						ByIdMap.OutOfBoundsStrategy.WRAP
					),
					Type::index
				)

				@JvmStatic
				fun byName(name: String): Type {
					for (value in entries) {
						if (value.usageReqName == name) {
							return value
						}
					}
					return NONE
				}

				@JvmStatic
				fun byIndex(index: Int): Type {
					for (value in entries) {
						if (value.index == index) {
							return value
						}
					}
					return NONE
				}
			}
		}

		companion object {
			@JvmField
			val MAP_CODEC: MapCodec<UsageReq> =
				RecordCodecBuilder.mapCodec<UsageReq>(Function { thisOptionsInstance ->
					thisOptionsInstance.group(
						Codec.INT.fieldOf("value").forGetter(UsageReq::value),
						Type.CODEC.fieldOf("type").forGetter(UsageReq::type)
					).apply(thisOptionsInstance, ::UsageReq)
				})

			@JvmField
			val CODEC: Codec<UsageReq> = MAP_CODEC.codec()

			@JvmField
			val STREAM_CODEC: StreamCodec<ByteBuf, UsageReq> = StreamCodec.composite(
				ByteBufCodecs.INT,
				UsageReq::value,
				Type.STREAM_CODEC,
				UsageReq::type,
				::UsageReq
			)
		}
	}

	companion object {
		/**
		 * 使用条件
		 */
		@JvmField
		val USE_CONDITION: String = GoldenBoughsLib.modRlText("tooltip.use_condition")

		/**
		 * 需求
		 */
		@JvmField
		val REQUIREMENT: String = GoldenBoughsLib.modRlText("tooltip.requirement")

		/**
		 * 区间
		 */
		@JvmField
		val INTERVAL: String = GoldenBoughsLib.modRlText("tooltip.interval")

		/**
		 * 不超过
		 */
		@JvmField
		val NOT_TO_EXCEED: String = GoldenBoughsLib.modRlText("tooltip.not_to_exceed")

		/**
		 * 不低于
		 */
		@JvmField
		val NOT_LOWER_THAN: String = GoldenBoughsLib.modRlText("tooltip.not_lower_than")

		@JvmField
		val LIST_STREAM_CODEC: StreamCodec<ByteBuf, List<UsageReq>> = UsageReq.STREAM_CODEC.apply(ByteBufCodecs.list())

		@JvmField
		val STREAM_CODEC: StreamCodec<ByteBuf, ItemVirtueUsageReq> =
			StreamCodec.composite(
				LIST_STREAM_CODEC,
				ItemVirtueUsageReq::fortitude,
				LIST_STREAM_CODEC,
				ItemVirtueUsageReq::prudence,
				LIST_STREAM_CODEC,
				ItemVirtueUsageReq::temperance,
				LIST_STREAM_CODEC,
				ItemVirtueUsageReq::justice,
				LIST_STREAM_CODEC,
				ItemVirtueUsageReq::composite,
				::ItemVirtueUsageReq
			)

		@JvmField
		val LIST_CODEC: Codec<List<UsageReq>> = Codec.list<UsageReq>(UsageReq.CODEC)

		@JvmField
		val CODEC: Codec<ItemVirtueUsageReq> =
			RecordCodecBuilder.create(Function { instance ->
				instance.group(
					LIST_CODEC.fieldOf("fortitude").forGetter(ItemVirtueUsageReq::fortitude),
					LIST_CODEC.fieldOf("prudence").forGetter(ItemVirtueUsageReq::prudence),
					LIST_CODEC.fieldOf("temperance").forGetter(ItemVirtueUsageReq::temperance),
					LIST_CODEC.fieldOf("justice").forGetter(ItemVirtueUsageReq::justice),
					LIST_CODEC.fieldOf("composite").forGetter(ItemVirtueUsageReq::composite)
				).apply(instance, ::ItemVirtueUsageReq)
			})

		@JvmField
		val EMPTY: ItemVirtueUsageReq = Builder().build()

		@JvmStatic
		private fun validateCompositeRatingValue(attribute: VirtueType?, value: Int) {
			assert(attribute != VirtueType.COMPOSITE || value != 6) {
				"Composite Rating must be between [-1, 1, 2, 3, 4, 5]. Currently, it is: %d".format(value)
			}
		}

		@JvmStatic
		private fun getParameterComponent(detailed: Boolean, value: Int): Component {
			return if (detailed) Component.literal(value.toString()) else Component.translatable(getRating(value).virtueName)
		}
	}
}
