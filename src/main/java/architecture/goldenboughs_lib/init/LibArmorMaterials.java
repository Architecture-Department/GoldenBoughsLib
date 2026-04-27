package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public final class LibArmorMaterials {
	public static final DeferredRegister<ArmorMaterial> REGISTRY = GoldenBoughsLib.modRegister(BuiltInRegistries.ARMOR_MATERIAL);

	// EGO 护甲
	public static final Holder<ArmorMaterial> ZAYIN = register(
		"zayin", 1, 2, 3, 1, 3, 9,
		LibSoundEvents.ARMOR_EQUIP_ZAYIN, 0.5F, 0.0F);
	public static final Holder<ArmorMaterial> TETH = register(
		"teth", 2, 5, 6, 2, 5, 9,
		LibSoundEvents.ARMOR_EQUIP_TETH, 1.0F, 0.0F);
	public static final Holder<ArmorMaterial> HE = register(
		"he", 3, 6, 8, 3, 11, 9,
		LibSoundEvents.ARMOR_EQUIP_HE, 2.0F, 0.025F);
	public static final Holder<ArmorMaterial> WAW = register(
		"waw", 4, 7, 9, 4, 12, 9,
		LibSoundEvents.ARMOR_EQUIP_WAW, 3.0F, 0.05F);
	public static final Holder<ArmorMaterial> ALEPH = register(
		"aleph", 4, 8, 10, 5, 13, 9,
		LibSoundEvents.ARMOR_EQUIP_ALEPH, 4.0F, 0.1F);

	// 其他
	public static final Holder<ArmorMaterial> SUIT = register(
		"suit", 1, 2, 3, 1, 3, 9,
		LibSoundEvents.ARMOR_EQUIP_ZAYIN, 0.0F, 0.0F);

	/**
	 * 注册一个新的护甲材质
	 *
	 * @param id                  护甲材质的唯一标识符
	 * @param boots               靴子的防御值
	 * @param leggings            护腿的防御值
	 * @param chestplate          胸甲的防御值
	 * @param helmet              头盔的防御值
	 * @param body                身体部位的防御值
	 * @param enchantmentValue    附魔值，影响附魔等级
	 * @param equipSound          装备护甲时播放的声音
	 * @param toughness           护甲韧性值，减少受到的伤害
	 * @param knockbackResistance 击退抗性，减少被击退的距离
	 * @return 返回注册后的护甲材质Holder对象
	 */
	private static @NotNull Holder<ArmorMaterial> register(String id, int boots, int leggings, int chestplate, int helmet, int body,
	                                                       int enchantmentValue, Holder<SoundEvent> equipSound, float toughness,
	                                                       float knockbackResistance) {
		return register(id, boots, leggings, chestplate, helmet, body, enchantmentValue, equipSound, toughness, knockbackResistance,
			() -> Ingredient.of(Items.AIR));
	}

	/**
	 * 注册一个新的护甲材质，并指定修复材料
	 *
	 * @param id                  护甲材质的唯一标识符
	 * @param boots               靴子的防御值
	 * @param leggings            护腿的防御值
	 * @param chestplate          胸甲的防御值
	 * @param helmet              头盔的防御值
	 * @param body                身体部位的防御值
	 * @param enchantmentValue    附魔值，影响附魔等级
	 * @param equipSound          装备护甲时播放的声音
	 * @param toughness           护甲韧性值，减少受到的伤害
	 * @param knockbackResistance 击退抗性，减少被击退的距离
	 * @param repairIngredient    用于修复护甲的材料
	 * @return 返回注册后的护甲材质Holder对象
	 */
	private static @NotNull Holder<ArmorMaterial> register(String id, int boots, int leggings, int chestplate, int helmet, int body,
	                                                       int enchantmentValue, Holder<SoundEvent> equipSound, float toughness,
	                                                       float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		return register(id, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, boots);
			map.put(ArmorItem.Type.LEGGINGS, leggings);
			map.put(ArmorItem.Type.CHESTPLATE, chestplate);
			map.put(ArmorItem.Type.HELMET, helmet);
			map.put(ArmorItem.Type.BODY, body);
		}), enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient);
	}

	/**
	 * 注册一个新的护甲材质，使用枚举映射定义各部件防御值
	 *
	 * @param id                  护甲材质的唯一标识符
	 * @param defense             各护甲部件类型对应的防御值映射
	 * @param enchantmentValue    附魔值，影响附魔等级
	 * @param equipSound          装备护甲时播放的声音
	 * @param toughness           护甲韧性值，减少受到的伤害
	 * @param knockbackResistance 击退抗性，减少被击退的距离
	 * @param repairIngredient    用于修复护甲的材料
	 * @return 返回注册后的护甲材质Holder对象
	 */
	private static @NotNull Holder<ArmorMaterial> register(String id, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue,
	                                                       Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
	                                                       Supplier<Ingredient> repairIngredient) {
		List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace(id)));
		return register(id, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
	}

	/**
	 * 注册一个新的护甲材质，可完全自定义配置
	 *
	 * @param id                  护甲材质的唯一标识符
	 * @param defense             各护甲部件类型对应的防御值映射
	 * @param enchantmentValue    附魔值，影响附魔等级
	 * @param equipSound          装备护甲时播放的声音
	 * @param toughness           护甲韧性值，减少受到的伤害
	 * @param knockbackResistance 击退抗性，减少被击退的距离
	 * @param repairIngridient    用于修复护甲的材料（注意：此处拼写与标准不同）
	 * @param layers              护甲的渲染层
	 * @return 返回注册后的护甲材质Holder对象
	 */
	private static @NotNull Holder<ArmorMaterial> register(String id, EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue,
	                                                       Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
	                                                       Supplier<Ingredient> repairIngridient, List<ArmorMaterial.Layer> layers) {
		// 创建新的枚举映射以确保所有护甲类型都有对应的防御值
		EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

		for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
			enummap.put(armoritem$type, defense.get(armoritem$type));
		}
		return ModArmorMaterials.REGISTRY.register(id, () -> new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngridient, layers, toughness,
			knockbackResistance));
	}
}
