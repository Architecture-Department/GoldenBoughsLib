package architecture.goldenboughs_lib.mixed.client

import architecture.goldenboughs_lib.api.NoMixinException
import net.minecraft.client.KeyMapping

interface IKeyMappingLib {
	fun `goldenboughs_lib$getClickCount`(): Int {
		throw NoMixinException()
	}

	companion object {
		@JvmStatic
		fun of(obj: KeyMapping): IKeyMappingLib {
			return obj
		}
	}
}
