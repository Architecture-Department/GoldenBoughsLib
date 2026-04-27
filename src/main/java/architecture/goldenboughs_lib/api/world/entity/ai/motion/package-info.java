/**
 * 运动控制组件包
 * <p>提供实体运动控制的通用组件，支持冲刺、悬停、加速、匀速移动等功能</p>
 *
 * <h2>主要组件：</h2>
 * <ul>
 *   <li>{@link architecture.goldenboughs_lib.api.world.entity.ai.motion.DashComponent} - 冲刺组件，提供移动和定位功能</li>
 * </ul>
 *
 * <h2>功能特性：</h2>
 * <ul>
 *   <li>悬停控制：{@code hangOn()} 和 {@code hangAbove()} 方法实现目标位置的精确悬停</li>
 *   <li>加速/匀速移动：支持加速度和匀速两种移动模式</li>
 *   <li>预判系统：根据目标运动轨迹预判冲刺方向</li>
 * </ul>
 */
package architecture.goldenboughs_lib.api.world.entity.ai.motion;
