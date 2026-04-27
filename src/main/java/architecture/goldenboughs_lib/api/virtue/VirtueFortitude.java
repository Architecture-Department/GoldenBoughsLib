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
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * 勇气
 */
public final class VirtueFortitude extends AbstractVirtue {
	public static final ResourceLocation AMPLITUDE_ID = GoldenBoughsLib.modRl("fortitude_amplitude");

	public VirtueFortitude(final Player holder) {
		super(holder);
	}

	@Override
	public VirtueType getVirtue() {
		return VirtueType.FORTITUDE;
	}

	@Override
	public Map<AttributeInstance, Set<AttributeModifier>> getAffectedAttributeAndModifiers() {
		return Map.ofEntries(
			getAttributeAndModifiers(Attributes.MAX_HEALTH)
		);
	}

	@Override
	public Map<AttributeInstance, Float> getCorrelationAttributesHolder() {
		return Map.ofEntries(
			getAttributeAndValue(Attributes.MAX_HEALTH, 1f)
		);
	}

	@Override
	public AttributeInstance getPointsAttributeInstance() {
		return getPlayer().getAttribute(LibAttributes.FORTITUDE_POINTS);
	}

	@Override
	public ResourceLocation getAmplitudeId() {
		return AMPLITUDE_ID;
	}

	@Override
	public void updateTrigger() {
	}

	@Override
	public void updatePoints(final int newPoints) {
		// TODO 需要提供随机处理
		setAttributeModifier(Attributes.MAX_HEALTH, newPoints, AttributeModifier.Operation.ADD_VALUE);
	}

	public static class Serialize extends AbstractSerialize<VirtueFortitude> {

		@Override
		public VirtueFortitude createAttachment(final IAttachmentHolder holder, final CompoundTag nbt, final HolderLookup.Provider provider) {
			return new VirtueFortitude((Player) holder);
		}
	}

	public static class Sync extends AbstractSync<VirtueFortitude> {

		@Override
		public VirtueFortitude createAttachment(final IAttachmentHolder holder, final RegistryFriendlyByteBuf buf, @Nullable final VirtueFortitude attachment) {
			return new VirtueFortitude((Player) holder);
		}
	}
}
