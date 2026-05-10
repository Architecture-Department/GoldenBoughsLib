package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier

/**
 * 属性修改器动作 - 为实体添加或移除属性修饰符
 * 
 * 包含两个子类：[Add] 用于添加修饰符，[Remove] 用于移除修饰符
 */
abstract class AttributeModifierAction private constructor(
	val living: LivingEntity,
	val attributeHolder: Holder<Attribute>,
	val id: ResourceLocation
) : BTNode() {
	class Add(
		living: LivingEntity,
		attributeHolder: Holder<Attribute>,
		id: ResourceLocation,
		val value: Double,
		val operation: AttributeModifier.Operation
	) : AttributeModifierAction(living, attributeHolder, id) {
		override fun execute(): BTStatus? {
			this.living.getAttribute(this.attributeHolder)
				?.addOrUpdateTransientModifier(AttributeModifier(id, value, operation))
			return null
		}
	}

	class Remove(living: LivingEntity, attributeHolder: Holder<Attribute>, id: ResourceLocation) :
		AttributeModifierAction(living, attributeHolder, id) {
		override fun execute(): BTStatus? {
			this.living.getAttribute(this.attributeHolder)?.removeModifier(id)
			return null
		}
	}
}
