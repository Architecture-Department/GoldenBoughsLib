package architecture.goldenboughs_lib.api.world.entity;

import net.minecraft.nbt.CompoundTag;

public interface ISkillExpand {

	void readSkillsData(CompoundTag compound);

	void addSkillsData(CompoundTag compound);

	void tick();
}
