package architecture.goldenboughs_lib.common.item

import architecture.goldenboughs_lib.api.world.item.IEgoItem
import net.minecraft.world.item.Item

// TODO 规范化处理
class EgoItem(itemProperties: Properties) : Item(itemProperties), IEgoItem
