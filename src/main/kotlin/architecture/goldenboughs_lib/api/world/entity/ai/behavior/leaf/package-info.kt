/**
 * 行为树叶节点包
 * 
 * 包含所有具体的行为执行节点，直接控制实体的动作和状态
 * 
 * <h2>主要功能分类：</h2>
 * 
 *  * **动画控制：**[AnimCtrlAction]、[AnimTriggerAction]
 *  * **移动行为：**冲刺 ([DashAction])、飞行 ([FlyTowardTargetAction])、游走 ([RandomStrollAction])
 *  * **攻击行为：**跳跃攻击 ([JumpAttackAction])、射击 ([ShootAction])
 *  * **属性控制：**属性修改 ([AttributeModifierAction])、设置属性 ([SetAttributeAction])
 *  * **状态同步：**数据同步 ([SyncAction])、标志位同步 ([SyncFlagAction])
 *  * **基础行为：**等待 ([WaitAction])、注视 ([LookAtTargetAction])、物理控制 ([SetNoPhysicsAction])
 * 
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

