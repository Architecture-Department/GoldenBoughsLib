/**
 * 行为树装饰节点包
 * <p>包含用于增强或修改子节点行为的装饰器节点，提供条件检查、中断控制等功能</p>
 *
 * <h2>主要组件：</h2>
 * <ul>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration.DecorationNode} - 装饰节点抽象基类（包含单个子节点）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration.ConditionNode} - 条件装饰节点（执行前检查条件）</li>
 * </ul>
 *
 * <h2>功能特性：</h2>
 * <ul>
 *   <li><strong>前置条件验证：</strong>在执行子节点前检查条件，不满足则立即失败</li>
 *   <li><strong>行为增强：</strong>可以在不修改原有节点的情况下添加新的控制逻辑</li>
 *   <li><strong>描述信息：</strong>支持为装饰的行为添加描述，便于调试和可视化</li>
 * </ul>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 条件节点：只有距离目标小于 10 格时才执行攻击
 * ConditionNode conditionAttack = new ConditionNode(
 *     new DistanceLowerThanCondition(mob, 10),
 *     new JumpAttackAction(mob, 0.5f)
 * );
 * </pre>
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration;
