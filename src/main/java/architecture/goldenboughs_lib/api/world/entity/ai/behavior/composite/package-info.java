/**
 * 行为树组合节点包
 * <p>包含用于组织和管理多个子节点的组合节点，实现复杂的行为逻辑</p>
 *
 * <h2>主要组件：</h2>
 * <ul>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.CompositeNode} - 组合节点抽象基类</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.SequenceNode} - 序列节点（AND 逻辑，按顺序执行直到全部成功）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.SelectorNode} - 选择节点（OR 逻辑，按顺序执行直到有一个成功）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.ParallelNode} - 并行节点（同时执行所有子节点）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite.WeightNode} - 权重随机节点（根据权重随机选择一个子节点执行）</li>
 * </ul>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 序列节点：先移动再攻击
 * SequenceNode attackSequence = new SequenceNode()
 *     .addChild(new MoveToTargetAction(mob, 5, 10))
 *     .addChild(new JumpAttackAction(mob, 0.5f));
 *
 * // 选择节点：优先远程攻击，失败时近战
 * SelectorNode attackSelector = new SelectorNode()
 *     .addChild(new ShootAction(mob))
 *     .addChild(new DashAction(mob, 1.0f));
 * </pre>
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite;
