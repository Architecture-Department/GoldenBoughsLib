package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.datagen.i18n.LibZhCn
import architecture.goldenboughs_lib.datagen.tag.LibDatagenBlockTag
import architecture.goldenboughs_lib.datagen.tag.LibDatagenDamageTypeTag
import architecture.goldenboughs_lib.datagen.tag.LibDatagenItemTag
import architecture.goldenboughs_lib.init.LibDamageTypes
import architecture.goldenboughs_lib.util.buildClient
import architecture.goldenboughs_lib.util.buildServer
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * 数据生成主类
 */
@EventBusSubscriber(modid = Lib.ID)
object Datagen {
	@SubscribeEvent
	fun gatherData(event: GatherDataEvent) {
		val generator = event.generator
		val output = generator.packOutput
		val completableFuture = event.lookupProvider

		val existingFileHelper = event.existingFileHelper
		// 服务端数据生成
		val blockTag = LibDatagenBlockTag(output, completableFuture, existingFileHelper)
		event.buildServer(
			LibDatagenDatapackBuiltinEntries(
				output, completableFuture,
				RegistrySetBuilder()
					.add(Registries.DAMAGE_TYPE, LibDamageTypes::bootstrap)
			),
			blockTag,
			LibDatagenItemTag(output, completableFuture, blockTag.contentsGetter(), existingFileHelper),
			LibDatagenDamageTypeTag(output, completableFuture, existingFileHelper)
		)

		// 客户端数据生成
		event.buildClient(
			LibZhCn(output),
			LibDatagenParticle(output, existingFileHelper),
			LibDatagenItemModel(output, existingFileHelper),
			LibDatagenBlockState(output, existingFileHelper),
			LibDatagenSoundDefinitionsProvider(output, existingFileHelper)
		)
	}
}
