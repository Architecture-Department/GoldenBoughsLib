/**
 * 行为树条件包
 * 
 * 包含所有用于检查行为执行前提条件的接口和实现
 * 
 * <h2>主要组件：</h2>
 * 
 *  * [ConditionBT] - 条件接口（函数式接口）
 *  * [AbstractConditionLeaf] - 条件叶节点抽象基类
 * 
 * 
 * <h2>内置条件：</h2>
 * 
 *  * `TargetExistCondition` - 目标存在条件
 *  * `DistanceLowerThanCondition` - 距离小于阈值条件
 *  * `HealthLowerThanCondition` - 生命值低于阈值条件
 *  * `AngleLowerThanCondition` - 角度小于阈值条件
 *  * `AndCondition`/`OrCondition`/`NotCondition` - 逻辑组合条件
 * 
 * 
 * <h2>使用方式：</h2>
 * <pre>
 * // 直接使用 Lambda 表达式
 * Condition hasTarget = () -> mob.getTarget() != null;
 * 
 * // 组合条件
 * Condition canAttack = Condition.and(
 * new TargetExistCondition(mob),
 * new DistanceLowerThanCondition(mob, 15)
 * );
 * 
 * // 在行为中添加条件
 * selector.addWithCondition(canAttack, new ShootAction(mob));
</pre> * 
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition

