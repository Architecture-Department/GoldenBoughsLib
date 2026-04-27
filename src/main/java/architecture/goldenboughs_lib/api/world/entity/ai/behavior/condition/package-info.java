/**
 * 行为树条件包
 * <p>包含所有用于检查行为执行前提条件的接口和实现</p>
 *
 * <h2>主要组件：</h2>
 * <ul>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.ConditionBT} - 条件接口（函数式接口）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition.AbstractConditionLeaf} - 条件叶节点抽象基类</li>
 * </ul>
 *
 * <h2>内置条件：</h2>
 * <ul>
 *   <li>{@code TargetExistCondition} - 目标存在条件</li>
 *   <li>{@code DistanceLowerThanCondition} - 距离小于阈值条件</li>
 *   <li>{@code HealthLowerThanCondition} - 生命值低于阈值条件</li>
 *   <li>{@code AngleLowerThanCondition} - 角度小于阈值条件</li>
 *   <li>{@code AndCondition}/{@code OrCondition}/{@code NotCondition} - 逻辑组合条件</li>
 * </ul>
 *
 * <h2>使用方式：</h2>
 * <pre>
 * // 直接使用 Lambda 表达式
 * Condition hasTarget = () -> mob.getTarget() != null;
 *
 * // 组合条件
 * Condition canAttack = Condition.and(
 *     new TargetExistCondition(mob),
 *     new DistanceLowerThanCondition(mob, 15)
 * );
 *
 * // 在行为中添加条件
 * selector.addWithCondition(canAttack, new ShootAction(mob));
 * </pre>
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition;
