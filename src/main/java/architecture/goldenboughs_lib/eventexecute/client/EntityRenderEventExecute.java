package architecture.goldenboughs_lib.eventexecute.client;

import architecture.goldenboughs_lib.api.Armors;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class EntityRenderEventExecute<T extends ArmorItem & GeoItem> {
	@SuppressWarnings("rawtypes")
	public static final EntityRenderEventExecute INSTANCE = new EntityRenderEventExecute<>();
	// 预定义需要隐藏模型部件的护甲映射，作为常量以避免重复创建
	private final Map<DeferredItem<T>, EquipmentSlot> HIDDEN_PARTS_ARMOR_MAP = new HashMap<>();
	private Map<Item, EquipmentSlot> itemMap = null;

	public void add(DeferredItem<T> item, EquipmentSlot slot) {
		HIDDEN_PARTS_ARMOR_MAP.put(item, slot);
	}

	public void add(Armors<T> armors) {
		armors.getMap().forEach((slot, item) -> {
			HIDDEN_PARTS_ARMOR_MAP.put(item, slot);
		});
	}

	/**
	 * 根据特定护甲隐藏玩家部分模型
	 * 当玩家装备指定的EGO护甲时，隐藏对应的玩家模型部件
	 *
	 * @param entity      实体对象，代表当前穿戴护甲的生物
	 * @param playerModel 玩家模型对象，用于控制模型部件的可见性
	 */
	public void hiddenParts(LivingEntity entity, PlayerModel<?> playerModel) {
		// 如果itemMap为空，则初始化
		if (itemMap == null) {
			itemMap = new HashMap<>();
			HIDDEN_PARTS_ARMOR_MAP.forEach((item, slot) -> {
				itemMap.put(item.get(), slot);
			});
		}

		// 使用集合记录已处理的装备槽位，防止重复处理
		Set<EquipmentSlot> processedSlots = new HashSet<>();

		// 遍历实体的护甲槽位
		for (var itemStack : entity.getArmorSlots()) {
			// 跳过空槽位
			if (itemStack.isEmpty()) {
				continue;
			}

			// 查找当前护甲是否在预定义的映射中
			EquipmentSlot equipmentSlot = itemMap.get(itemStack.getItem());
			if (equipmentSlot == null || processedSlots.contains(equipmentSlot)) {
				continue;
			}
			// 如果找到匹配的护甲且该槽位未处理过，则设置模型可见性
			processedSlots.add(equipmentSlot);
			// 根据槽位设置模型可见性
			switch (equipmentSlot) {
				case LEGS, FEET -> {
					playerModel.leftPants.visible = false;
					playerModel.rightPants.visible = false;
				}
				case CHEST -> {
					playerModel.jacket.visible = false;
					playerModel.leftSleeve.visible = false;
					playerModel.rightSleeve.visible = false;
				}
			}
		}
	}
}
