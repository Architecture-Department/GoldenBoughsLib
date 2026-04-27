package architecture.goldenboughs_lib.datagen;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn;
import architecture.goldenboughs_lib.datagen.tag.LibDatagenBlockTag;
import architecture.goldenboughs_lib.datagen.tag.LibDatagenDamageTypeTag;
import architecture.goldenboughs_lib.datagen.tag.LibDatagenItemTag;
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
public final class Datagen {
	@SubscribeEvent
	public static void gatherData(@NotNull GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();

		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		// 服务端数据生成
		buildServer(event, generator, new LibDatagenDatapackBuiltinEntries(output, completableFuture, new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, LibDamageTypes::bootstrap)));
		LibDatagenBlockTag blockTag = new LibDatagenBlockTag(output, completableFuture, existingFileHelper);
		buildServer(event, generator, blockTag);
		buildServer(event, generator, new LibDatagenItemTag(output, completableFuture, blockTag.contentsGetter(), existingFileHelper));
		buildServer(event, generator, new LibDatagenDamageTypeTag(output, completableFuture, existingFileHelper));

		// 客户端数据生成
		buildClient(event, generator, new LibZhCn(output));
		buildClient(event, generator, new LibDatagenParticle(output, existingFileHelper));
		buildClient(event, generator, new LibDatagenItemModel(output, existingFileHelper));
		buildClient(event, generator, new LibDatagenBlockState(output, existingFileHelper));
		buildClient(event, generator, new LibDatagenSoundDefinitionsProvider(output, existingFileHelper));
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
