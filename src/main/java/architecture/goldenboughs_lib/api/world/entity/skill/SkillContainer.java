package architecture.goldenboughs_lib.api.world.entity.skill;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// TODO
public class SkillContainer {
	private final Map<String, MobSkill> skills = new HashMap<>();

	private final Function<String, MobSkill> skillFactory;

	public SkillContainer(Function<String, MobSkill> skillFactory) {
		this.skillFactory = skillFactory;
	}

	/**
	 * 读取数据
	 */
	protected void readData(CompoundTag compound) {
		skills.clear();
		compound.getAllKeys().forEach(key -> {
			MobSkill skill = getSkill(key);
			if (skill != null) {
				skill.readData(compound.getCompound(key));
			}
		});
	}

	public MobSkill getSkill(String key) {
		return skillFactory.apply(key);
	}

	/**
	 * 写入数据
	 */
	protected void addData(CompoundTag compound) {
		skills.forEach((key, skill) -> {
			CompoundTag skillTag = new CompoundTag();
			skill.addData(skillTag);
			compound.put(key, skillTag);
		});
	}

	public void tick() {
		skills.forEach((key, skill) -> skill.tick());
	}
}
