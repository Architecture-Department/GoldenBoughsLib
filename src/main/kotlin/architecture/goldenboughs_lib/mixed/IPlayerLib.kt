package architecture.goldenboughs_lib.mixed

import net.minecraft.world.entity.player.Player

interface IPlayerLib {
	companion object {
		@JvmStatic
		fun of(obj: Player): IPlayerLib {
			return obj
		}
	}
}
