/**
 * 黑板系统包
 * <p>提供行为树节点间的数据共享和通信机制，实现复杂的状态传递和信息共享</p>
 *
 * <h2>主要组件：</h2>
 * <ul>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard.Blackboard} - 黑板类（存储键值对数据）</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard.IBlackboardHolder} - 黑板持有者接口</li>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard.KeyType} - 键类型枚举</li>
 * </ul>
 *
 * <h2>功能特性：</h2>
 * <ul>
 *   <li><strong>类型安全：</strong>通过 {@code KeyType} 确保存取数据的类型一致性</li>
 *   <li><strong>数据隔离：</strong>每个行为树实例可以有独立的黑板或共享黑板</li>
 *   <li><strong>灵活访问：</strong>支持获取、设置、检查键是否存在等操作</li>
 * </ul>
 *
 * <h2>使用示例：</h2>
 * <pre>
 * // 创建黑板
 * Blackboard blackboard = new Blackboard();
 *
 * // 存储数据
 * blackboard.set("targetPos", Vec3.ZERO);
 * blackboard.set("attackCount", 0);
 *
 * // 读取数据
 * Vec3 targetPos = blackboard.get("targetPos");
 * Integer count = blackboard.get("attackCount");
 * </pre>
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard;
