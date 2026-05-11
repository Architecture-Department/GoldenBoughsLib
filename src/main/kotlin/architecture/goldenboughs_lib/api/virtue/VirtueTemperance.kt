package architecture.goldenboughs_lib.api.virtue

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibAttachments
import architecture.goldenboughs_lib.init.LibAttributes
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.attachment.IAttachmentHolder

/**
 * 自律
 */
class VirtueTemperance(
	holder: Player
) :
	AbstractVirtue(
		holder, AMPLITUDE_ID, VirtueType.TEMPERANCE
	) {

	override val affectedAttributeAndModifiers: Map<AttributeInstance?, Set<AttributeModifier>>
		get() = mapOf(
			getAttributeAndModifiers(Attributes.BLOCK_BREAK_SPEED),
			getAttributeAndModifiers(Attributes.ATTACK_KNOCKBACK)
		)

	override val correlationAttributesHolder: Map<AttributeInstance?, Float>
		get() = mapOf(
			getAttributeAndValue(Attributes.BLOCK_BREAK_SPEED, 1f),
			getAttributeAndValue(Attributes.ATTACK_KNOCKBACK, 1f)
		)

	override val pointsAttributeInstance: AttributeInstance?
		get() = getAttribute(LibAttributes.TEMPERANCE_POINTS)


	override fun syncData() {
		player.syncData(LibAttachments.TEMPERANCE)
	}

	override fun updateTrigger() {
	}

	override fun updatePoints(newPoints: Int) {
		// TODO 需要提供随机处理
		// 挖掘速度
		setAttributeModifier(
			Attributes.BLOCK_BREAK_SPEED,
			newPoints * BLOCK_BREAK_SPEED,
			AttributeModifier.Operation.ADD_VALUE
		)
		// 击退
		setAttributeModifier(
			Attributes.ATTACK_KNOCKBACK,
			newPoints * ATTACK_KNOCKBACK_SPEED,
			AttributeModifier.Operation.ADD_VALUE
		)
	}

	class Serialize : AbstractSerialize<VirtueTemperance>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			nbt: CompoundTag,
			provider: HolderLookup.Provider
		): VirtueTemperance {
			return VirtueTemperance(holder as Player)
		}
	}

	class Sync : AbstractSync<VirtueTemperance>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			buf: RegistryFriendlyByteBuf,
			attachment: VirtueTemperance?
		): VirtueTemperance {
			return VirtueTemperance(holder as Player)
		}
	}

	companion object {
		@JvmField
		val AMPLITUDE_ID: ResourceLocation = Lib.modRl("temperance_amplitude")
		const val BLOCK_BREAK_SPEED: Double = 0.02
		const val ATTACK_KNOCKBACK_SPEED: Double = 0.015
	}
}
