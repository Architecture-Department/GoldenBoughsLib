package architecture.goldenboughs_lib.api.virtue

import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibAttributes
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.common.NeoForgeMod

/**
 * 正义
 */
final class VirtueJustice(
	holder: Player
) : AbstractVirtue(
	holder, AMPLITUDE_ID, VirtueType.JUSTICE
) {
	private var flightSpeedBonus = 0.0

	override val affectedAttributeAndModifiers: Map<AttributeInstance?, Set<AttributeModifier>>
		get() = mapOf(
			getAttributeAndModifiers(Attributes.MOVEMENT_SPEED),
			getAttributeAndModifiers(Attributes.ATTACK_SPEED),
			getAttributeAndModifiers(NeoForgeMod.SWIM_SPEED)
		)

	override val correlationAttributesHolder: Map<AttributeInstance?, Float>
		get() = mapOf(
			getAttributeAndValue(Attributes.MOVEMENT_SPEED, 1f),
			getAttributeAndValue(Attributes.ATTACK_SPEED, 1f),
			getAttributeAndValue(NeoForgeMod.SWIM_SPEED, 1f)
		)

	override val pointsAttributeInstance: AttributeInstance?
		get() = getAttribute(LibAttributes.JUSTICE_POINTS)

	override fun updatePoints(newPoints: Int) {
		// TODO 需要提供随机处理
		setAttributeModifier(
			Attributes.MOVEMENT_SPEED,
			newPoints * MOVEMENT_SPEED,
			AttributeModifier.Operation.ADD_VALUE
		)
		setAttributeModifier(Attributes.ATTACK_SPEED, newPoints * ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE)
		setAttributeModifier(NeoForgeMod.SWIM_SPEED, newPoints * SWIM_SPEED, AttributeModifier.Operation.ADD_VALUE)
		setFlightSpeedBonus(newPoints.toDouble())
	}

	override fun updateTrigger() {
	}

	fun getFlightSpeedBonus(): Double {
		return flightSpeedBonus
	}

	fun setFlightSpeedBonus(flightSpeedBonus: Double) {
		if (getFlightSpeedBonus() == flightSpeedBonus) {
			return
		}
		updateFlightSpeedBonus(flightSpeedBonus)
		this.flightSpeedBonus = flightSpeedBonus
		updateTrigger()
		syncData()
	}

	// TODO 重构飞行速度加成
	private fun updateFlightSpeedBonus(newFlightSpeedBonus: Double) {
//    var abilities = getPlayer().getAbilities();
//    abilities.setFlyingSpeed(abilities.getFlyingSpeed() + (float) (newFlightSpeedBonus * JUSTICE_FLIGHT_SPEED));
	}

	class Serialize : AbstractSerialize<VirtueJustice>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			nbt: CompoundTag,
			provider: HolderLookup.Provider
		): VirtueJustice {
			return VirtueJustice(holder as Player)
		}

		override fun read(
			holder: IAttachmentHolder,
			nbt: CompoundTag,
			provider: HolderLookup.Provider
		): VirtueJustice {
			val attachment = super.read(holder, nbt, provider)
			attachment.flightSpeedBonus = nbt.getDouble("flightSpeedBonus")
			return attachment
		}

		override fun write(attachment: VirtueJustice, provider: HolderLookup.Provider): CompoundTag {
			val nbt = super.write(attachment, provider)
			nbt.putDouble("flightSpeedBonus", attachment.flightSpeedBonus)
			return nbt
		}
	}

	class Sync : AbstractSync<VirtueJustice>() {
		override fun createAttachment(
			holder: IAttachmentHolder,
			buf: RegistryFriendlyByteBuf,
			attachment: VirtueJustice?
		): VirtueJustice {
			return VirtueJustice(holder as Player)
		}

		override fun write(buf: RegistryFriendlyByteBuf, attachment: VirtueJustice, initialSync: Boolean) {
			super.write(buf, attachment, initialSync)
			buf.writeDouble(attachment.flightSpeedBonus)
		}

		override fun read(
			holder: IAttachmentHolder,
			buf: RegistryFriendlyByteBuf,
			attachment: VirtueJustice?
		): VirtueJustice? {
			val newAttachment = super.read(holder, buf, attachment)
			newAttachment?.flightSpeedBonus = buf.readDouble()
			return newAttachment
		}
	}


	companion object {
		const val MOVEMENT_SPEED: Double = 0.001
		const val ATTACK_SPEED: Double = 0.01
		const val SWIM_SPEED: Double = 0.01
		const val JUSTICE_FLIGHT_SPEED: Double = 0.00013
		const val VANILLA_FLYING_SPEED: Float = 0.05f

		@JvmField
		val AMPLITUDE_ID = LibConstants.modRl("justice_amplitude")
	}
}
