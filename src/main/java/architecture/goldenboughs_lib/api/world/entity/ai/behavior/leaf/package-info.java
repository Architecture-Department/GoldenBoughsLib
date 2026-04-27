/**
 * 行为树叶节点包
 * <p>包含所有具体的行为执行节点，直接控制实体的动作和状态</p>
 *
 * <h2>主要功能分类：</h2>
 * <ul>
 *   <li><strong>动画控制：</strong>{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.AnimCtrlAction}、{@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.AnimTriggerAction}</li>
 *   <li><strong>移动行为：</strong>冲刺 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.DashAction})、飞行 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.FlyTowardTargetAction})、游走 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.RandomStrollAction})</li>
 *   <li><strong>攻击行为：</strong>跳跃攻击 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.JumpAttackAction})、射击 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.ShootAction})</li>
 *   <li><strong>属性控制：</strong>属性修改 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.AttributeModifierAction})、设置属性 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.SetAttributeAction})</li>
 *   <li><strong>状态同步：</strong>数据同步 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.SyncAction})、标志位同步 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.SyncFlagAction})</li>
 *   <li><strong>基础行为：</strong>等待 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.WaitAction})、注视 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.LookAtTargetAction})、物理控制 ({@link architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf.SetNoPhysicsAction})</li>
 * </ul>
 */
package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;
