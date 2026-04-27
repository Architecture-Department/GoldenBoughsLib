package architecture.goldenboughs_lib.common.item;

import architecture.goldenboughs_lib.api.world.item.IEgoItem;
import net.minecraft.world.item.Item;

// TODO 规范化处理
public class EgoItem extends Item implements IEgoItem {
	public EgoItem(final Properties itemProperties) {
		super(itemProperties);
	}
}
