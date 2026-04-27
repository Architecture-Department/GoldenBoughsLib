package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import architecture.goldenboughs_lib.util.DateUtils;
import net.minecraft.world.level.Level;

/**
 * 时间条件
 */
public class TimeCondition extends AbstractConditionLeaf {
	final int from;
	final int to;
	final Level level;

	public TimeCondition(int from, int to, Level level) {
		this.from = from;
		this.to = to;
		this.level = level;
	}

	public static TimeCondition isDay(Level level) {
		return new TimeCondition(DateUtils._04$30, DateUtils._19$30, level);
	}

	public static TimeCondition isNight(Level level) {
		return new TimeCondition(DateUtils._19$30, DateUtils._04$30, level);
	}

	@Override
	public boolean check() {
		int dayTime = (int) (level.dayTime() % 24000);
		if (from > to) {
			return (dayTime >= from || dayTime <= to);
		}
		return (dayTime >= from && dayTime <= to);
	}
}
