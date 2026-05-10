package architecture.goldenboughs_lib.api.world.item

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.api.virtue.VirtueRating
import architecture.goldenboughs_lib.common.components.ItemVirtueUsageReq
import architecture.goldenboughs_lib.init.LibDataComponentTypes
import net.minecraft.world.item.Item

/**
 * 所有E.G.O类的物品都应该继承这个
 */
@AllOpe
interface IEgoItem {
	/**
	 * 武器属性构造器
	 */
	@AllOpe
	class Builder<T : Builder<T>> {
		var virtueUsageReqBuilder: ItemVirtueUsageReq.Builder = ItemVirtueUsageReq.Builder()

		fun virtueUsageReq(virtueRequirementBuilder: ItemVirtueUsageReq.Builder): T {
			this.virtueUsageReqBuilder = virtueRequirementBuilder
			return self()
		}

		protected fun self(): T {
			return this as T
		}

		/**
		 * @param fortitude  勇气
		 * @param prudence   谨慎
		 * @param temperance 自律
		 * @param justice    正义
		 * @param composite  综合等级
		 */
		fun virtueUsageReq(
			fortitude: VirtueRating?,
			prudence: VirtueRating?,
			temperance: VirtueRating?,
			justice: VirtueRating?,
			composite: VirtueRating?
		): T {
			this.virtueUsageReqBuilder = ItemVirtueUsageReq.Builder.of(fortitude, prudence, temperance, justice, composite)
			return self()
		}

		/**
		 * @param fortitude  勇气
		 * @param prudence   谨慎
		 * @param temperance 自律
		 * @param justice    正义
		 * @param composite  综合等级
		 */
		fun virtueUsageReq(
			fortitude: Int = 0,
			prudence: Int = 0,
			temperance: Int = 0,
			justice: Int = 0,
			composite: Int = 0
		): T {
			this.virtueUsageReqBuilder = ItemVirtueUsageReq.Builder.of(fortitude, prudence, temperance, justice, composite)
			return self()
		}

		fun getItemVirtueUsageReqBuilder() {

		}
	}

	companion object {
		@JvmStatic
		fun add(properties: Item.Properties, builder: IEgoWeaponItem.Builder<*>): Item.Properties {
			return properties
				.component(
					LibDataComponentTypes.ITEM_VIRTUE_USAGE_REQ,
					builder.virtueUsageReqBuilder.build()
				)
				.component(LibDataComponentTypes.IS_RESTRAIN, false)
				.stacksTo(1)
		}
	}
}