package architecture.goldenboughs_lib.init;

import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.common.entiy.corpse.StaffCorpse;
import architecture.goldenboughs_lib.core.Lib;
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn;
import architecture.goldenboughs_lib.util.LcLevelUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LibEntityTypes {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Lib.ID);

	public static final DeferredHolder<EntityType<?>, EntityType<StaffCorpse>> STAFF_CORPSE = register(
		"staff_corpse",
		"职员尸体",
		LcLevel.ZAYIN,
		EntityType.Builder.of(StaffCorpse::new, MobCategory.MISC)
			.clientTrackingRange(128)
			.setUpdateInterval(1)
			.setShouldReceiveVelocityUpdates(true)
			.sized(2, 0.5F)
			.eyeHeight(0.25F)
	);

	private static <I extends Entity> DeferredHolder<EntityType<?>, EntityType<I>> register(String name, String zhName,
	                                                                                        LcLevel lcLevel,
	                                                                                        EntityType.Builder<I> sup) {
		return register(name, zhName, lcLevel, () -> sup.build(name));
	}

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, String zhName,
	                                                                                        LcLevel lcLevel,
	                                                                                        Supplier<EntityType<T>> sup) {
		DeferredHolder<EntityType<?>, EntityType<T>> holder = REGISTRY.register(name, sup);
		LcLevelUtil.lcLevel(lcLevel, holder);
		LibZhCn.addI18nEntityTypeText(zhName, holder);
		return holder;
	}
}
