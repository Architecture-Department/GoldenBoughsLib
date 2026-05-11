package architecture.goldenboughs_lib.mixed

import architecture.goldenboughs_lib.api.NoMixinException
import net.neoforged.neoforge.common.damagesource.DamageContainer
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent

interface `ILivingDamageEvent$Post` {
	fun `goldenboughs_lib$getDamageContainer`(): DamageContainer {
		throw NoMixinException()
	}

	companion object {
		@JvmStatic
		fun of(obj: LivingDamageEvent.Post): `ILivingDamageEvent$Post` {
			return obj as `ILivingDamageEvent$Post`
		}
	}
}
