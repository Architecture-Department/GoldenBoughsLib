package architecture.goldenboughs_lib.mixed

import net.minecraft.world.entity.LivingEntity

interface ILivingEntity {
	companion object {
		@JvmStatic
		fun of(obj: LivingEntity): ILivingEntity {
			return obj
		}
	}
}
