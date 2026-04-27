package architecture.goldenboughs_lib.api.virtue;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.imaginarycraft.init.world.LibAttributes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * 谨慎
 */
public final class VirtuePrudence extends AbstractVirtue {
	private static final ResourceLocation AMPLITUDE_ID = GoldenBoughsLib.modRl("justice_amplitude");

	public VirtuePrudence(final Player holder) {
		super(holder);
	}

	@Override
	public VirtueType getVirtue() {
		return VirtueType.PRUDENCE;
	}

	@Override
	public Map<AttributeInstance, Set<AttributeModifier>> getAffectedAttributeAndModifiers() {
		return Map.ofEntries(
			getAttributeAndModifiers(LibAttributes.MAX_RATIONALITY)
		);
	}

	@Override
	public Map<AttributeInstance, Float> getCorrelationAttributesHolder() {
		return Map.ofEntries(
			getAttributeAndValue(LibAttributes.MAX_RATIONALITY, 1f)
		);
	}

	@Override
	public AttributeInstance getPointsAttributeInstance() {
		return getAttribute(LibAttributes.PRUDENCE_POINTS);
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
		setAttributeModifier(LibAttributes.MAX_RATIONALITY, newPoints, AttributeModifier.Operation.ADD_VALUE);
	}

	public static class Serialize extends AbstractSerialize<VirtuePrudence> {

		@Override
		public VirtuePrudence createAttachment(final IAttachmentHolder holder, final CompoundTag nbt, final HolderLookup.Provider provider) {
			return new VirtuePrudence((Player) holder);
		}
	}

	public static class Sync extends AbstractSync<VirtuePrudence> {

		@Override
		public VirtuePrudence createAttachment(final IAttachmentHolder holder, final RegistryFriendlyByteBuf buf, @Nullable final VirtuePrudence attachment) {
			return new VirtuePrudence((Player) holder);
		}
	}
}
