package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.datagen.i18n.DatagenI18n;
import architecture.goldenboughs_lib.datagen.tag.DatagenBlockTag;
import architecture.goldenboughs_lib.datagen.tag.DatagenDamageTypeTag;
import architecture.goldenboughs_lib.datagen.tag.DatagenEntityTag;
import architecture.goldenboughs_lib.datagen.tag.DatagenItemTag;
import architecture.goldenboughs_lib.init.LibDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * 数据生成主类
 */
@EventBusSubscriber(modid = GoldenBoughsLib.ID)
public final class LibDatagen {
	@SubscribeEvent
	public static void gatherData(@NotNull GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();

		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		// 服务端数据生成
		buildServer(event, generator, new DatagenDatapackBuiltinEntries(output, new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, LibDamageTypes::bootstrap),
			completableFuture));
		DatagenBlockTag blockTag = new DatagenBlockTag(output, completableFuture, existingFileHelper);
		buildServer(event, generator, blockTag);
		buildServer(event, generator, new DatagenItemTag(output, completableFuture, blockTag.contentsGetter(), existingFileHelper));
		buildServer(event, generator, new DatagenDamageTypeTag(output, completableFuture, existingFileHelper));
		buildServer(event, generator, new DatagenEntityTag(output, completableFuture, existingFileHelper));
		buildServer(event, generator, new DatagenCuriosTest(output, existingFileHelper, completableFuture));

		// 客户端数据生成
		DatagenI18n.init(output).forEach(i18 -> buildClient(event, generator, i18));
		buildClient(event, generator, new DatagenParticle(output, existingFileHelper));
		buildClient(event, generator, new DatagenItemModel(output, existingFileHelper));
		buildClient(event, generator, new architecture.imaginarycraft.datagen.DatagenBlockState(output, existingFileHelper));
		buildClient(event, generator, new DatagenSoundDefinitionsProvider(output, existingFileHelper));
	}

	private static <T extends DataProvider> @NotNull T buildClient(@NotNull GatherDataEvent event,
	                                                               @NotNull DataGenerator generator,
	                                                               T provider) {
		return generator.addProvider(event.includeClient(), provider);
	}

	private static <T extends DataProvider> @NotNull T buildServer(@NotNull GatherDataEvent event,
	                                                               @NotNull DataGenerator generator,
	                                                               T provider) {
		return generator.addProvider(event.includeServer(), provider);
	}
}
