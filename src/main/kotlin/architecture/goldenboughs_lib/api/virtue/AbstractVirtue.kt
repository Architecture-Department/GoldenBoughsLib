package architecture.goldenboughs_lib.api.virtue

import architecture.goldenboughs_lib.api.AllOpe
import it.unimi.dsi.fastutil.objects.ObjectArraySet
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.attachment.AttachmentSyncHandler
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.attachment.IAttachmentSerializer
import java.util.*
import java.util.stream.Collectors
import java.util.stream.DoubleStream

abstract class AbstractVirtue protected constructor(
	override val player: Player,
	override val amplitudeId: ResourceLocation,
	override val virtue: VirtueType
) : IVirtue {

	override fun modifyPoints(points: Int) {
		if (points == 0) {
			return
		}
		setPoints(this.points + points, amplitudeId)
	}

	override fun setPoints(points: Int, modifierId: ResourceLocation) {
		if (this.points == points) {
			return
		}
		updatePoints(points)
		val modifier = AttributeModifier(modifierId, points.toDouble(), AttributeModifier.Operation.ADD_VALUE)
		pointsAttributeInstance?.addOrReplacePermanentModifier(modifier)
		updateTrigger()
		if (!player.level().isClientSide()) {
			syncData()
		}
	}

	override fun syncData() {
		if (player.level().isClientSide()) {
			return
		}
		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		player.syncData(virtue.getAttachmentTypeHolder<AbstractVirtue>()?.value())
	}

	override var points: Int = 0
		get() {
			return pointsAttributeInstance?.value?.toInt() ?: 0
		}
		set(value) {
			if (field == value) {
				return
			}
			setPoints(value, amplitudeId)
		}

	override fun modifyPoints(points: Int, modifierId: ResourceLocation) {
		if (points == 0) {
			return
		}
		setPoints(this.points + points, modifierId)
	}

	override val rating: VirtueRating
		get() = VirtueRating.getRating(ratingPoints)

	override val ratingPoints: Int
		get() = correlationAttributesHolder.entries.stream()
			.flatMapToDouble { entry ->
				DoubleStream.of(entry.value.toDouble() * (entry.key?.baseValue ?: 0.0))
			}.sum().toInt()


	/**
	 * 获取属性值
	 *
	 * @param attributeHolder 属性
	 * @return 属性值
	 */
	protected fun getAttributeValue(attributeHolder: Holder<Attribute>): Double {
		return player.getAttributeValue(attributeHolder)
	}

	/**
	 * 获取属性基础值
	 *
	 * @param attributeHolder 属性
	 * @return 属性基础值
	 */
	protected fun getAttributeBaseValue(attributeHolder: Holder<Attribute>): Double {
		return player.getAttributeBaseValue(attributeHolder)
	}

	/**
	 * 获取属性修改器
	 *
	 * @param attributeHolder 属性
	 * @param modifierId      属性修改器id
	 * @return 属性修改器
	 */
	protected fun getAttributeModifier(
		attributeHolder: Holder<Attribute>,
		modifierId: ResourceLocation
	): AttributeModifier? {
		return getAttribute(attributeHolder)?.getModifier(modifierId)
	}

	/**
	 * 获取属性
	 *
	 * @param attributeHolder 属性
	 * @return 属性
	 */
	protected fun getAttribute(attributeHolder: Holder<Attribute>): AttributeInstance? {
		return player.getAttribute(attributeHolder)
	}

	protected fun getAttributeAndValue(
		attributeHolder: Holder<Attribute>,
		value: Float
	): Pair<AttributeInstance?, Float> {
		return getAttribute(attributeHolder) to value
	}

	protected fun getAttributeAndModifiers(attributeHolder: Holder<Attribute>):
		Pair<AttributeInstance?, Set<AttributeModifier>> {
		return getAttributeAndModifiers(attributeHolder, amplitudeId)
	}

	/**
	 * 获取属性和属性修改器
	 *
	 * @param attributeHolder 属性
	 * @param modifierIdArray 属性修改器id
	 * @return 属性和属性修改器
	 */
	protected fun getAttributeAndModifiers(
		attributeHolder: Holder<Attribute>,
		vararg modifierIdArray: ResourceLocation
	): Pair<AttributeInstance?, Set<AttributeModifier>> {
		val attributeInstance = getAttribute(attributeHolder)
		return attributeInstance to Arrays.stream(modifierIdArray)
			.map { id -> attributeInstance?.getModifier(id) }
			.filter(Objects::nonNull)
			.collect(Collectors.toCollection { ObjectArraySet.ofUnchecked() })
	}

	/**
	 * 在原来的基础上修改属性修改器
	 */
	protected fun addModifyAttributeModifier(attribute: Holder<Attribute>, value: Double) {
		addModifyAttributeModifier(amplitudeId, attribute, value)
	}

	/**
	 * 在原来的基础上修改属性修改器
	 */
	protected fun addModifyAttributeModifier(
		amplitudeId: ResourceLocation,
		attribute: Holder<Attribute>,
		value: Double
	) {
		val modifier = getAttribute(attribute)?.getModifier(amplitudeId)
		setAttributeModifier(
			amplitudeId,
			attribute,
			if (modifier == null) value
			else modifier.amount() + value, AttributeModifier.Operation.ADD_VALUE
		)
	}

	protected fun setAttributeModifier(
		amplitudeId: ResourceLocation,
		attribute: Holder<Attribute>,
		value: Double,
		operation: AttributeModifier.Operation
	) {
		getAttribute(attribute)?.addOrUpdateTransientModifier(AttributeModifier(amplitudeId, value, operation))
	}

	protected fun setAttributeModifier(
		attribute: Holder<Attribute>,
		value: Double,
		operation: AttributeModifier.Operation = AttributeModifier.Operation.ADD_VALUE
	) {
		setAttributeModifier(amplitudeId, attribute, value, operation)
	}

	@AllOpe
	abstract class AbstractSerialize<T : AbstractVirtue> : IAttachmentSerializer<CompoundTag, T> {
		override fun read(holder: IAttachmentHolder, nbt: CompoundTag, provider: HolderLookup.Provider): T {
			return createAttachment(holder, nbt, provider)
		}

		abstract fun createAttachment(
			holder: IAttachmentHolder,
			nbt: CompoundTag,
			provider: HolderLookup.Provider
		): T

		override fun write(attachment: T, provider: HolderLookup.Provider): CompoundTag {
			return CompoundTag()
		}
	}

	@AllOpe
	abstract class AbstractSync<T : AbstractVirtue> : AttachmentSyncHandler<T> {
		override fun write(buf: RegistryFriendlyByteBuf, attachment: T, initialSync: Boolean) {
		}

		override fun read(holder: IAttachmentHolder, buf: RegistryFriendlyByteBuf, previousValue: T?): T? {
			return createAttachment(holder, buf, previousValue)
		}

		abstract fun createAttachment(holder: IAttachmentHolder, buf: RegistryFriendlyByteBuf, attachment: T?): T
	}
}
