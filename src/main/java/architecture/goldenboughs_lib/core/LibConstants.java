package architecture.goldenboughs_lib.core;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashSet;
import java.util.Set;

public final class LibConstants {
	public static final Set<DeferredItem<? extends Item>> EGO_ARMOUR = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> EGO_WEAPON = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> EGO_TOOL = new HashSet<>();
	// 例如道具类的物品等没有特殊分类的物品，例如：安吉拉的图书馆
	public static final Set<DeferredItem<? extends Item>> EGO = new HashSet<>();

	// 武器的分类
	public static final Set<DeferredItem<? extends Item>> REMOTE = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> MELEE = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> SPECIAL = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> GUN = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> PISTOL = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> RIFLE = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> CANNON = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> CROSSBOW = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> BOW = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> KNIFE = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> HAMMER = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> FIST = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> SPEAR = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> AXE = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> MACE = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> SWORDS = new HashSet<>();

	// 盔甲的分类
	public static final Set<DeferredItem<? extends Item>> HEAD_ARMOR = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> CHEST_ARMOR = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> LEG_ARMOR = new HashSet<>();
	public static final Set<DeferredItem<? extends Item>> FOOT_ARMOR = new HashSet<>();
}
