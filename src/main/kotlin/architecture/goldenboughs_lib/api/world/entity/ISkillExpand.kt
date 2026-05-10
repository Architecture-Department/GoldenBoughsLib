package architecture.goldenboughs_lib.api.world.entity

import net.minecraft.nbt.CompoundTag

interface ISkillExpand {
	fun readSkillsData(compound: CompoundTag)

	fun addSkillsData(compound: CompoundTag)

	fun tick()
}
