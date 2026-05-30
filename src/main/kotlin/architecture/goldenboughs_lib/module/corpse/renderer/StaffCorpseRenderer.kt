package architecture.goldenboughs_lib.module.corpse.renderer

import architecture.goldenboughs_lib.module.corpse.entity.StaffCorpse
import com.mojang.authlib.GameProfile
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.RemotePlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.PlayerModelPart
import java.util.*

class StaffCorpseRenderer(context: EntityRendererProvider.Context) : EntityRenderer<StaffCorpse>(context) {
	override fun getTextureLocation(staffCorpse: StaffCorpse): ResourceLocation {
		return MissingTextureAtlasSprite.getLocation()
	}

	override fun render(
		pEntity: StaffCorpse,
		entityYaw: Float,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int
	) {
		super.render(pEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
		poseStack.pushPose()
		poseStack.mulPose(Axis.YP.rotationDegrees(-pEntity.yRot))
		if (!pEntity.isOnFace) {
			poseStack.mulPose(Axis.YP.rotationDegrees(180f))
		}
		poseStack.mulPose(Axis.XP.rotationDegrees((if (pEntity.isOnFace) 90 else -90).toFloat()))
		poseStack.translate(0.0, -1.0, if (pEntity.isOnFace) -2.01 / 16 else 2.01 / 16)
		val remotePlayer: RemotePlayer = remotePlayers.computeIfAbsent(
			pEntity.ownerUuid
		) { ownerUuid: UUID ->
			object : RemotePlayer(pEntity.level() as ClientLevel, GameProfile(ownerUuid, pEntity.ownerName)) {
				override fun isModelPartShown(part: PlayerModelPart): Boolean {
					return true
				}
			}
		}
		Minecraft.getInstance().entityRenderDispatcher.getRenderer(remotePlayer).render(
			remotePlayer, 0f, 1f, poseStack, bufferSource, packedLight
		)
		poseStack.popPose()
	}

	companion object {
		private val remotePlayers: MutableMap<UUID, RemotePlayer> = HashMap<UUID, RemotePlayer>()
	}
}