package architecture.goldenboughs_lib.module.virtue.api

import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibAttributes
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.attachment.IAttachmentHolder

/**
 * 谨慎
 */
final class VirtuePrudence(
	holder: Player
) : AbstractVirtue(
	holder, AMPLITUDE_ID, VirtueType.PRUDENCE
) {

	override val affectedAttributeAndModifiers: Map<AttributeInstance?, Set<AttributeModifier>>
		get() = mapOf(
			getAttributeAndModifiers(LibAttributes.MAX_RATIONALITY)
		)

	override val correlationAttributesHolder: Map<AttributeInstance?, Float>
		get() = mapOf(
			getAttributeAndValue(LibAttributes.MAX_RATIONALITY, 1f)
		)

	override val pointsAttributeInstance: AttributeInstance?
		get() = getAttribute(LibAttributes.PRUDENCE_POINTS)

	override fun updateTrigger() {
	}

	override fun updatePoints(newPoints: Int) {
		// TODO 需要提供随机处理
		setAttributeModifier(LibAttributes.MAX_RATIONALITY, newPoints.toDouble(), AttributeModifier.Operation.ADD_VALUE)
	}

	class Serialize : AbstractSerialize<VirtuePrudence>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			nbt: CompoundTag,
			provider: HolderLookup.Provider
		): VirtuePrudence {
			return VirtuePrudence(holder as Player)
		}
	}

	class Sync : AbstractSync<VirtuePrudence>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			buf: RegistryFriendlyByteBuf,
			attachment: VirtuePrudence?
		): VirtuePrudence {
			return VirtuePrudence(holder as Player)
		}
	}

	companion object {
		@JvmField
		val AMPLITUDE_ID = LibConstants.modRl("justice_amplitude")
	}
}
