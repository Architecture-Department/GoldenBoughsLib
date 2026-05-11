package architecture.goldenboughs_lib.mixed

import net.minecraft.world.entity.player.Player

interface IPlayer {
	companion object {
		@JvmStatic
		fun of(obj: Player): IPlayer {
			return obj
		}
	}
}
