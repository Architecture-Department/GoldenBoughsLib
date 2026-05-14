package architecture.goldenboughs_lib.mixed

import net.minecraft.world.entity.LivingEntity

interface ILivingEntityLib {
	companion object {
		@JvmStatic
		fun of(obj: LivingEntity): ILivingEntityLib {
			return obj
		}
	}
}
