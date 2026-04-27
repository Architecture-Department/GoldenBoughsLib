package architecture.goldenboughs_lib.datagen.tag;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.tag.LibItemTags;
import architecture.imaginarycraft.core.ImaginaryCraftConstants;
import architecture.imaginarycraft.init.world.item.ToolItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public final class DatagenItemTag extends ItemTagsProvider {
	public DatagenItemTag(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final CompletableFuture<TagLookup<Block>> blockTags, final ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTags, GoldenBoughsLib.ID, existingFileHelper);
	}

	@Override
	protected void addTags(final HolderLookup.Provider provider) {
		//region E.G.O
		//region 饰品
		addSet(LibItemTags.EGO_CURIOS_HEADWEAR, ImaginaryCraftConstants.EGO_CURIOS_HEADWEAR);
		addSet(LibItemTags.EGO_CURIOS_HEAD, ImaginaryCraftConstants.EGO_CURIOS_HEAD);
		addSet(LibItemTags.EGO_CURIOS_HINDBRAIN, ImaginaryCraftConstants.EGO_CURIOS_HINDBRAIN);
		addSet(LibItemTags.EGO_CURIOS_EYE, ImaginaryCraftConstants.EGO_CURIOS_EYE);
		addSet(LibItemTags.EGO_CURIOS_FACE, ImaginaryCraftConstants.EGO_CURIOS_FACE);
		addSet(LibItemTags.EGO_CURIOS_CHEEK, ImaginaryCraftConstants.EGO_CURIOS_CHEEK);
		addSet(LibItemTags.EGO_CURIOS_MASK, ImaginaryCraftConstants.EGO_CURIOS_MASK);
		addSet(LibItemTags.EGO_CURIOS_MOUTH, ImaginaryCraftConstants.EGO_CURIOS_MOUTH);
		addSet(LibItemTags.EGO_CURIOS_NECK, ImaginaryCraftConstants.EGO_CURIOS_NECK);
		addSet(LibItemTags.EGO_CURIOS_BROOCH, ImaginaryCraftConstants.EGO_CURIOS_BROOCH);
		addSet(LibItemTags.EGO_CURIOS_HAND, ImaginaryCraftConstants.EGO_CURIOS_HAND);
		addSet(LibItemTags.EGO_CURIOS_GLOVE, ImaginaryCraftConstants.EGO_CURIOS_GLOVE);
		addSet(LibItemTags.EGO_CURIOS_BACK, ImaginaryCraftConstants.EGO_CURIOS_BACK);
		tag(LibItemTags.EGO_CURIOS).addTags(
			LibItemTags.EGO_CURIOS_HEADWEAR,
			LibItemTags.EGO_CURIOS_CHEEK,
			LibItemTags.EGO_CURIOS_HEAD,
			LibItemTags.EGO_CURIOS_HINDBRAIN,
			LibItemTags.EGO_CURIOS_EYE,
			LibItemTags.EGO_CURIOS_FACE,
			LibItemTags.EGO_CURIOS_MASK,
			LibItemTags.EGO_CURIOS_MOUTH,
			LibItemTags.EGO_CURIOS_NECK,
			LibItemTags.EGO_CURIOS_BROOCH,
			LibItemTags.EGO_CURIOS_HAND,
			LibItemTags.EGO_CURIOS_GLOVE,
			LibItemTags.EGO_CURIOS_BACK);
		//endregion

		addSet(LibItemTags.EGO_ARMOUR, ImaginaryCraftConstants.EGO_ARMOUR);
		addSet(LibItemTags.EGO_TOOL, ImaginaryCraftConstants.EGO_TOOL);
		addSet(LibItemTags.EGO_WEAPON, ImaginaryCraftConstants.EGO_WEAPON)
			.add(ToolItems.CHAOS_SWORD.get());
		addSet(LibItemTags.EGO, ImaginaryCraftConstants.EGO).addTags(
			LibItemTags.EGO_CURIOS,
			LibItemTags.EGO_ARMOUR,
			LibItemTags.EGO_WEAPON,
			LibItemTags.EGO_TOOL);

		addSet(ItemTags.HEAD_ARMOR, ImaginaryCraftConstants.HEAD_ARMOR);
		addSet(ItemTags.CHEST_ARMOR, ImaginaryCraftConstants.CHEST_ARMOR);
		addSet(ItemTags.LEG_ARMOR, ImaginaryCraftConstants.LEG_ARMOR);
		addSet(ItemTags.FOOT_ARMOR, ImaginaryCraftConstants.FOOT_ARMOR);
		tag(Tags.Items.ARMORS).addTag(LibItemTags.EGO_ARMOUR);
		//endregion

		//region 近战武器
		ImaginaryCraftConstants.MELEE.addAll(ImaginaryCraftConstants.KNIFE);
		ImaginaryCraftConstants.MELEE.addAll(ImaginaryCraftConstants.HAMMER);
		ImaginaryCraftConstants.MELEE.addAll(ImaginaryCraftConstants.FIST);
		ImaginaryCraftConstants.MELEE.addAll(ImaginaryCraftConstants.SPEAR);
		ImaginaryCraftConstants.MELEE.addAll(ImaginaryCraftConstants.MACE);
		addSet(LibItemTags.MELEE, ImaginaryCraftConstants.MELEE).addTag(ItemTags.SWORDS);
		//endregion

		//region 远程武器
		ImaginaryCraftConstants.GUN.addAll(ImaginaryCraftConstants.CANNON);
		ImaginaryCraftConstants.GUN.addAll(ImaginaryCraftConstants.PISTOL);
		ImaginaryCraftConstants.GUN.addAll(ImaginaryCraftConstants.RIFLE);
		addSet(LibItemTags.GUN, ImaginaryCraftConstants.GUN);

		addSet(LibItemTags.REMOTE, ImaginaryCraftConstants.REMOTE).addTags(
			Tags.Items.TOOLS_CROSSBOW,
			Tags.Items.TOOLS_BOW,
			LibItemTags.GUN);
		//endregion

		addSet(ItemTags.BOW_ENCHANTABLE, ImaginaryCraftConstants.BOW);
		addSet(ItemTags.CROSSBOW_ENCHANTABLE, ImaginaryCraftConstants.CROSSBOW);

		addSet(Tags.Items.TOOLS_CROSSBOW, ImaginaryCraftConstants.CROSSBOW);
		addSet(Tags.Items.TOOLS_BOW, ImaginaryCraftConstants.BOW);
		addSet(ItemTags.AXES, ImaginaryCraftConstants.AXE);

		ImaginaryCraftConstants.SWORDS.addAll(ImaginaryCraftConstants.KNIFE);
		addSet(ItemTags.SWORDS, ImaginaryCraftConstants.SWORDS)
			.add(ToolItems.CHAOS_SWORD.get());
		tag(ItemTags.SWORD_ENCHANTABLE).addTag(LibItemTags.MELEE);
		tag(ItemTags.BREAKS_DECORATED_POTS).addTag(LibItemTags.MELEE);
		addSet(LibItemTags.SPECIAL, ImaginaryCraftConstants.SPECIAL);
		tag(Tags.Items.TOOLS)
			.add(ToolItems.CREATIVE_RATIONALITY_TOOL.get())
			.addTags(
				LibItemTags.EGO_WEAPON,
				LibItemTags.EGO_TOOL,
				LibItemTags.SPECIAL,
				LibItemTags.REMOTE,
				LibItemTags.MELEE);
	}

	private @NotNull IntrinsicTagAppender<Item> addSet(TagKey<Item> tag, @NotNull Set<DeferredItem<? extends Item>> set) {
		return tag(tag).add(set.stream().map(DeferredHolder::get).toArray(Item[]::new));
	}
}
