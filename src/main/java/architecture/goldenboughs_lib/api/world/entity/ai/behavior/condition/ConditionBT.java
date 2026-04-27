package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

/**
 * 行为树条件接口 - 用于检查行为执行的前提条件
 * <p>实现此接口的类可用于条件节点或带条件的行为添加</p>
 */
@FunctionalInterface
public interface ConditionBT {

	/**
	 * 非
	 */
	static NotCondition not(ConditionBT condition) {
		return new NotCondition(condition);
	}

	/**
	 * 与
	 */
	static AndCondition and(ConditionBT... conditions) {
		return new AndCondition(Lists.newArrayList(conditions));
	}

	/**
	 * 或
	 */
	static OrCondition or(ConditionBT... conditions) {
		return new OrCondition(Lists.newArrayList(conditions));
	}

	boolean check();

	default @Nullable String getDesc() {
		return null;
	}

	default ConditionBT setConDesc(String desc) {
		return this;
	}

}

