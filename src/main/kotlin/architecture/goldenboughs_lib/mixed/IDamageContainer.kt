package architecture.goldenboughs_lib.mixed

import net.neoforged.neoforge.common.damagesource.DamageContainer

interface IDamageContainer {
	companion object {
		@JvmStatic
		fun of(obj: DamageContainer): IDamageContainer {
			return obj as IDamageContainer
		}
	}
}
