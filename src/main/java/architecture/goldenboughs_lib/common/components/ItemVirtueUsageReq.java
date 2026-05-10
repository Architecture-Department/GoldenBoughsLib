package architecture.goldenboughs_lib.common.components;

import architecture.goldenboughs_lib.api.virtue.VirtueRating;
import architecture.goldenboughs_lib.api.virtue.VirtueType;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibAttributes;
import architecture.goldenboughs_lib.init.LibDataComponentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * 物品四色属性能力使用要求提示
 * <p>
 * 当一个最后一个值为-1就是包括大于
 */
public record ItemVirtueUsageReq(List<UsageReq> fortitude, List<UsageReq> prudence,
                                 List<UsageReq> temperance, List<UsageReq> justice,
                                 List<UsageReq> composite) implements TooltipProvider {
	/**
	 * 使用条件
	 */
	public static final String USE_CONDITION = GoldenBoughsLib.modRlText("tooltip.use_condition");
	/**
	 * 需求
	 */
	public static final String REQUIREMENT = GoldenBoughsLib.modRlText("tooltip.requirement");
	/**
	 * 区间
	 */
	public static final String INTERVAL = GoldenBoughsLib.modRlText("tooltip.interval");
	/**
	 * 不超过
	 */
	public static final String NOT_TO_EXCEED = GoldenBoughsLib.modRlText("tooltip.not_to_exceed");
	/**
	 * 不低于
	 */
	public static final String NOT_LOWER_THAN = GoldenBoughsLib.modRlText("tooltip.not_lower_than");
	public static final StreamCodec<ByteBuf, ItemVirtueUsageReq> STREAM_CODEC = StreamCodec.composite(
		UsageReq.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemVirtueUsageReq::fortitude,
		UsageReq.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemVirtueUsageReq::prudence,
		UsageReq.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemVirtueUsageReq::temperance,
		UsageReq.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemVirtueUsageReq::justice,
		UsageReq.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemVirtueUsageReq::composite,
		ItemVirtueUsageReq::new);
	public static final Codec<ItemVirtueUsageReq> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.list(UsageReq.CODEC).fieldOf("fortitude").forGetter(ItemVirtueUsageReq::fortitude),
			Codec.list(UsageReq.CODEC).fieldOf("prudence").forGetter(ItemVirtueUsageReq::prudence),
			Codec.list(UsageReq.CODEC).fieldOf("temperance").forGetter(ItemVirtueUsageReq::temperance),
			Codec.list(UsageReq.CODEC).fieldOf("justice").forGetter(ItemVirtueUsageReq::justice),
			Codec.list(UsageReq.CODEC).fieldOf("composite").forGetter(ItemVirtueUsageReq::composite))
		.apply(instance, ItemVirtueUsageReq::new));

	public static final ItemVirtueUsageReq EMPTY = new Builder().build();

	private static void validateCompositeRatingValue(VirtueType attribute, int value) {
		assert attribute != VirtueType.COMPOSITE || value != 6 :
			"Composite Rating must be between [-1, 1, 2, 3, 4, 5]. Currently, it is: %d".formatted(value);
	}

	private static Component getParameterComponent(boolean detailed, int value) {
		return detailed ? Component.literal(String.valueOf(value)) : Component.translatable(VirtueRating.getRating(value).getVirtueName());
	}

	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
		if (isEmpty()) {
			return;
		}
		Player player = Minecraft.getInstance().player;
		tooltipAdder.accept(Component.translatable(USE_CONDITION).withColor(0xAAAAAA));

		boolean detailed = player != null && (player.isCreative() && tooltipFlag.hasShiftDown() || player.getAttributeValue(LibAttributes.INTELLIGENCE_DEPARTMENT_ACTIVATION) >= 1);
		for (VirtueType type : VirtueType.getEntries()) {
			Component component = analysisUsageReq(type, detailed);
			if (component == null) {
				continue;
			}
			tooltipAdder.accept(Component.literal(" ").append(component));
		}
	}

	/**
	 * 获取属性对应的使用要求
	 */
	@Nullable
	public Component analysisUsageReq(VirtueType virtueType, boolean isDetailed) {
		List<UsageReq> list = getAttributeList(virtueType);
		if (list.isEmpty()) {
			return null;
		}

		MutableComponent mutableComponent = Component.translatable(virtueType.tooltipName);

		if (virtueType == VirtueType.COMPOSITE) {
			isDetailed = false;
		} else {
			mutableComponent.withColor(virtueType.getColourValue());
		}

		return Component.empty().append(mutableComponent).append(getUsageReqComponent(list, isDetailed));
	}

	private List<UsageReq> getAttributeList(VirtueType attribute) {
		return switch (attribute) {
			case FORTITUDE -> this.fortitude;
			case PRUDENCE -> this.prudence;
			case TEMPERANCE -> this.temperance;
			case JUSTICE -> this.justice;
			case COMPOSITE -> this.composite;
		};
	}

	/**
	 * 根据使用需求列表生成对应的文本组件
	 *
	 * @param list       使用需求列表
	 * @param isDetailed 是否显示详细信息（true时显示具体数值，false时显示等级名称）
	 * @return 包含使用需求描述的文本组件
	 */
	private MutableComponent getUsageReqComponent(List<UsageReq> list, boolean isDetailed) {
		MutableComponent component;
		int size = list.size();
		UsageReq lastUsageReq = list.getLast();
		int lastValue = lastUsageReq.value;
		Object object = isDetailed ? lastValue : VirtueRating.getRating(lastValue).getVirtueName();
		UsageReq.Type type = lastUsageReq.type;

		// 检查非法的使用需求类型组合
		if (size >= 2 && type == UsageReq.Type.NOT_TO_EXCEED) {
			throw new IllegalArgumentException("Invalid UsageReq type: " + type + ", list: " + list);
		}

		// 处理单个使用需求的情况
		if (size == 1) {
			// 不超过
			if (type == UsageReq.Type.NOT_TO_EXCEED) {
				component = Component.translatable(NOT_TO_EXCEED, object);
				return component.withColor(0xAAAAAA);
			}
			// 不低于
			if (type == UsageReq.Type.NOT_LOWER_THAN) {
				component = Component.translatable(NOT_LOWER_THAN, object);
				return component.withColor(0xAAAAAA);
			}
		} else if (size == 2) {
			// 处理区间范围的情况
			if (type != UsageReq.Type.NOT_LOWER_THAN) {
				int value = list.get(1).value;
				Object object1 = isDetailed ? value : VirtueRating.getRating(value).getVirtueName();
				component = Component.translatable(INTERVAL, object, object1);
				return component.withColor(0xAAAAAA);
			}
		}

		// 处理多个离散值的情况
		component = Component.translatable(REQUIREMENT);
		for (UsageReq usageReq : list) {
			int value = usageReq.value;
			Object object1 = isDetailed ? value : VirtueRating.getRating(value).getVirtueName();
			component.append(Component.literal(" " + object1));
		}
		return component.withColor(0xAAAAAA);
	}

	public boolean isEmpty() {
		return this.fortitude.isEmpty() && this.prudence.isEmpty() && this.temperance.isEmpty() && this.justice.isEmpty() && this.composite.isEmpty();
	}

	public static class Builder {
		private final List<UsageReq> fortitude = new ArrayList<>();
		private final List<UsageReq> prudence = new ArrayList<>();
		private final List<UsageReq> temperance = new ArrayList<>();
		private final List<UsageReq> justice = new ArrayList<>();
		private final List<UsageReq> composite = new ArrayList<>();

		/**
		 * @param fortitude  勇气
		 * @param prudence   谨慎
		 * @param temperance 自律
		 * @param justice    正义
		 * @param composite  综合等级
		 */
		public static Builder of(VirtueRating fortitude, VirtueRating prudence, VirtueRating temperance, VirtueRating justice, VirtueRating composite) {
			Builder builder = new Builder();
			if (fortitude != null) {
				builder.fortitude(fortitude);
			}
			if (prudence != null) {
				builder.prudence(prudence);
			}
			if (temperance != null) {
				builder.temperance(temperance);
			}
			if (justice != null) {
				builder.justice(justice);
			}
			if (composite != null) {
				builder.composite(composite);
			}
			return builder;
		}

		/**
		 * @param fortitude  勇气
		 * @param prudence   谨慎
		 * @param temperance 自律
		 * @param justice    正义
		 * @param composite  综合等级
		 */
		public static Builder of(int fortitude, int prudence, int temperance, int justice, int composite) {
			Builder builder = new Builder();
			if (fortitude != 0) {
				builder.fortitude(fortitude);
			}
			if (prudence != 0) {
				builder.prudence(prudence);
			}
			if (temperance != 0) {
				builder.temperance(temperance);
			}
			if (justice != 0) {
				builder.justice(justice);
			}
			if (composite != 0) {
				builder.composite(composite);
			}
			return builder;
		}

		/**
		 * 将构建器添加到物品属性中
		 *
		 * @param properties 物品属性
		 * @param builder    品德使用要求构建器
		 */
		public static void add(final Item.Properties properties, final Builder builder) {
			if (builder == null || (builder.isEmpty())) {
				return;
			}

			properties.component(LibDataComponentTypes.ITEM_VIRTUE_USAGE_REQ, builder.build());
		}

		/**
		 * 设置勇气属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		public Builder fortitude(VirtueRating value) {
			return updateList(fortitude, value.getMinValue(), false);
		}

		// 更新单个值列表
		private Builder updateList(List<UsageReq> list, int value, boolean isNotToExceed) {
			list.clear();
			list.add(new UsageReq(value, isNotToExceed ? UsageReq.Type.NOT_TO_EXCEED : UsageReq.Type.NOT_LOWER_THAN));
			return this;
		}

		/**
		 * 设置谨慎属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		public Builder prudence(VirtueRating value) {
			return updateList(prudence, value.getMinValue(), false);
		}

		/**
		 * 设置自律属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		public Builder temperance(VirtueRating value) {
			return updateList(temperance, value.getMinValue(), false);
		}

		/**
		 * 设置正义属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		public Builder justice(VirtueRating value) {
			return updateList(justice, value.getMinValue(), false);
		}

		/**
		 * 设置综合属性要求（不低于）
		 *
		 * @param value 要求等级
		 * @return 构建器本身
		 */
		public Builder composite(VirtueRating value) {
			return updateList(composite, value.getMinValue(), false);
		}

		/**
		 * 设置勇气属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		public Builder fortitude(int value) {
			return updateList(fortitude, value, false);
		}

		/**
		 * 设置谨慎属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		public Builder prudence(int value) {
			return updateList(prudence, value, false);
		}

		/**
		 * 设置自律属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		public Builder temperance(int value) {
			return updateList(temperance, value, false);
		}

		/**
		 * 设置正义属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		public Builder justice(int value) {
			return updateList(justice, value, false);
		}

		/**
		 * 设置综合属性要求（不低于）
		 *
		 * @param value 要求值
		 * @return 构建器本身
		 */
		public Builder composite(int value) {
			return updateList(composite, value, false);
		}

		/**
		 * 构建品德使用要求对象
		 *
		 * @return 品德使用要求对象
		 */
		public ItemVirtueUsageReq build() {
			return new ItemVirtueUsageReq(
				Collections.unmodifiableList(this.fortitude),
				Collections.unmodifiableList(this.prudence),
				Collections.unmodifiableList(this.temperance),
				Collections.unmodifiableList(this.justice),
				Collections.unmodifiableList(this.composite));
		}

		/**
		 * 判断是否为空（没有任何属性要求）
		 *
		 * @return 如果没有设置任何属性要求则返回true，否则返回false
		 */
		public boolean isEmpty() {
			return this.fortitude.isEmpty() &&
				this.prudence.isEmpty() &&
				this.temperance.isEmpty() &&
				this.justice.isEmpty() &&
				this.composite.isEmpty();
		}

		/**
		 * 设置勇气属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder fortitude(int value, boolean isNotToExceed) {
			return updateList(fortitude, value, isNotToExceed);
		}

		/**
		 * 设置勇气属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder fortitude(VirtueRating value, boolean isNotToExceed) {
			return updateList(fortitude, value.getMinValue(), isNotToExceed);
		}

		/**
		 * 设置勇气属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		public Builder fortitude(int min, int max) {
			return updateListRange(fortitude, min, max);
		}

		// 更新区间值列表
		private Builder updateListRange(List<UsageReq> list, int min, int max) {
			list.clear();
			list.add(new UsageReq(min, UsageReq.Type.NOT_TO_EXCEED));
			list.add(new UsageReq(max, UsageReq.Type.NOT_LOWER_THAN));
			return this;
		}

		/**
		 * 设置勇气属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		public Builder fortitude(VirtueRating min, VirtueRating max) {
			return updateListRange(fortitude, min.getMinValue(), max.getMinValue());
		}

		/**
		 * 设置勇气属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		public Builder fortitudeOf(int... values) {
			return updateListValues(fortitude, values);
		}

		// 更新多个特定值列表（整数版本）
		private Builder updateListValues(List<UsageReq> list, int... values) {
			list.clear();
			for (int value : values) {
				list.add(new UsageReq(value, UsageReq.Type.EQUAL));
			}
			return this;
		}

		/**
		 * 设置勇气属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		public Builder fortitudeOf(VirtueRating... values) {
			return updateListValues(fortitude, values);
		}

		// 更新多个特定值列表（等级版本）
		private Builder updateListValues(List<UsageReq> list, VirtueRating... values) {
			list.clear();
			for (VirtueRating value : values) {
				list.add(new UsageReq(value.getRating(), UsageReq.Type.EQUAL));
			}
			return this;
		}

		/**
		 * 设置谨慎属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder prudence(int value, boolean isNotToExceed) {
			return updateList(prudence, value, isNotToExceed);
		}

		/**
		 * 设置谨慎属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder prudence(VirtueRating value, boolean isNotToExceed) {
			return updateList(prudence, value.getMinValue(), isNotToExceed);
		}

		/**
		 * 设置谨慎属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		public Builder prudence(int min, int max) {
			return updateListRange(prudence, min, max);
		}

		/**
		 * 设置谨慎属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		public Builder prudence(VirtueRating min, VirtueRating max) {
			return updateListRange(prudence, min.getMinValue(), max.getMinValue());
		}

		/**
		 * 设置谨慎属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		public Builder prudenceOf(int... values) {
			return updateListValues(prudence, values);
		}

		/**
		 * 设置谨慎属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		public Builder prudenceOf(VirtueRating... values) {
			return updateListValues(prudence, values);
		}

		/**
		 * 设置自律属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder temperance(int value, boolean isNotToExceed) {
			return updateList(temperance, value, isNotToExceed);
		}

		/**
		 * 设置自律属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder temperance(VirtueRating value, boolean isNotToExceed) {
			return updateList(temperance, value.getMinValue(), isNotToExceed);
		}

		/**
		 * 设置自律属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		public Builder temperance(int min, int max) {
			return updateListRange(temperance, min, max);
		}

		/**
		 * 设置自律属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		public Builder temperance(VirtueRating min, VirtueRating max) {
			return updateListRange(temperance, min.getMinValue(), max.getMinValue());
		}

		/**
		 * 设置自律属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		public Builder temperanceOf(int... values) {
			return updateListValues(temperance, values);
		}

		/**
		 * 设置自律属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		public Builder temperanceOf(VirtueRating... values) {
			return updateListValues(temperance, values);
		}

		/**
		 * 设置正义属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder justice(int value, boolean isNotToExceed) {
			return updateList(justice, value, isNotToExceed);
		}

		/**
		 * 设置正义属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder justice(VirtueRating value, boolean isNotToExceed) {
			return updateList(justice, value.getMinValue(), isNotToExceed);
		}

		/**
		 * 设置正义属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		public Builder justice(int min, int max) {
			return updateListRange(justice, min, max);
		}

		/**
		 * 设置正义属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		public Builder justice(VirtueRating min, VirtueRating max) {
			return updateListRange(justice, min.getMinValue(), max.getMinValue());
		}

		/**
		 * 设置正义属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		public Builder justiceOf(int... values) {
			return updateListValues(justice, values);
		}

		/**
		 * 设置正义属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		public Builder justiceOf(VirtueRating... values) {
			return updateListValues(justice, values);
		}

		/**
		 * 设置综合属性要求
		 *
		 * @param value         要求值
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder composite(int value, boolean isNotToExceed) {
			return updateList(composite, value, isNotToExceed);
		}

		/**
		 * 设置综合属性要求
		 *
		 * @param value         要求等级
		 * @param isNotToExceed true表示不超过该值，false表示不低于该值
		 * @return 构建器本身
		 */
		public Builder composite(VirtueRating value, boolean isNotToExceed) {
			return updateList(composite, value.getMinValue(), isNotToExceed);
		}

		/**
		 * 设置综合属性区间要求
		 *
		 * @param min 最小值
		 * @param max 最大值
		 * @return 构建器本身
		 */
		public Builder composite(int min, int max) {
			return updateListRange(composite, min, max);
		}

		/**
		 * 设置综合属性区间要求
		 *
		 * @param min 最小等级
		 * @param max 最大等级
		 * @return 构建器本身
		 */
		public Builder composite(VirtueRating min, VirtueRating max) {
			return updateListRange(composite, min.getMinValue(), max.getMinValue());
		}

		/**
		 * 设置综合属性多个特定值要求
		 *
		 * @param values 特定值数组
		 * @return 构建器本身
		 */
		public Builder compositeOf(int... values) {
			return updateListValues(composite, values);
		}

		/**
		 * 设置综合属性多个特定等级要求
		 *
		 * @param values 特定等级数组
		 * @return 构建器本身
		 */
		public Builder compositeOf(VirtueRating... values) {
			return updateListValues(composite, values);
		}
	}

	public record UsageReq(int value, Type type) {
		public static final MapCodec<UsageReq> MAP_CODEC = RecordCodecBuilder.mapCodec((thisOptionsInstance) -> thisOptionsInstance.group(
			Codec.INT.fieldOf("value").forGetter(UsageReq::value),
			Type.CODEC.fieldOf("type").forGetter(UsageReq::type)
		).apply(thisOptionsInstance, UsageReq::new));
		public static final Codec<UsageReq> CODEC = MAP_CODEC.codec();

		public static final StreamCodec<ByteBuf, UsageReq> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, UsageReq::value,
			Type.STREAM_CODEC, UsageReq::type,
			UsageReq::new);

		public enum Type implements StringRepresentable {
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
			EQUAL(3, "equal", '='),
			;

			public static final Codec<Type> CODEC = StringRepresentable
				.fromEnum(Type::values).validate(DataResult::success);
			public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs
				.idMapper(ByIdMap.continuous(Type::getIndex, values(), ByIdMap.OutOfBoundsStrategy.WRAP), Type::getIndex);
			private final int index;
			private final String name;
			private final char symbol;


			Type(final int index, final String name, final char symbol) {
				this.index = index;
				this.name = name;
				this.symbol = symbol;
			}

			public static Type byName(String name) {
				for (Type value : values()) {
					if (value.name.equals(name)) {
						return value;
					}
				}
				return NONE;
			}

			public static Type byIndex(int index) {
				for (Type value : values()) {
					if (value.index == index) {
						return value;
					}
				}
				return NONE;
			}

			@Override
			public @NotNull String getSerializedName() {
				return name;
			}

			public int getIndex() {
				return index;
			}

			public char getSymbol() {
				return symbol;
			}
		}
	}
}
