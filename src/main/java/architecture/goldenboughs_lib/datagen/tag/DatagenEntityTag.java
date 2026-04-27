package architecture.goldenboughs_lib.datagen.tag;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.tag.LibEntityTags;
import architecture.imaginarycraft.common.world.entity.ordeals.IOrdealsEntity;
import architecture.imaginarycraft.init.world.entity.OrdealsEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public final class DatagenEntityTag extends EntityTypeTagsProvider {
	public DatagenEntityTag(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, GoldenBoughsLib.ID, existingFileHelper);
	}

	private static EntityType<?>[] getArray(Collection<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entries, Class<? extends IOrdealsEntity> clazz) {
		return entries.stream()
			.filter(entityType -> clazz.isInstance(entityType.get()))
			.map(DeferredHolder::get)
			.toArray(EntityType[]::new);
	}

	@Override
	protected void addTags(final HolderLookup.Provider provider) {
		tag(LibEntityTags.ORDEALS_VIOLET).add(
			OrdealsEntityTypes.GRANT_US_LOVE.get(),
			OrdealsEntityTypes.FRUIT_OF_UNDERSTANDING.get());
		tag(LibEntityTags.ORDEALS_AMBER)/*.add()*/;
		tag(LibEntityTags.ORDEALS_GREEN)/*.add()*/;
		tag(LibEntityTags.ORDEALS_CRIMSON)/*.add()*/;
		tag(LibEntityTags.ORDEALS).addTags(LibEntityTags.ORDEALS_VIOLET, LibEntityTags.ORDEALS_AMBER, LibEntityTags.ORDEALS_GREEN, LibEntityTags.ORDEALS_CRIMSON);
	}
}
