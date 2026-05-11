package architecture.goldenboughs_lib.eventexecute.client

import architecture.goldenboughs_lib.api.Armors
import net.minecraft.client.model.PlayerModel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem

object EntityRenderEventExecute {
	// 预定义需要隐藏模型部件的护甲映射，作为常量以避免重复创建
	private val hiddenPartsArmorMap: MutableMap<DeferredItem<out ArmorItem>, EquipmentSlot> = HashMap()
	private var itemMap: MutableMap<Item, EquipmentSlot>? = null

	@JvmStatic
	fun add(item: DeferredItem<ArmorItem>, slot: EquipmentSlot) {
		hiddenPartsArmorMap[item] = slot
	}

	@JvmStatic
	fun add(armors: Armors<out ArmorItem>) {
		armors.map.forEach { (slot, item) ->
			item?.run {
				hiddenPartsArmorMap[this] = slot
			}
		}
	}

	/**
	 * 根据特定护甲隐藏玩家部分模型
	 * 当玩家装备指定的EGO护甲时，隐藏对应的玩家模型部件
	 *
	 * @param entity      实体对象，代表当前穿戴护甲的生物
	 * @param playerModel 玩家模型对象，用于控制模型部件的可见性
	 */
	@JvmStatic
	fun hiddenParts(entity: LivingEntity, playerModel: PlayerModel<*>) {
		// 如果itemMap为空，则初始化
		if (itemMap == null) {
			itemMap = mutableMapOf()
			hiddenPartsArmorMap.forEach { (item, slot) ->
				itemMap!![item.get()] = slot
			}
		}

		// 使用集合记录已处理的装备槽位，防止重复处理
		val processedSlots: MutableSet<EquipmentSlot> = HashSet()

		// 遍历实体的护甲槽位
		for (itemStack in entity.armorSlots) {
			// 跳过空槽位
			if (itemStack.isEmpty) {
				continue
			}

			// 查找当前护甲是否在预定义的映射中
			val equipmentSlot = itemMap!![itemStack.item]
			if (equipmentSlot == null || processedSlots.contains(equipmentSlot)) {
				continue
			}
			// 如果找到匹配的护甲且该槽位未处理过，则设置模型可见性
			processedSlots.add(equipmentSlot)
			// 根据槽位设置模型可见性
			when (equipmentSlot) {
				EquipmentSlot.LEGS, EquipmentSlot.FEET -> {
					playerModel.leftPants.visible = false
					playerModel.rightPants.visible = false
				}

				EquipmentSlot.CHEST -> {
					playerModel.jacket.visible = false
					playerModel.leftSleeve.visible = false
					playerModel.rightSleeve.visible = false
				}

				else -> {}
			}
		}
	}
}
