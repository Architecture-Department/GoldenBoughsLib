package architecture.goldenboughs_lib.module.virtue.api

import architecture.goldenboughs_lib.core.LibConstants
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
 * 勇气
 */
final class VirtueFortitude(
	holder: Player
) : AbstractVirtue(
	holder, AMPLITUDE_ID, VirtueType.FORTITUDE
) {

	override val affectedAttributeAndModifiers: Map<AttributeInstance?, Set<AttributeModifier>>
		get() = mapOf(getAttributeAndModifiers(Attributes.MAX_HEALTH))

	override val correlationAttributesHolder: Map<AttributeInstance?, Float>
		get() = mapOf(getAttributeAndValue(Attributes.MAX_HEALTH, 1f))

	override val pointsAttributeInstance: AttributeInstance?
		get() = player.getAttribute(LibAttributes.FORTITUDE_POINTS)

	override fun updateTrigger() {
	}

	override fun updatePoints(newPoints: Int) {
		// TODO 需要提供随机处理
		setAttributeModifier(Attributes.MAX_HEALTH, newPoints.toDouble(), AttributeModifier.Operation.ADD_VALUE)
	}

	class Serialize : AbstractSerialize<VirtueFortitude>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			nbt: CompoundTag,
			provider: HolderLookup.Provider
		): VirtueFortitude {
			return VirtueFortitude(holder as Player)
		}
	}

	class Sync : AbstractSync<VirtueFortitude>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			buf: RegistryFriendlyByteBuf,
			attachment: VirtueFortitude?
		): VirtueFortitude {
			return VirtueFortitude(holder as Player)
		}
	}

	companion object {
		@JvmField
		val AMPLITUDE_ID: ResourceLocation = LibConstants.modRl("fortitude_amplitude")
	}
}
