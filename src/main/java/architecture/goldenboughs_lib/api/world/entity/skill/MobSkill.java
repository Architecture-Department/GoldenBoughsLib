package architecture.goldenboughs_lib.api.world.entity.skill;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.api.event.IdentifierProvider;

public abstract class MobSkill implements IdentifierProvider {
	private final ResourceLocation id;
	private final int defaultCd;
	private final int maxCd;
	private int cd;
	private boolean isTriggerCdEnd = false;

	public MobSkill(ResourceLocation id, int maxCd) {
		this(id, 0, maxCd);
	}

	public MobSkill(ResourceLocation id, int defaultCd, int maxCd) {
		this.id = id;
		this.defaultCd = defaultCd;
		this.maxCd = maxCd;
	}

	public int getCd() {
		return cd;
	}

	public void setCd(int cd) {
		this.cd = cd;
		if (cd > 0) {
			isTriggerCdEnd = false;
		}
	}

	public boolean isTriggerCdEnd() {
		return isTriggerCdEnd;
	}

	public int getMaxCd() {
		return maxCd;
	}

	public int getDefaultCd() {
		return defaultCd;
	}

	/**
	 * 技能重置冷却
	 */
	public void resetCd() {
		cd = defaultCd;
		if (cd > 0) {
			isTriggerCdEnd = false;
		}
	}

	/**
	 * 技能进入冷却
	 */
	public void enterCd() {
		cd = maxCd;
		if (cd > 0) {
			isTriggerCdEnd = false;
		}
	}

	public void tick() {
		if (cd > 0) {
			cd--;
		} else if (!isTriggerCdEnd) {
			isTriggerCdEnd = true;
			cdEnd();
		}
	}

	/**
	 * 技能冷却结束
	 */
	public abstract void cdEnd();

	/**
	 * 技能释放
	 */
	public abstract void launch();

	/**
	 * 读取数据
	 */
	public void readData(CompoundTag compound) {
		cd = compound.contains("Cd") ? compound.getInt("Cd") : defaultCd;
	}

	/**
	 * 写入数据
	 */
	public void addData(CompoundTag compound) {
		compound.putInt("Cd", cd);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
}