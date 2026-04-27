package architecture.goldenboughs_lib.core.registry;

import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.core.capability.block.BlockLcLevel;
import architecture.goldenboughs_lib.core.capability.block.IBlockLcLevel;
import architecture.goldenboughs_lib.core.capability.entity.EntityLcLevel;
import architecture.goldenboughs_lib.core.capability.entity.IEntityLcLevel;
import architecture.goldenboughs_lib.core.capability.item.IItemLcDamageType;
import architecture.goldenboughs_lib.core.capability.item.IItemLcLevel;
import architecture.goldenboughs_lib.core.capability.item.IItemUsageReq;
import architecture.goldenboughs_lib.core.capability.item.ItemLcLevel;
import architecture.goldenboughs_lib.init.LibCapabilitys;
import architecture.goldenboughs_lib.init.LibItems;
import architecture.goldenboughs_lib.util.LcLevelUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 注册能力
 */
@EventBusSubscriber(modid = GoldenBoughsLib.ID)
public final class CapabilityRegistry {
	public static final List<ItemLike> ITEM_ZAYIN = new ArrayList<>();
	public static final List<ItemLike> ITEM_TETH = new ArrayList<>();
	public static final List<ItemLike> ITEM_HE = new ArrayList<>();
	public static final List<ItemLike> ITEM_WAW = new ArrayList<>();
	public static final List<ItemLike> ITEM_ALEPH = new ArrayList<>();
	public static final List<Supplier<Block>> BLOCK_ZAYIN = new ArrayList<>();
	public static final List<Supplier<Block>> BLOCK_TETH = new ArrayList<>();
	public static final List<Supplier<Block>> BLOCK_HE = new ArrayList<>();
	public static final List<Supplier<Block>> BLOCK_WAW = new ArrayList<>();
	public static final List<Supplier<Block>> BLOCK_ALEPH = new ArrayList<>();
	public static final List<Supplier<EntityType<?>>> ENTITY_ZAYIN = new ArrayList<>();
	public static final List<Supplier<EntityType<?>>> ENTITY_TETH = new ArrayList<>();
	public static final List<Supplier<EntityType<?>>> ENTITY_HE = new ArrayList<>();
	public static final List<Supplier<EntityType<?>>> ENTITY_WAW = new ArrayList<>();
	public static final List<Supplier<EntityType<?>>> ENTITY_ALEPH = new ArrayList<>();

	static {
		ENTITY_ALEPH.add(() -> EntityType.WITHER);
		ENTITY_ALEPH.add(() -> EntityType.ENDER_DRAGON);
		ENTITY_ALEPH.add(() -> EntityType.WARDEN);

		ENTITY_WAW.add(() -> EntityType.RAVAGER);
		ENTITY_WAW.add(() -> EntityType.ELDER_GUARDIAN);
		ENTITY_WAW.add(() -> EntityType.IRON_GOLEM);

		ENTITY_HE.add(() -> EntityType.WITHER_SKELETON);
		ENTITY_HE.add(() -> EntityType.WITCH);
		ENTITY_HE.add(() -> EntityType.VINDICATOR);
		ENTITY_HE.add(() -> EntityType.EVOKER);
		ENTITY_HE.add(() -> EntityType.ZOGLIN);
		ENTITY_HE.add(() -> EntityType.SHULKER);
		ENTITY_HE.add(() -> EntityType.PIGLIN_BRUTE);
		ENTITY_HE.add(() -> EntityType.HOGLIN);
		ENTITY_HE.add(() -> EntityType.GHAST);
		ENTITY_HE.add(() -> EntityType.ENDERMAN);
		ENTITY_HE.add(() -> EntityType.GUARDIAN);

		ENTITY_TETH.add(() -> EntityType.CAVE_SPIDER);
		ENTITY_TETH.add(() -> EntityType.SPIDER);
		ENTITY_TETH.add(() -> EntityType.PIGLIN);
		ENTITY_TETH.add(() -> EntityType.PILLAGER);
		ENTITY_TETH.add(() -> EntityType.VEX);
		ENTITY_TETH.add(() -> EntityType.SILVERFISH);
		ENTITY_TETH.add(() -> EntityType.ENDERMITE);
		ENTITY_TETH.add(() -> EntityType.PHANTOM);
		ENTITY_TETH.add(() -> EntityType.MAGMA_CUBE);
		ENTITY_TETH.add(() -> EntityType.HUSK);
		ENTITY_TETH.add(() -> EntityType.CREEPER);
		ENTITY_TETH.add(() -> EntityType.BREEZE);
		ENTITY_TETH.add(() -> EntityType.DROWNED);
		ENTITY_TETH.add(() -> EntityType.ZOMBIFIED_PIGLIN);
		ENTITY_TETH.add(() -> EntityType.ZOMBIE);
		ENTITY_TETH.add(() -> EntityType.STRAY);
		ENTITY_TETH.add(() -> EntityType.SKELETON);
		ENTITY_TETH.add(() -> EntityType.BOGGED);
		ENTITY_TETH.add(() -> EntityType.BLAZE);
		ENTITY_TETH.add(() -> EntityType.SLIME);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void registerHighest(RegisterCapabilitiesEvent event) {
		itemLevel(event, LcLevel.ZAYIN, ITEM_ZAYIN);
		itemLevel(event, LcLevel.TETH, ITEM_TETH);
		itemLevel(event, LcLevel.HE, ITEM_HE);
		itemLevel(event, LcLevel.WAW, ITEM_WAW);
		itemLevel(event, LcLevel.ALEPH, ITEM_ALEPH);
		itemLevel(event, LcLevel.ALEPH, ITEM_ALEPH);
		itemLevel(event, ItemLcLevel.NULL, LibItems.CHAOS_SWORD, LibItems.CREATIVE_RATIONALITY_TOOL);
		entityLevel(event, LcLevel.ZAYIN, ENTITY_ZAYIN);
		entityLevel(event, LcLevel.TETH, ENTITY_TETH);
		entityLevel(event, LcLevel.HE, ENTITY_HE);
		entityLevel(event, LcLevel.WAW, ENTITY_WAW);
		entityLevel(event, LcLevel.ALEPH, ENTITY_ALEPH);
		blockLevel(event, LcLevel.ZAYIN, BLOCK_ZAYIN);
		blockLevel(event, LcLevel.TETH, BLOCK_TETH);
		blockLevel(event, LcLevel.HE, BLOCK_HE);
		blockLevel(event, LcLevel.WAW, BLOCK_WAW);
		blockLevel(event, LcLevel.ALEPH, BLOCK_ALEPH);
	}

	public static void itemLevel(RegisterCapabilitiesEvent event, IItemLcLevel lcLevel, ItemLike... items) {
		event.registerItem(LibCapabilitys.LcLevel.LC_LEVEL_ITEM,
			(stack, ctx) -> lcLevel, items);
	}

	public static void itemLevel(RegisterCapabilitiesEvent event, @Nullable LcLevel lcLevel, List<ItemLike> items) {
		if (items.isEmpty()) {
			return;
		}
		event.registerItem(LibCapabilitys.LcLevel.LC_LEVEL_ITEM, (stack, ctx) ->
			switch (lcLevel) {
				case ZAYIN -> ItemLcLevel.ZAYIN;
				case TETH -> ItemLcLevel.TETH;
				case HE -> ItemLcLevel.HE;
				case WAW -> ItemLcLevel.WAW;
				case ALEPH -> ItemLcLevel.ALEPH;
				case null -> ItemLcLevel.NULL;
			}, items.toArray(new ItemLike[0])
		);
	}

	public static void blockLevel(RegisterCapabilitiesEvent event, @Nullable LcLevel lcLevel, List<Supplier<Block>> items) {
		if (items.isEmpty()) {
			return;
		}
		event.registerBlock(LibCapabilitys.LcLevel.LC_LEVEL_BLOCK, (level, blockPos, blockState, blockEntity, c) ->
			switch (lcLevel) {
				case ZAYIN -> BlockLcLevel.ZAYIN;
				case TETH -> BlockLcLevel.TETH;
				case HE -> BlockLcLevel.HE;
				case WAW -> BlockLcLevel.WAW;
				case ALEPH -> BlockLcLevel.ALEPH;
				case null -> BlockLcLevel.NULL;
			}, (Block[]) items.stream().map(Supplier::get).toArray()
		);
	}

	public static void entityLevel(RegisterCapabilitiesEvent event, @Nullable LcLevel lcLevel, List<Supplier<EntityType<?>>> items) {
		if (items.isEmpty()) {
			return;
		}
		for (Supplier<EntityType<?>> entityTypeSupplier : items) {
			EntityType<?> entityType = entityTypeSupplier.get();
			LcLevelUtil.ENTITY_TYPE_LEVEL.put(entityType, lcLevel);
			event.registerEntity(LibCapabilitys.LcLevel.LC_LEVEL_ENTITY, entityType, (stack, ctx) ->
				switch (lcLevel) {
					case ZAYIN -> EntityLcLevel.ZAYIN;
					case TETH -> EntityLcLevel.TETH;
					case HE -> EntityLcLevel.HE;
					case WAW -> EntityLcLevel.WAW;
					case ALEPH -> EntityLcLevel.ALEPH;
					case null -> EntityLcLevel.NULL;
				}
			);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerLowest(RegisterCapabilitiesEvent event) {
		for (Item item : BuiltInRegistries.ITEM) {
			registerItem(event, item, IItemLcDamageType.class, LibCapabilitys.LC_DAMAGE_TYPE_ITEM);
			registerItem(event, item, IItemUsageReq.class, LibCapabilitys.USAGE_REQ_ITEM);
			registerItem(event, item, IItemLcLevel.class, LibCapabilitys.LcLevel.LC_LEVEL_ITEM);
		}

		for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
			registerEntity(event, entityType, IEntityLcLevel.class, LibCapabilitys.LcLevel.LC_LEVEL_ENTITY);
		}

		for (BlockEntityType<?> blockEntityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
			registerBlockEntity(event, blockEntityType, IBlockLcLevel.class, LibCapabilitys.LcLevel.LC_LEVEL_BLOCK);
		}

		for (Block block : BuiltInRegistries.BLOCK) {
			registerBlock(event, block, IBlockLcLevel.class, LibCapabilitys.LcLevel.LC_LEVEL_BLOCK);
		}
	}

	public static <T, C> void registerItem(
		RegisterCapabilitiesEvent event,
		Item item,
		Class<T> capabilityClass,
		ItemCapability<T, C> capability
	) {
		if (!capabilityClass.isInstance(item) || event.isItemRegistered(capability, item)) {
			return;
		}
		event.registerItem(capability, (stack, ctx) ->
			capabilityClass.cast(item), item);
	}

	public static <T, C> void registerBlock(
		RegisterCapabilitiesEvent event,
		Block block,
		Class<T> capabilityClass,
		BlockCapability<T, C> capability
	) {
		if (!capabilityClass.isInstance(block) || event.isBlockRegistered(capability, block)) {
			return;
		}
		event.registerBlock(capability, (level, blockPos, blockState, blockEntity, c) ->
			capabilityClass.cast(block), block);
	}

	public static <T, C> void registerBlockEntity(
		RegisterCapabilitiesEvent event,
		BlockEntityType<?> block,
		Class<T> capabilityClass,
		BlockCapability<T, C> capability
	) {
		event.registerBlockEntity(capability, block, (blockEntity, c) ->
			capabilityClass.isInstance(blockEntity) ? capabilityClass.cast(blockEntity) : null);
	}

	public static <T, C> void registerEntity(
		RegisterCapabilitiesEvent event,
		EntityType<?> entityType,
		Class<T> capabilityClass,
		EntityCapability<T, C> capability
	) {
		if (event.isEntityRegistered(capability, entityType)) {
			return;
		}
		event.registerEntity(capability, entityType, (entity, ctx) ->
			capabilityClass.isInstance(entity) ? capabilityClass.cast(entity) : null);
	}
}
