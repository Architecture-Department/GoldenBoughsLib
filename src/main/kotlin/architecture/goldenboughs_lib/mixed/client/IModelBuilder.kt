package architecture.goldenboughs_lib.mixed.client

import architecture.goldenboughs_lib.api.NoMixinException
import net.neoforged.neoforge.client.model.generators.ModelBuilder

interface IModelBuilder<T : ModelBuilder<T>> {
	fun `goldenboughs_lib$getTexture`(): MutableMap<String, String> {
		throw NoMixinException()
	}

	companion object {
		@JvmStatic
		fun <T : ModelBuilder<T>> of(obj: ModelBuilder<T>): IModelBuilder<T> {
			return obj as IModelBuilder<T>
		}
	}
}
