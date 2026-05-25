package architecture.goldenboughs_lib.mixed.client

import architecture.goldenboughs_lib.api.NoMixinException
import net.minecraft.client.renderer.ItemModelShaper
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

interface IItemModelShaper {
	fun `goldenboughs_lib$getItemGuiModel`(item: Item): BakedModel? {
		throw NoMixinException()
	}

	fun `goldenboughs_lib$getGuiLocation`(stack: ItemStack): ModelResourceLocation {
		throw NoMixinException()
	}

	companion object {
		@JvmStatic
		fun of(obj: ItemModelShaper): IItemModelShaper {
			return obj as IItemModelShaper
		}
	}
}
