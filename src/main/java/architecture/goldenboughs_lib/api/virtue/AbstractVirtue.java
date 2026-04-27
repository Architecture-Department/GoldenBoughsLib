package architecture.goldenboughs_lib.api.virtue;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public abstract class AbstractVirtue implements IVirtue {
	private final Player player;

	protected AbstractVirtue(Player holder) {
		this.player = holder;
	}

	@Override
	public void modifyPoints(final int points) {
		if (points == 0) {
			return;
		}
		setPoints(getPoints() + points, getAmplitudeId());
	}

	@Override
	public void setPoints(final int points, final ResourceLocation modifierId) {
		if (getPoints() == points) {
			return;
		}
		updatePoints(points);
		AttributeModifier modifier = new AttributeModifier(modifierId, points, AttributeModifier.Operation.ADD_VALUE);
		getPointsAttributeInstance().addOrReplacePermanentModifier(modifier);
		updateTrigger();
		if (!getPlayer().level().isClientSide()) {
			syncData();
		}
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public void syncData() {
		if (getPlayer().level().isClientSide()) {
			return;
		}
		getPlayer().syncData(getVirtue().getAttachmentTypeHolder().value());
	}

	@Override
	public int getPoints() {
		return (int) getPointsAttributeInstance().getValue();
	}

	@Override
	public void setPoints(final int points) {
		if (getPoints() == points) {
			return;
		}
		setPoints(points, getAmplitudeId());
	}

	@Override
	public void modifyPoints(final int points, final ResourceLocation modifierId) {
		if (points == 0) {
			return;
		}
		setPoints(getPoints() + points, modifierId);
	}

	@Override
	public VirtueRating getRating() {
		return VirtueRating.getRating(getRatingPoints());
	}

	@Override
	public int getRatingPoints() {
		return (int) getCorrelationAttributesHolder().entrySet().stream()
			.flatMapToDouble(entry -> DoubleStream.of(entry.getValue() * entry.getKey().getBaseValue()))
			.sum();
	}

	/**
	 * 获取属性值
	 *
	 * @param attributeHolder 属性
	 * @return 属性值
	 */
	protected double getAttributeValue(Holder<Attribute> attributeHolder) {
		return getPlayer().getAttributeValue(attributeHolder);
	}

	/**
	 * 获取属性基础值
	 *
	 * @param attributeHolder 属性
	 * @return 属性基础值
	 */
	protected double getAttributeBaseValue(Holder<Attribute> attributeHolder) {
		return getPlayer().getAttributeBaseValue(attributeHolder);
	}

	/**
	 * 获取属性修改器
	 *
	 * @param attributeHolder 属性
	 * @param modifierId      属性修改器id
	 * @return 属性修改器
	 */
	protected AttributeModifier getAttributeModifier(Holder<Attribute> attributeHolder, ResourceLocation modifierId) {
		return getAttribute(attributeHolder).getModifier(modifierId);
	}

	/**
	 * 获取属性
	 *
	 * @param attributeHolder 属性
	 * @return 属性
	 */
	protected AttributeInstance getAttribute(Holder<Attribute> attributeHolder) {
		return getPlayer().getAttribute(attributeHolder);
	}

	protected Map.Entry<AttributeInstance, Float> getAttributeAndValue(Holder<Attribute> attributeHolder, Float value) {
		return Map.entry(getAttribute(attributeHolder), value);
	}

	protected Map.Entry<AttributeInstance, Set<AttributeModifier>> getAttributeAndModifiers(Holder<Attribute> attributeHolder) {
		return getAttributeAndModifiers(attributeHolder, getAmplitudeId());
	}

	/**
	 * 获取属性和属性修改器
	 *
	 * @param attributeHolder 属性
	 * @param modifierIdArray 属性修改器id
	 * @return 属性和属性修改器
	 */
	protected Map.Entry<AttributeInstance, Set<AttributeModifier>> getAttributeAndModifiers(Holder<Attribute> attributeHolder, ResourceLocation... modifierIdArray) {
		var attributeInstance = getAttribute(attributeHolder);
		return Map.entry(attributeInstance, Arrays.stream(modifierIdArray)
			.map(attributeInstance::getModifier)
			.filter(Objects::nonNull)
			.collect(Collectors.toCollection(ObjectArraySet::ofUnchecked)));
	}

	/**
	 * 在原来的基础上修改属性修改器
	 */
	protected void addModifyAttributeModifier(Holder<Attribute> attribute, double value) {
		addModifyAttributeModifier(getAmplitudeId(), attribute, value);
	}

	/**
	 * 在原来的基础上修改属性修改器
	 */
	protected void addModifyAttributeModifier(ResourceLocation amplitudeId, Holder<Attribute> attribute, double value) {
		var modifier = getAttribute(attribute).getModifier(amplitudeId);
		setAttributeModifier(amplitudeId, attribute, modifier == null ? value : modifier.amount() + value, AttributeModifier.Operation.ADD_VALUE);
	}

	protected void setAttributeModifier(ResourceLocation amplitudeId, Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
		getAttribute(attribute).addOrUpdateTransientModifier(new AttributeModifier(amplitudeId, value, operation));
	}

	protected void setAttributeModifier(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
		setAttributeModifier(getAmplitudeId(), attribute, value, operation);
	}

	public static abstract class AbstractSerialize<T extends AbstractVirtue> implements IAttachmentSerializer<CompoundTag, T> {

		@Override
		public @NotNull T read(final @NotNull IAttachmentHolder holder, final @NotNull CompoundTag nbt, final HolderLookup.@NotNull Provider provider) {
			return createAttachment(holder, nbt, provider);
		}

		public abstract T createAttachment(final IAttachmentHolder holder, final CompoundTag nbt, final HolderLookup.Provider provider);

		@Override
		@NotNull
		public CompoundTag write(final @NotNull T attachment, final HolderLookup.@NotNull Provider provider) {
			return new CompoundTag();
		}
	}

	public static abstract class AbstractSync<T extends AbstractVirtue> implements AttachmentSyncHandler<T> {

		@Override
		public void write(final @NotNull RegistryFriendlyByteBuf buf, final @NotNull T attachment, final boolean initialSync) {
		}

		@Override
		@NotNull
		public T read(final @NotNull IAttachmentHolder holder, final @NotNull RegistryFriendlyByteBuf buf, @Nullable T attachment) {
			return createAttachment(holder, buf, attachment);
		}

		public abstract T createAttachment(final IAttachmentHolder holder, final RegistryFriendlyByteBuf buf, @Nullable T attachment);
	}


}
