package architecture.goldenboughs_lib.module.lc_damage.capability

import architecture.goldenboughs_lib.module.lc_damage.api.LcLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

/**
 * 可以通过继承该接口实现自定义LcLevel获取但仍然推荐通过能力系统注册
 */
interface IBlockLcLevel {
	/**
	 * 返回null则不参与等级系统处理
	 */
	fun getLcLevel(level: Level, pos: BlockPos): LcLevel?
}
