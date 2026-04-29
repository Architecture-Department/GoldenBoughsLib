package architecture.goldenboughs_lib.api.virtue;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibAttributes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * 正义
 */
public final class VirtueJustice extends AbstractVirtue {
	public static final double MOVEMENT_SPEED = 0.001;
	public static final double ATTACK_SPEED = 0.01;
	public static final double SWIM_SPEED = 0.01;
	public static final double JUSTICE_FLIGHT_SPEED = 0.00013;
	public static final float VANILLA_FLYING_SPEED = 0.05f;
	private static final ResourceLocation AMPLITUDE_ID = GoldenBoughsLib.modRl("justice_amplitude");
	private double flightSpeedBonus;

	public VirtueJustice(final Player holder) {
		super(holder);
	}

	@Override
	public VirtueType getVirtue() {
		return VirtueType.JUSTICE;
	}

	@Override
	public Map<AttributeInstance, Set<AttributeModifier>> getAffectedAttributeAndModifiers() {
		return Map.ofEntries(
			getAttributeAndModifiers(Attributes.MOVEMENT_SPEED),
			getAttributeAndModifiers(Attributes.ATTACK_SPEED),
			getAttributeAndModifiers(NeoForgeMod.SWIM_SPEED)
		);
	}

	@Override
	public Map<AttributeInstance, Float> getCorrelationAttributesHolder() {
		return Map.ofEntries(
			getAttributeAndValue(Attributes.MOVEMENT_SPEED, 1f),
			getAttributeAndValue(Attributes.ATTACK_SPEED, 1f),
			getAttributeAndValue(NeoForgeMod.SWIM_SPEED, 1f)
		);
	}

	@Override
	public AttributeInstance getPointsAttributeInstance() {
		return getAttribute(LibAttributes.JUSTICE_POINTS);
	}

	@Override
	public ResourceLocation getAmplitudeId() {
		return AMPLITUDE_ID;
	}

	@Override
	public void updatePoints(final int newPoints) {
		// TODO 需要提供随机处理
		setAttributeModifier(Attributes.MOVEMENT_SPEED, newPoints * MOVEMENT_SPEED, AttributeModifier.Operation.ADD_VALUE);
		setAttributeModifier(Attributes.ATTACK_SPEED, newPoints * ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE);
		setAttributeModifier(NeoForgeMod.SWIM_SPEED, newPoints * SWIM_SPEED, AttributeModifier.Operation.ADD_VALUE);
		setFlightSpeedBonus(newPoints);
	}

	@Override
	public void updateTrigger() {
	}

	public double getFlightSpeedBonus() {
		return flightSpeedBonus;
	}

	public void setFlightSpeedBonus(double flightSpeedBonus) {
		if (getFlightSpeedBonus() == flightSpeedBonus) {
			return;
		}
		updateFlightSpeedBonus(flightSpeedBonus);
		this.flightSpeedBonus = flightSpeedBonus;
		updateTrigger();
		syncData();
	}

	// TODO 重构飞行速度加成
	private void updateFlightSpeedBonus(double newFlightSpeedBonus) {
//    var abilities = getPlayer().getAbilities();
//    abilities.setFlyingSpeed(abilities.getFlyingSpeed() + (float) (newFlightSpeedBonus * JUSTICE_FLIGHT_SPEED));
	}

	public static class Serialize extends AbstractSerialize<VirtueJustice> {

		@Override
		public VirtueJustice createAttachment(final IAttachmentHolder holder, final CompoundTag nbt, final HolderLookup.Provider provider) {
			return new VirtueJustice((Player) holder);
		}

		@Override
		public @NotNull VirtueJustice read(final @NotNull IAttachmentHolder holder, final @NotNull CompoundTag nbt, final HolderLookup.@NotNull Provider provider) {
			var attachment = super.read(holder, nbt, provider);
			attachment.flightSpeedBonus = nbt.getDouble("flightSpeedBonus");
			return attachment;
		}

		@Override
		public @NotNull CompoundTag write(final @NotNull VirtueJustice attachment, final HolderLookup.@NotNull Provider provider) {
			var nbt = super.write(attachment, provider);
			nbt.putDouble("flightSpeedBonus", attachment.flightSpeedBonus);
			return nbt;
		}
	}

	public static class Sync extends AbstractSync<VirtueJustice> {
		@Override
		public VirtueJustice createAttachment(final IAttachmentHolder holder, final RegistryFriendlyByteBuf buf, @Nullable final VirtueJustice attachment) {
			return new VirtueJustice((Player) holder);
		}

		@Override
		public void write(final @NotNull RegistryFriendlyByteBuf buf, final @NotNull VirtueJustice attachment, final boolean initialSync) {
			super.write(buf, attachment, initialSync);
			buf.writeDouble(attachment.flightSpeedBonus);
		}

		@Override
		public @NotNull VirtueJustice read(final @NotNull IAttachmentHolder holder, final @NotNull RegistryFriendlyByteBuf buf, @Nullable VirtueJustice attachment) {
			var newAttachment = super.read(holder, buf, attachment);
			newAttachment.flightSpeedBonus = buf.readDouble();
			return newAttachment;
		}
	}


}
