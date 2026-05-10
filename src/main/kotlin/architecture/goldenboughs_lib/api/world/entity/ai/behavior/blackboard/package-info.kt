/**
 * 黑板系统包
 * 
 * 提供行为树节点间的数据共享和通信机制，实现复杂的状态传递和信息共享
 * 
 * <h2>主要组件：</h2>
 * 
 *  * [Blackboard] - 黑板类（存储键值对数据）
 *  * [IBlackboardHolder] - 黑板持有者接口
 *  * [KeyType] - 键类型枚举
 * 
 * 
 * <h2>功能特性：</h2>
 * 
 *  * **类型安全：**通过 `KeyType` 确保存取数据的类型一致性
 *  * **数据隔离：**每个行为树实例可以有独立的黑板或共享黑板
 *  * **灵活访问：**支持获取、设置、检查键是否存在等操作
 * 
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
</pre> * 
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.blackboard
