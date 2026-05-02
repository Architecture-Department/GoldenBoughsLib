package architecture.goldenboughs_lib.common.entiy.corpse.client;

import architecture.goldenboughs_lib.common.entiy.corpse.StaffCorpse;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffCorpseRenderer extends EntityRenderer<StaffCorpse> {
	private static final Map<UUID, RemotePlayer> remotePlayers = new HashMap<>();

	public StaffCorpseRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull StaffCorpse staffCorpse) {
		return MissingTextureAtlasSprite.getLocation();
	}

	@Override
	public void render(@NotNull StaffCorpse p_entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
		super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(-p_entity.getYRot()));
		if (!p_entity.isOnFace()) {
			poseStack.mulPose(Axis.YP.rotationDegrees(180));
		}
		poseStack.mulPose(Axis.XP.rotationDegrees(p_entity.isOnFace() ? 90 : -90));
		poseStack.translate(0, -1, p_entity.isOnFace() ? -2.01 / 16 : 2.01 / 16);
		RemotePlayer remotePlayer = remotePlayers.computeIfAbsent(p_entity.getOwnerUuid(),
			ownerUuid -> new RemotePlayer((ClientLevel) p_entity.level(), new GameProfile(ownerUuid, p_entity.getOwnerName())) {
				@Override
				public boolean isModelPartShown(@NonNull PlayerModelPart part) {
					return true;
				}
			});
		Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(remotePlayer).render(
			remotePlayer, 0, 1, poseStack, bufferSource, packedLight
		);
		poseStack.popPose();
	}
}
