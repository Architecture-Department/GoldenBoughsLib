package architecture.goldenboughs_lib.api.world.item;

import architecture.goldenboughs_lib.api.virtue.VirtueRating;
import architecture.goldenboughs_lib.common.components.ItemVirtueUsageReq;
import architecture.goldenboughs_lib.init.LibDataComponentTypes;
import net.minecraft.world.item.Item;

/**
 * 所有E.G.O类的物品都应该继承这个
 */
public interface IEgoItem {
	static Item.Properties add(Item.Properties properties, IEgoWeaponItem.Builder<?> builder) {
		return properties
			.component(LibDataComponentTypes.ITEM_VIRTUE_USAGE_REQ, builder.virtueUsageReqBuilder.build())
			.component(LibDataComponentTypes.IS_RESTRAIN, false)
			.stacksTo(1);
	}

	/**
	 * 武器属性构造器
	 */
	class Builder<T extends Builder<T>> {
		public ItemVirtueUsageReq.Builder virtueUsageReqBuilder = new ItemVirtueUsageReq.Builder();

		public T virtueUsageReq(ItemVirtueUsageReq.Builder virtueRequirementBuilder) {
			this.virtueUsageReqBuilder = virtueRequirementBuilder;
			return self();
		}

		@SuppressWarnings("unchecked")
		protected T self() {
			return (T) this;
		}

		/**
		 * @param fortitude  勇气
		 * @param prudence   谨慎
		 * @param temperance 自律
		 * @param justice    正义
		 * @param composite  综合等级
		 */
		public T virtueUsageReq(VirtueRating fortitude, VirtueRating prudence, VirtueRating temperance, VirtueRating justice, VirtueRating composite) {
			this.virtueUsageReqBuilder = ItemVirtueUsageReq.Builder.of(fortitude, prudence, temperance, justice, composite);
			return self();
		}

		/**
		 * @param fortitude  勇气
		 * @param prudence   谨慎
		 * @param temperance 自律
		 * @param justice    正义
		 * @param composite  综合等级
		 */
		public T virtueUsageReq(int fortitude, int prudence, int temperance, int justice, int composite) {
			this.virtueUsageReqBuilder = ItemVirtueUsageReq.Builder.of(fortitude, prudence, temperance, justice, composite);
			return self();
		}
	}
}
