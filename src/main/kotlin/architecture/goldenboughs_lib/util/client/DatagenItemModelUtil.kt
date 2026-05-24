package architecture.goldenboughs_lib.util.client

import architecture.goldenboughs_lib.mixed.client.IModelBuilder
import architecture.goldenboughs_lib.util.LibUtil.rlOf
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*
import kotlin.math.min

object DatagenItemModelUtil {
	/**
	 * 为所有物品生成模型
	 * 遍历所有注册的物品条目并为其创建基础的generated模型
	 *
	 * @param registry   物品注册表
	 * @param pathPrefix 模型路径前缀
	 */
	@JvmStatic
	fun ItemModelProvider.withExistingParent(registry: DeferredRegister.Items, pathPrefix: String) {
		registry.entries.stream().map { it.id }.forEach { itemId: ResourceLocation ->
			IModelBuilder.of(this.withExistingParent(itemId.path, "item/generated"))
				.`goldenboughs_lib$getTexture`()["layer0"] = itemId.withPrefix(pathPrefix).toString()
		}
	}

	/**
	 * 获取指定名称的父模型文件
	 *
	 * @param name 父模型名称
	 * @return 父模型文件
	 */
	@JvmStatic
	fun getParent(name: String): ModelFile.UncheckedModelFile {
		return ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace(name))
	}

	/**
	 * 为物品创建带有不同纹理的模型文件
	 * 根据提供的纹理映射和谓词创建多个模型变体
	 * 使用默认的"item/generated"作为父模型
	 *
	 * @param item       物品实例
	 * @param prefix     前缀
	 * @param textures   纹理映射，键为浮点数值，值为纹理名称
	 * @param predicates 谓词资源位置数组，用于确定何时使用哪个模型变体
	 */
	@JvmStatic
	fun ItemModelProvider.createModelFile(
		item: Item, prefix: String, textures: MutableMap<Float, String>, vararg predicates: ResourceLocation
	) {
		createModelFile(item, prefix, textures, null, *predicates)
	}

	/**
	 * 创建模型文件
	 *
	 * @param item 物品实例
	 * @param name 模型名称
	 * @return 模型文件
	 */
	@JvmStatic
	fun createModelFile(item: Item, name: String): ModelFile.UncheckedModelFile {
		return ModelFile.UncheckedModelFile(getItemResourceLocation(item, name).withPrefix("item/"))
	}

	/**
	 * 获取物品的资源位置
	 *
	 * @param item 物品实例
	 * @param name 名称后缀
	 * @return 资源位置
	 */
	@JvmStatic
	fun getItemResourceLocation(item: Item, name: String): ResourceLocation {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).withSuffix("_$name")
	}

	/**
	 * 创建特殊物品模型
	 *
	 * @param item 物品实例
	 * @param name 模型名称
	 * @return 物品模型构建器
	 */
	@JvmStatic
	fun ItemModelProvider.specialItem(item: Item, name: String): ItemModelBuilder {
		return basicItem(getItemResourceLocation(item, name))
	}

	/**
	 * 创建模型物品
	 *
	 * @param item   物品实例
	 * @param parent 父模型文件
	 * @return 物品模型构建器
	 */
	@JvmStatic
	fun ItemModelProvider.createModelItem(item: Item, parent: ModelFile): ItemModelBuilder {
		val resourceLocation = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))
		return getBuilder(item.toString()).parent(parent).texture(
			"layer0", rlOf(
				resourceLocation.namespace, "item/" + resourceLocation.path
			)
		)
	}

	/**
	 * 用于给 GEO 模型生成的
	 */
	@JvmStatic
	fun ItemModelProvider.geoItem(item: Item) {
		getBuilder(item.toString()).parent(ModelFile.UncheckedModelFile(rlOf("builtin/entity")))
	}

	/**
	 * 创建基础物品模型
	 *
	 * @param item 物品实例
	 * @param name 模型名称
	 * @return 物品模型构建器
	 */
	@JvmStatic
	fun ItemModelProvider.basicItem(item: Item, name: String): ItemModelBuilder {
		return basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)), name)
	}

	/**
	 * 创建基础物品模型
	 *
	 * @param item 物品资源位置
	 * @param name 模型名称
	 * @return 物品模型构建器
	 */
	@JvmStatic
	fun ItemModelProvider.basicItem(item: ResourceLocation, name: String): ItemModelBuilder {
		return getBuilder(item.toString()).parent(
			uncheckedModelFile(rlOf(item.namespace, "models/item/$name"))
		).texture("layer0", rlOf(item.namespace, "item/" + item.path))
	}

	@JvmStatic
	fun uncheckedModelFile(location: ResourceLocation): ModelFile.UncheckedModelFile =
		ModelFile.UncheckedModelFile(location)

	@JvmStatic
	fun ItemModelProvider.createModelFile(
		item: Item,
		prefix: String,
		textures: Map<Float, String>,
		parent: ModelFile? = null,
		vararg predicates: ResourceLocation
	) {
		val resourceLocation = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item))
		val itemModId = resourceLocation.namespace
		val itemRl = "item/$prefix${resourceLocation.path}"

		val actualParent = parent ?: ModelFile.UncheckedModelFile("item/generated")
		val modelBuilder = getBuilder(item.toString()).parent(actualParent)
			.texture("layer0", rlOf(itemModId, itemRl))

		var index = 0
		for ((key, value) in textures) {
			val overrideModelRl = getItemResourceLocation(item, value).withPrefix("item/")
			modelBuilder.override().model(ModelFile.UncheckedModelFile(overrideModelRl))
				.predicate(predicates[min(index, predicates.size - 1)], key).end()

			getBuilder(overrideModelRl.toString()).parent(actualParent)
				.texture("layer0", rlOf(itemModId, "${itemRl}_$value"))
			index++
		}
	}
}