package architecture.goldenboughs_lib.util.datagen

import architecture.goldenboughs_lib.mixin.client.ModelProviderAccessor
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelProvider

object ModelProviderUtil {
	@JvmStatic
	fun ModelProvider<*>.extendWithFolder(rl: ResourceLocation): ResourceLocation {
		return (this as ModelProviderAccessor).callExtendWithFolder(rl)
	}

	@JvmStatic
	fun ItemModelProvider.extendWithFolder(rl: ResourceLocation): ResourceLocation {
		return (this as ModelProvider<*>).extendWithFolder(rl)
	}
}