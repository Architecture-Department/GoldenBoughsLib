package architecture.goldenboughs_lib.api.world.entity.ai.behavior.leaf;

import architecture.goldenboughs_lib.api.world.entity.ai.behavior.BTNode;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

/**
 * 设置属性动作 - 直接设置实体的属性基础值
 * <p>会覆盖属性的当前值，不添加修饰符</p>
 */
public class SetAttributeAction extends BTNode {

	final LivingEntity living;
	final Holder<Attribute> attributeHolder;
	final double value;

	public SetAttributeAction(LivingEntity living, Holder<Attribute> attributeHolder, double value) {
		this.living = living;
		this.attributeHolder = attributeHolder;
		this.value = value;
	}

	@Override
	public BTStatus execute() {
		this.living.getAttribute(this.attributeHolder).setBaseValue(this.value);
		return BTStatus.SUCCESS;
	}
}
