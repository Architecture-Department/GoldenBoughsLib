package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute

/**
 * 设置属性动作 - 直接设置实体的属性基础值
 * 
 * 会覆盖属性的当前值，不添加修饰符
 */
class SetAttributeAction(
	val living: LivingEntity,
	val attributeHolder: Holder<Attribute>,
	val value: Double
) :
	BTNode() {
	override fun execute(): BTStatus {
		this.living.getAttribute(this.attributeHolder)!!.baseValue = this.value
		return BTStatus.SUCCESS
	}
}
