/**
 * 行为树系统包
 * <p>提供完整的实体 AI 行为树实现，包括节点、条件、装饰器等组件</p>
 *
 * <h2>子包：</h2>
 * <ul>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf} - 叶节点（具体行为执行）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.composite} - 组合节点（序列、选择、并行等）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.decoration} - 装饰节点（条件、中断等）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.condition} - 条件接口和实现</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard} - 黑板系统（数据共享）</li>
 * </ul>
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior;
