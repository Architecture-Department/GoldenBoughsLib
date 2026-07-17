package architecture.goldenboughs_lib.module.virtue.api

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player

/**
 * 心核四德接口
 */
@AllOpen
interface IVirtue {
	/**
	 * 对应的心核四德类型
	 */
	val virtue: VirtueType

	/**
	 * 会影响的属性实例和属性修改器
	 */
	val affectedAttributeAndModifiers: Map<AttributeInstance?, Set<AttributeModifier>>

	/**
	 * 关联属性Holder和评级值
	 */
	val correlationAttributesHolder: Map<AttributeInstance?, Float>

	/**
	 * 点数属性实例
	 */
	val pointsAttributeInstance: AttributeInstance?

	/**
	 * 评级
	 */
	val rating: VirtueRating

	/**
	 * 数据附件处理者
	 */
	val player: Player

	/**
	 * 点数
	 */
	var points: Int

	/**
	 * 评级点数
	 */
	val ratingPoints: Int

	/**
	 * 振幅ID
	 */
	val amplitudeId: ResourceLocation

	/**
	 * 设置点数
	 * 
	 * @param points 点数
	 */
	fun setPoints(points: Int, modifierId: ResourceLocation)

	/**
	 * 同步数据
	 */
	fun syncData()

	/**
	 * 更新触发
	 * 
	 * 
	 * 在修改相关的时候触发
	 */
	fun updateTrigger()

	/**
	 * 更新点数
	 * 
	 * @param newPoints 新的点数
	 */
	fun updatePoints(newPoints: Int)

	/**
	 * 在原来的基础上修改点数
	 * 
	 * @param points 要修改的点数
	 */
	fun modifyPoints(points: Int)

	/**
	 * 在原来的基础上修改点数
	 * 
	 * @param points 要修改的点数
	 */
	fun modifyPoints(points: Int, modifierId: ResourceLocation)
}
