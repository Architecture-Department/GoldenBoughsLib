package architecture.goldenboughs_lib.mixed.client

import architecture.goldenboughs_lib.api.NoMixinException
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

interface IBlockEntityWithoutLevelRendererLib {
	fun `goldenboughs_lib$renderByItem`(
		sourceLivingEntity: LivingEntity?,
		stack: ItemStack,
		displayContext: ItemDisplayContext,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		throw NoMixinException()
	}

	fun `goldenboughs_lib$getSourceLivingEntity`(): LivingEntity? {
		throw NoMixinException()
	}

	companion object {
		@JvmStatic
		fun of(obj: BlockEntityWithoutLevelRenderer): IBlockEntityWithoutLevelRendererLib {
			return obj
		}
	}
}
