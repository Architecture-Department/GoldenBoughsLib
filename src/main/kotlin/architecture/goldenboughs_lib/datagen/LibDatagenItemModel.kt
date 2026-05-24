package architecture.goldenboughs_lib.datagen

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.core.registry.client.LibItemPropertyRenderersRegistrar
import architecture.goldenboughs_lib.init.LibItems
import architecture.goldenboughs_lib.util.client.DatagenItemModelUtil.createModelFile
import architecture.goldenboughs_lib.util.client.DatagenItemModelUtil.getParent
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

/**
 * 物品模型数据生成器
 * 用于为模组中的物品生成对应的模型文件
 *
 * @param output             数据包输出路径
 * @param existingFileHelper 已存在文件助手，用于检查资源是否存在
 */
class LibDatagenItemModel(
	output: PackOutput,
	existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, Lib.ID, existingFileHelper) {
	override fun registerModels() {
		creativeRationalityTool(LibItems.CREATIVE_RATIONALITY_TOOL.get())
		chaosSword(LibItems.CHAOS_SWORD.get())
	}

	/**
	 * 为混沌剑生成模型
	 * 根据不同的伤害类型创建不同的模型变体
	 *
	 * @param item 混沌剑物品
	 */
	private fun chaosSword(item: Item) {
		val map = LinkedHashMap<Float, String>()
		map[0f] = "physics"
		map[0.1f] = "spirit"
		map[0.2f] = "erosion"
		map[0.3f] = "the_soul"
		createModelFile(
			item,
			"weapon/",
			map,
			getParent("item/handheld"),
			LibItemPropertyRenderersRegistrar.CURRENT_LC_DAMAGE_TYPE
		)
	}

	/**
	 * 为创造模式理智值工具生成模型
	 * 根据工具的不同模式创建不同的模型变体
	 *
	 * @param item 创造模式理智值工具物品
	 */
	private fun creativeRationalityTool(item: Item) {
		val map = LinkedHashMap<Float, String>()
		map[0f] = "add"
		map[1f] = "decrease"
		createModelFile(item, "tool/", map, LibItemPropertyRenderersRegistrar.MODE_BOOLEAN)
	}
}
